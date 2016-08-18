package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;

public class ElementArrayBuffer extends Buffer {

    private ElementArrayBuffer(final int glBuffer) {
        super(TargetType.ELEMENT_ARRAY_BUFFER, glBuffer);
    }

    public static void unbind() {
        GL15.glBindBuffer(TargetType.ELEMENT_ARRAY_BUFFER.getGLTargetType(), 0);
    }

}
