package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;

public class TransformFeedbackBuffer extends Buffer {

    private TransformFeedbackBuffer(final int glBuffer) {
        super(TargetType.TRANSFORM_FEEDBACK_BUFFER, glBuffer);
    }

    public static void unbind() {
        GL15.glBindBuffer(TargetType.TRANSFORM_FEEDBACK_BUFFER.getGLTargetType(), 0);
    }

}
