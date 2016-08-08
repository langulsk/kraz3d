package com.kraz3d.opengl;

import com.google.common.base.MoreObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Program implements Serializable {

    private final int glProgram;

    private Program(final int glProgram) {
        this.glProgram = glProgram;
    }

    public static Program create() {
        final int glProgram = GL20.glCreateProgram();
        return new Program(glProgram);
    }

    public void attach(final Shader shader) {
        final int glShader = shader.getGLShader();
        GL20.glAttachShader(this.glProgram, glShader);
    }

    public void link() {
        GL20.glLinkProgram(this.glProgram);
    }

    public void delete() {
        GL20.glDeleteProgram(this.glProgram);
    }

    public void use() {
        GL20.glUseProgram(this.glProgram);
    }

    public static void disuse() {
        GL20.glUseProgram(0);
    }

    public boolean getDeleteStatus() {
        return getStatus(GL20.GL_DELETE_STATUS);
    }

    public boolean getLinkStatus() {
        return getStatus(GL20.GL_LINK_STATUS);
    }

    public boolean getValidateStatus() {
        return getStatus(GL20.GL_VALIDATE_STATUS);
    }

    private boolean getStatus(final int glStatusType) {
        try (final MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer statusBuffer = stack.mallocInt(1);
            GL20.glGetProgramiv(this.glProgram, glStatusType, statusBuffer);
            return statusBuffer.get(0) != GL11.GL_FALSE;
        }
    }

    public String getInfoLog() {
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer lengthBuffer = memoryStack.mallocInt(1);
            GL20.glGetProgramiv(this.glProgram, GL20.GL_INFO_LOG_LENGTH, lengthBuffer);
            final ByteBuffer infoLogBuffer = memoryStack.malloc(lengthBuffer.get(0));
            GL20.glGetProgramInfoLog(this.glProgram, lengthBuffer, infoLogBuffer);
            return MemoryUtil.memUTF8(infoLogBuffer, lengthBuffer.get(0));
        }

    }

    public List<Attribute> getAttributes() {
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer activeAttributesBuffer = memoryStack.mallocInt(1);
            final IntBuffer activeAttributeMaxLengthBuffer = memoryStack.mallocInt(1);
            GL20.glGetProgramiv(this.glProgram, GL20.GL_ACTIVE_ATTRIBUTES, activeAttributesBuffer);
            GL20.glGetProgramiv(this.glProgram, GL20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, activeAttributeMaxLengthBuffer);
            final IntBuffer lengthBuffer = memoryStack.mallocInt(1);
            final IntBuffer sizeBuffer = memoryStack.mallocInt(1);
            final IntBuffer typeBuffer = memoryStack.mallocInt(1);
            final ByteBuffer nameBuffer = memoryStack.malloc(activeAttributeMaxLengthBuffer.get(0));
            final List<Attribute> attributes = IntStream.range(0, activeAttributesBuffer.get(0))
                    .mapToObj(index -> {
                        GL20.glGetActiveAttrib(this.glProgram, index, lengthBuffer, sizeBuffer, typeBuffer, nameBuffer);
                        final String name = MemoryUtil.memUTF8(nameBuffer, lengthBuffer.get(0));
                        final DataType dataType = DataType.getDataType(typeBuffer.get(0));
                        final int size = sizeBuffer.get(0);
                        final int location = GL20.glGetAttribLocation(this.glProgram, nameBuffer);
                        return new Attribute(name, dataType, size, location);
                    })
                    .collect(Collectors.toList());
            return Collections.unmodifiableList(attributes);
        }
    }

    public List<Uniform> getUniforms() {
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer activeUniformBuffer = memoryStack.mallocInt(1);
            final IntBuffer activeUniformMaxLengthBuffer = memoryStack.mallocInt(1);
            GL20.glGetProgramiv(this.glProgram, GL20.GL_ACTIVE_UNIFORMS, activeUniformBuffer);
            GL20.glGetProgramiv(this.glProgram, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH, activeUniformMaxLengthBuffer);
            final IntBuffer lengthBuffer = memoryStack.mallocInt(1);
            final IntBuffer sizeBuffer = memoryStack.mallocInt(1);
            final IntBuffer typeBuffer = memoryStack.mallocInt(1);
            final ByteBuffer nameBuffer = memoryStack.malloc(activeUniformMaxLengthBuffer.get(0));
            final List<Uniform> uniforms = IntStream.range(0, activeUniformBuffer.get(0))
                    .mapToObj(index -> {
                        GL20.glGetActiveUniform(this.glProgram, index, lengthBuffer, sizeBuffer, typeBuffer, nameBuffer);
                        final String name = MemoryUtil.memUTF8(nameBuffer, lengthBuffer.get(0));
                        final DataType dataType = DataType.getDataType(typeBuffer.get(0));
                        final int size = sizeBuffer.get(0);
                        final int location = GL20.glGetUniformLocation(this.glProgram, nameBuffer);
                        return new Uniform(name, dataType, size, location);
                    })
                    .collect(Collectors.toList());
            return Collections.unmodifiableList(uniforms);
        }
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (object.getClass() != getClass()) {
            return false;
        }
        final Program other = (Program) object;
        return this.glProgram == other.glProgram;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.glProgram);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Program.class)
                .add("glProgram", this.glProgram)
                .toString();
    }

}
