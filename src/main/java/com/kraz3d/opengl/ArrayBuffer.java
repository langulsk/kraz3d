package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;

public class ArrayBuffer extends Buffer {

    private ArrayBuffer(final int glBuffer) {
        super(TargetType.ARRAY_BUFFER, glBuffer);
    }

    public static void unbind() {
        GL15.glBindBuffer(TargetType.ARRAY_BUFFER.getGLTargetType(), 0);
    }

}
