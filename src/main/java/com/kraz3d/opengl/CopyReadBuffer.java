package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;

public class CopyReadBuffer extends Buffer {

    private CopyReadBuffer(final int glBuffer) {
        super(TargetType.COPY_READ_BUFFER, glBuffer);
    }

    public static void unbind() {
        GL15.glBindBuffer(TargetType.COPY_WRITE_BUFFER.getGLTargetType(), 0);
    }

}
