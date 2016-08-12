package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;

import java.io.Serializable;

public abstract class Buffer implements Serializable {

    private final TargetType targetType;
    private final int glBuffer;

    protected Buffer(final TargetType targetType, final int glBuffer) {
        this.targetType = targetType;
        this.glBuffer = glBuffer;
    }

    public void bind() {
        final int glTargetType = this.targetType.getGLTargetType();
        GL15.glBindBuffer(glTargetType, this.glBuffer);
    }

}
