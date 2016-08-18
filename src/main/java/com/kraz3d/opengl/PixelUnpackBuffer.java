package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;

public class PixelUnpackBuffer extends Buffer {

    private PixelUnpackBuffer(final int glBuffer) {
        super(TargetType.PIXEL_UNPACK_BUFFER, glBuffer);
    }

    public static void unbind() {
        GL15.glBindBuffer(TargetType.PIXEL_UNPACK_BUFFER.getGLTargetType(), 0);
    }

}
