package com.kraz3d.game;

import com.google.common.base.Stopwatch;
import com.google.common.io.Closer;
import com.kraz3d.engine.Vertex;
import com.kraz3d.opengl.*;
import com.kraz3d.opengl.Error;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
            final long window = GLFW.glfwCreateWindow(320, 240, "Game", MemoryUtil.NULL, MemoryUtil.NULL);
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
                    crates.stream()
                            .map(Crate::getDrawElementsCommand)
                            .flatMap(Collection::stream)
                            .collect(Collectors.groupingBy(DrawElementsCommand::getProgram))
                            .forEach((program, commands) -> useProgramAndExecuteCommands(program, commands, projectionMatrixBuffer, viewMatrixBuffer));
                    VertexArray.unbind();
                    Program.disuse();
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

    private static void useProgramAndExecuteCommands(final Program program, final Collection<DrawElementsCommand> commands, final FloatBuffer projectionMatrixBuffer, final FloatBuffer viewMatrixBuffer) {
        program.use();
        final Collection<Uniform> uniforms = program.getUniforms();
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
        commands.stream()
                .collect(Collectors.groupingBy(DrawElementsCommand::getVertexArray))
                .forEach(Main::bindVertexArrayAndExecuteCommands);
    }

    private static void bindVertexArrayAndExecuteCommands(final VertexArray vertexArray, final Collection<DrawElementsCommand> commands) {
        vertexArray.bind();
        commands.stream()
                .collect(Collectors.groupingBy(DrawElementsCommand::getPrimitiveType))
                .forEach(Main::executeCommands);
    }

    private static void executeCommands(final PrimitiveType primitiveType, final Collection<DrawElementsCommand> commands) {
        final int mode = primitiveType.getGLPrimitiveType();
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer countBuffer = memoryStack.ints(commands.stream().mapToInt(DrawElementsCommand::getCount).toArray());
            final PointerBuffer indicesBuffer = memoryStack.pointers(commands.stream().mapToLong(DrawElementsCommand::getByteOffset).toArray());
            GL14.glMultiDrawElements(mode, countBuffer, GL11.GL_UNSIGNED_INT, indicesBuffer);
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

        final ArrayBuffer arrayBuffer = ArrayBuffer.generate(1)
                .stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);
        final ElementArrayBuffer elementArrayBuffer = ElementArrayBuffer.generate(1)
                .stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);
        final VertexArray vertexArray = VertexArray.generate(1)
                .stream()
                .findFirst()
                .orElseThrow(RuntimeException::new);

        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {

            final FloatBuffer verticesBuffer = memoryStack.mallocFloat(CrateResource.getVerticesDataSize());
            CrateResource.getVerticesData(verticesBuffer);

            arrayBuffer.bind();
            arrayBuffer.data(verticesBuffer, UsageType.STATIC_DRAW);
            ArrayBuffer.unbind();

            final IntBuffer indicesBuffer = memoryStack.mallocInt(CrateResource.getIndicesDataSize());
            CrateResource.getIndicesData(indicesBuffer);

            elementArrayBuffer.bind();
            elementArrayBuffer.data(indicesBuffer, UsageType.STATIC_DRAW);
            ElementArrayBuffer.unbind();

            final Attribute vertexPosition = attributes.stream()
                    .filter(attribute -> "vertex_position".equals(attribute.getName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            final Attribute vertexColor = attributes.stream()
                    .filter(attribute -> "vertex_color".equals(attribute.getName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            vertexArray.bind();
            arrayBuffer.bind();
            elementArrayBuffer.bind();

            GL20.glEnableVertexAttribArray(vertexPosition.getLocation());
            GL20.glVertexAttribPointer(vertexPosition.getLocation(), 4, GL11.GL_FLOAT, false, Vertex.getStride(), Vertex.getPositionPointer());

            GL20.glEnableVertexAttribArray(vertexColor.getLocation());
            GL20.glVertexAttribPointer(vertexColor.getLocation(), 4, GL11.GL_FLOAT, false, Vertex.getStride(), Vertex.getColorPointer());

            VertexArray.unbind();

            final Collection<DrawElementsCommand> drawElementsCommands = LongStream.iterate(0, byteOffset -> byteOffset + 4 * Integer.BYTES)
                    .limit(6)
                    .mapToObj(byteOffset -> new DrawElementsCommand.Builder()
                            .setProgram(program)
                            .setVertexArray(vertexArray)
                            .setPrimitiveType(PrimitiveType.TRIANGLE_STRIP)
                            .setCount(4)
                            .setByteOffset(byteOffset)
                            .build())
                    .collect(Collectors.toList());

            return Collections.unmodifiableCollection(Collections.singletonList(new Crate.Builder()
                    .setDrawElementsCommands(drawElementsCommands)
                    .setArrayBuffer(arrayBuffer)
                    .setElementArrayBuffer(elementArrayBuffer)
                    .build()));

        }
    }

    private static void deleteCubes(final Collection<Crate> crates) {
        crates.stream()
                .map(Crate::getDrawElementsCommand)
                .flatMap(Collection::stream)
                .map(DrawElementsCommand::getProgram)
                .distinct()
                .forEach(Program::delete);
        final List<VertexArray> vertexArrays = crates.stream()
                .map(Crate::getDrawElementsCommand)
                .flatMap(Collection::stream)
                .map(DrawElementsCommand::getVertexArray)
                .distinct()
                .collect(Collectors.toList());
        VertexArray.delete(vertexArrays);
        final List<Buffer> buffers = crates.stream()
                .map(crate -> Arrays.asList(crate.getArrayBuffer(), crate.getElementArrayBuffer()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        Buffer.delete(buffers);
    }

}
