package com.kraz3d.opengl;

import org.lwjgl.opengl.GL11;

import java.io.Serializable;

public class Error implements Serializable {

    private Error() {
    }

    public static ErrorType get() {
        final int glErrorType = GL11.glGetError();
        return ErrorType.getErrorType(glErrorType);
    }

}
