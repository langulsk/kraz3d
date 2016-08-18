package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;

public class UniformBuffer extends Buffer {

    private UniformBuffer(final int glBuffer) {
        super(TargetType.UNIFORM_BUFFER, glBuffer);
    }

    public static void unbind() {
        GL15.glBindBuffer(TargetType.UNIFORM_BUFFER.getGLTargetType(), 0);
    }

}
