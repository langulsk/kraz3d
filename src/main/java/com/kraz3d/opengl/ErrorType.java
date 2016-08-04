package com.kraz3d.opengl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.Serializable;
import java.util.EnumSet;

import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;

public enum ErrorType implements Serializable {


    NO_ERROR(GL11.GL_NO_ERROR),
    INVALID_ENUM(GL11.GL_INVALID_ENUM),
    INVALID_VALUE(GL11.GL_INVALID_VALUE),
    INVALID_OPERATION(GL11.GL_INVALID_OPERATION),
    INVALID_FRAMEBUFFER_OPERATION(GL30.GL_INVALID_FRAMEBUFFER_OPERATION),
    OUT_OF_MEMORY(GL11.GL_OUT_OF_MEMORY),
    STACK_UNDERFLOW(GL11.GL_STACK_UNDERFLOW),
    STACK_OVERFLOW(GL_STACK_OVERFLOW);

    private final int glErrorType;

    ErrorType(final int glErrorType) {
        this.glErrorType = glErrorType;
    }

    int getGLErrorType() {
        return this.glErrorType;
    }

    static ErrorType getErrorType(final int glErrorType) {
        return EnumSet.allOf(ErrorType.class)
                .stream()
                .filter(errorType -> errorType.glErrorType == glErrorType)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }


}
