package com.kraz3d.game;

import com.google.common.base.Stopwatch;
import com.google.common.io.Closer;
import com.kraz3d.engine.Vertex;
import com.kraz3d.opengl.*;
import com.kraz3d.opengl.Error;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {

    private static final Logger Log = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        System.setProperty(Configuration.DEBUG.getProperty(), "true");
        System.setProperty(Configuration.DEBUG_FUNCTIONS.getProperty(), "true");
        System.setProperty(Configuration.DEBUG_MEMORY_ALLOCATOR.getProperty(), "true");
        System.setProperty(Configuration.DEBUG_STACK.getProperty(), "true");
        System.setProperty(Configuration.DEBUG_STREAM.getProperty(), "true");
        System.setProperty(Configuration.DISABLE_CHECKS.getProperty(), "false");
        try (final Closer closer = Closer.create()) {
            if (!GLFW.glfwInit()) {
                throw new RuntimeException();
            }
            closer.register(GLFW::glfwTerminate);
            GLFW.glfwDefaultWindowHints();
            GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
            GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
            final long window = GLFW.glfwCreateWindow(800, 600, "Game", MemoryUtil.NULL, MemoryUtil.NULL);
            if (window == MemoryUtil.NULL) {
                throw new RuntimeException();
            }
            closer.register(() -> GLFW.glfwDestroyWindow(window));
            GLFW.glfwMakeContextCurrent(window);
            closer.register(() -> GLFW.glfwMakeContextCurrent(MemoryUtil.NULL));
            GL.setCapabilities(GL.createCapabilities(true));
            GLFW.glfwSwapInterval(1);
            GLFW.glfwShowWindow(window);
            closer.register(() -> GLFW.glfwHideWindow(window));
            try (final MemoryStack stack = MemoryStack.stackPush()) {
                final IntBuffer widthBuffer = stack.mallocInt(1);
                final IntBuffer heightBuffer = stack.mallocInt(1);
                final Collection<Crate> crates = createCubes();
                final FloatBuffer clearColorBuffer = stack.floats(0.5f, 0.5f, 0.5f, 1.0f);
                final FloatBuffer clearDepthBuffer = stack.floats(1.0f);
                final FloatBuffer projectionMatrixBuffer = stack.mallocFloat(16);
                final FloatBuffer viewMatrixBuffer = stack.mallocFloat(16);
                final Stopwatch stopwatch = Stopwatch.createStarted();
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glCullFace(GL11.GL_BACK);
                GL11.glFrontFace(GL11.GL_CCW);
                GL11.glEnable(GL31.GL_PRIMITIVE_RESTART);
                GL31.glPrimitiveRestartIndex(-1);
                while (!GLFW.glfwWindowShouldClose(window)) {
                    GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
                    final int width = widthBuffer.get(0);
                    final int height = heightBuffer.get(0);
                    final float aspect = ((float) width) / height;
                    final Matrix4f projectionMatrix = new Matrix4f();
                    projectionMatrix.identity();
                    projectionMatrix.perspective((float) Math.toRadians(45.0f), aspect, 0.01f, 100.0f);
                    final double seconds = stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0;
                    final Vector3f eye = new Vector3f((float) Math.cos(seconds), 1, (float) Math.sin(seconds))
                            .mul(2);
                    final Matrix4f viewMatrix = new Matrix4f();
                    viewMatrix.identity();
                    viewMatrix.lookAt(
                            eye,
                            new Vector3f(0, 0, 0),
                            new Vector3f(0, 1, 0)
                    );
                    projectionMatrix.get(projectionMatrixBuffer);
                    viewMatrix.get(viewMatrixBuffer);
                    GL11.glViewport(0, 0, width, height);
                    GL30.glClearBufferfv(GL11.GL_COLOR, 0, clearColorBuffer);
                    GL30.glClearBufferfv(GL11.GL_DEPTH, 0, clearDepthBuffer);
                    for (final Crate crate : crates) {
                        final List<Uniform> uniforms = crate.getProgram().getUniforms();
                        crate.getProgram().use();
                        final Uniform projectionMatrixUniform = uniforms.stream()
                                .filter(uniform -> "projection_matrix".equals(uniform.getName()))
                                .findFirst()
                                .orElseThrow(RuntimeException::new);
                        final Uniform viewMatrixUniform = uniforms.stream()
                                .filter(uniform -> "view_matrix".equals(uniform.getName()))
                                .findFirst()
                                .orElseThrow(RuntimeException::new);
                        GL20.glUniformMatrix4fv(projectionMatrixUniform.getLocation(), false, projectionMatrixBuffer);
                        GL20.glUniformMatrix4fv(viewMatrixUniform.getLocation(), false, viewMatrixBuffer);
                        crate.getVertexArray().bind();
                        GL11.glDrawElements(GL11.GL_TRIANGLE_STRIP, CrateResource.getIndicesDataSize(), GL11.GL_UNSIGNED_SHORT, 0);
                        VertexArray.unbind();
                        Program.disuse();
                    }
                    ErrorType error;
                    while ((error = Error.get()) != ErrorType.NO_ERROR) {
                        Log.error("{}", error);
                    }
                    GLFW.glfwSwapBuffers(window);
                    GLFW.glfwPollEvents();
                }
                deleteCubes(crates);
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Collection<Crate> createCubes() {

        final Shader vertexShader = Shader.create(ShaderType.VERTEX_SHADER);
        vertexShader.source(CrateResource.getVertexShaderSource());
        vertexShader.compile();

        final Shader fragmentShader = Shader.create(ShaderType.FRAGMENT_SHADER);
        fragmentShader.source(CrateResource.getFragmentShaderSource());
        fragmentShader.compile();

        final Program program = Program.create();
        program.attach(vertexShader);
        program.attach(fragmentShader);
        program.link();
        if (!program.getLinkStatus()) {
            final String infoLog = program.getInfoLog();
            Log.error(infoLog);
        }
        vertexShader.delete();
        fragmentShader.delete();

        final List<Attribute> attributes = program.getAttributes();
//        final List<Uniform> uniforms = program.getUniforms();

        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {

            final IntBuffer vboBuffer = memoryStack.mallocInt(2);
            final VertexArray vertexArray = VertexArray.generate(1)
                    .stream()
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            GL15.glGenBuffers(vboBuffer);

            final FloatBuffer verticesBuffer = memoryStack.mallocFloat(CrateResource.getVerticesDataSize());
            CrateResource.getVerticesData(verticesBuffer);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboBuffer.get(0));
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            final ShortBuffer indicesBuffer = memoryStack.mallocShort(CrateResource.getIndicesDataSize());
            CrateResource.getIndicesData(indicesBuffer);

            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboBuffer.get(1));
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

            final Attribute vertexPosition = attributes.stream()
                    .filter(attribute -> "vertex_position".equals(attribute.getName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            final Attribute vertexColor = attributes.stream()
                    .filter(attribute -> "vertex_color".equals(attribute.getName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            vertexArray.bind();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboBuffer.get(0));
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboBuffer.get(1));

            GL20.glEnableVertexAttribArray(vertexPosition.getLocation());
            GL20.glVertexAttribPointer(vertexPosition.getLocation(), 4, GL11.GL_FLOAT, false, Vertex.getStride(), Vertex.getPositionPointer());

            GL20.glEnableVertexAttribArray(vertexColor.getLocation());
            GL20.glVertexAttribPointer(vertexColor.getLocation(), 4, GL11.GL_FLOAT, false, Vertex.getStride(), Vertex.getColorPointer());

            VertexArray.unbind();

            return Collections.unmodifiableCollection(Collections.singletonList(new Crate.Builder()
                    .setProgram(program)
                    .setVertexArray(vertexArray)
                    .setArrayBuffer(vboBuffer.get(0))
                    .setElementArrayBuffer(vboBuffer.get(1))
                    .build()));

        }
    }

    private static void deleteCubes(final Collection<Crate> crates) {
        crates.stream()
                .map(Crate::getProgram)
                .forEach(Program::delete);
        final List<VertexArray> vertexArrays = crates.stream()
                .map(Crate::getVertexArray)
                .distinct()
                .collect(Collectors.toList());
        VertexArray.delete(vertexArrays);
        try (final MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer vboBuffer = stack.ints(
                    crates.stream()
                            .map(crate -> new int[]{
                                    crate.getArrayBuffer(),
                                    crate.getElementArrayBuffer()})
                            .flatMapToInt(Arrays::stream)
                            .distinct()
                            .toArray());
            GL15.glDeleteBuffers(vboBuffer);
        }
    }

}
