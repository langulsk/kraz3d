package com.kraz3d.opengl;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import java.io.Serializable;
import java.util.EnumSet;

public enum ShaderType implements Serializable {

    VERTEX_SHADER(GL20.GL_VERTEX_SHADER),
    FRAGMENT_SHADER(GL20.GL_FRAGMENT_SHADER),
    GEOMETRY_SHADER(GL32.GL_GEOMETRY_SHADER);

    private final int glShaderType;

    ShaderType(final int glShaderType) {
        this.glShaderType = glShaderType;
    }

    int getGLShaderType() {
        return this.glShaderType;
    }

    static ShaderType getShaderType(final int glShaderType) {
        return EnumSet.allOf(ShaderType.class)
                .stream()
                .filter(shaderType -> shaderType.glShaderType == glShaderType)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
