package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;

public class TextureBuffer extends Buffer {

    private TextureBuffer(final int glBuffer) {
        super(TargetType.TEXTURE_BUFFER, glBuffer);
    }

    public static void unbind() {
        GL15.glBindBuffer(TargetType.TEXTURE_BUFFER.getGLTargetType(), 0);
    }

}
