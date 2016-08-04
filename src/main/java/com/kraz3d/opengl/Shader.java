package com.kraz3d.opengl;

import com.google.common.base.MoreObjects;
import org.lwjgl.opengl.GL20;

import java.io.Serializable;
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
