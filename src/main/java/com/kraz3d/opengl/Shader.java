package com.kraz3d.opengl;

import com.google.common.base.MoreObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

public class Shader implements Serializable {

    private final int glShader;

    private Shader(final int glShader) {
        this.glShader = glShader;
    }

    public static Shader create(final ShaderType shaderType) {
        final int glShaderType = shaderType.getGLShaderType();
        final int glShader = GL20.glCreateShader(glShaderType);
        return new Shader(glShader);
    }

    public void source(final String source) {
        GL20.glShaderSource(this.glShader, source);
    }

    public void compile() {
        GL20.glCompileShader(this.glShader);
    }

    public void delete() {
        GL20.glDeleteShader(this.glShader);
    }

    public boolean getCompileStatus() {
        return getStatus(GL20.GL_COMPILE_STATUS);
    }

    public boolean getDeleteStatus() {
        return getStatus(GL20.GL_DELETE_STATUS);
    }

    public boolean getValidateStatus() {
        return getStatus(GL20.GL_VALIDATE_STATUS);
    }

    private boolean getStatus(final int glStatusType) {
        try (final MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer statusBuffer = stack.mallocInt(1);
            GL20.glGetShaderiv(this.glShader, glStatusType, statusBuffer);
            return statusBuffer.get(0) != GL11.GL_FALSE;
        }
    }

    public String getInfoLog() {
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer lengthBuffer = memoryStack.mallocInt(1);
            GL20.glGetShaderiv(this.glShader, GL20.GL_INFO_LOG_LENGTH, lengthBuffer);
            final ByteBuffer infoLogBuffer = memoryStack.malloc(lengthBuffer.get(0));
            GL20.glGetShaderInfoLog(this.glShader, lengthBuffer, infoLogBuffer);
            return MemoryUtil.memUTF8(infoLogBuffer, lengthBuffer.get(0));
        }
    }

    int getGLShader() {
        return this.glShader;
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
        final Shader other = (Shader) object;
        return this.glShader == other.glShader;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.glShader);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Shader.class)
                .add("glShader", this.glShader)
                .toString();
    }

}
