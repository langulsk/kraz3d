package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;

public class PixelPackBuffer extends Buffer {

    private PixelPackBuffer(final int glBuffer) {
        super(TargetType.PIXEL_PACK_BUFFER, glBuffer);
    }

    public static void unbind() {
        GL15.glBindBuffer(TargetType.PIXEL_PACK_BUFFER.getGLTargetType(), 0);
    }

}
