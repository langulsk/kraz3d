package com.kraz3d.game;

import com.google.common.io.Closer;
import com.kraz3d.opengl.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(final String[] args) {
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
                while (!GLFW.glfwWindowShouldClose(window)) {
                    GLFW.glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
                    final int width = widthBuffer.get(0);
                    final int height = heightBuffer.get(0);
                    final FloatBuffer projectionMatrixBuffer = stack.mallocFloat(16);
//                    final float aspect = ((float) width) / height;
//                    final Matrix4f projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), aspect, 0.01f, 100.0f);
//                    final Matrix4f viewMatrix = new Matrix4f().lookAt(
//                            new Vector3f(0, 0, 1),
//                            new Vector3f(0, 0, 0),
//                            new Vector3f(0, 1, 0)
//                    );
//                    projectionMatrix.get(projectionMatrixBuffer);
                    GL11.glViewport(0, 0, width, height);
                    GL30.glClearBufferfv(GL11.GL_COLOR, 0, clearColorBuffer);
                    for (final Crate crate : crates) {
                        final List<Uniform> uniforms = crate.getProgram().getUniforms();
                        crate.getProgram().use();
//                        final Uniform projectionMatrixUniform = uniforms.stream()
//                                .filter(uniform -> "projection_matrix".startsWith(uniform.getName()))
//                                .findFirst()
//                                .orElseThrow(RuntimeException::new);
//                        GL20.glUniformMatrix4fv(projectionMatrixUniform.getLocation(), false, projectionMatrixBuffer);
                        crate.getVertexArray().bind();
                        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, 16);
                        VertexArray.unbind();
                        Program.disuse();
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
        vertexShader.delete();
        fragmentShader.delete();

        final List<Attribute> attributes = program.getAttributes();
//        final List<Uniform> uniforms = program.getUniforms();

        try (final MemoryStack stack = MemoryStack.stackPush()) {

            final IntBuffer vboBuffer = stack.mallocInt(1);
            final VertexArray vertexArray = VertexArray.generate(1)
                    .stream()
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            GL15.glGenBuffers(vboBuffer);

            final FloatBuffer verticesBuffer = stack.floats(
                    -0.5f, -0.5f, 0.5f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                    0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
                    0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                    -0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboBuffer.get(0));
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            final Attribute vertexPosition = attributes.stream()
                    .filter(attribute -> "vertex_position".startsWith(attribute.getName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            final Attribute vertexColor = attributes.stream()
                    .filter(attribute -> "vertex_color".startsWith(attribute.getName()))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            final int stride = Float.BYTES * 4 + Float.BYTES * 4;
            final int vertexPositionPointer = 0;
            final int vertexColorPointer = Float.BYTES * 4;

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboBuffer.get(0));
            vertexArray.bind();

            GL20.glEnableVertexAttribArray(vertexPosition.getLocation());
            GL20.glVertexAttribPointer(vertexPosition.getLocation(), 4, GL11.GL_FLOAT, false, stride, vertexPositionPointer);

            GL20.glEnableVertexAttribArray(vertexColor.getLocation());
            GL20.glVertexAttribPointer(vertexColor.getLocation(), 4, GL11.GL_FLOAT, false, stride, vertexColorPointer);

            VertexArray.unbind();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            return Collections.unmodifiableCollection(Collections.singletonList(new Crate.Builder()
                    .setProgram(program)
                    .setVertexArray(vertexArray)
                    .setArrayBuffer(vboBuffer.get(0))
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
                            .mapToInt(Crate::getArrayBuffer)
                            .distinct()
                            .toArray());
            GL15.glDeleteBuffers(vboBuffer);
        }
    }

}
