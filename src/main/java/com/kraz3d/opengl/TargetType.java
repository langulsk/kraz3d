package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import java.io.Serializable;
import java.util.EnumSet;

public enum TargetType implements Serializable {

    ARRAY_BUFFER(GL15.GL_ARRAY_BUFFER),
    COPY_READ_BUFFER(GL31.GL_COPY_READ_BUFFER),
    COPY_WRITE_BUFFER(GL31.GL_COPY_WRITE_BUFFER),
    ELEMENT_ARRAY_BUFFER(GL15.GL_ELEMENT_ARRAY_BUFFER),
    PIXEL_PACK_BUFFER(GL21.GL_PIXEL_PACK_BUFFER),
    PIXEL_UNPACK_BUFFER(GL21.GL_PIXEL_UNPACK_BUFFER),
    TEXTURE_BUFFER(GL31.GL_TEXTURE_BUFFER),
    TRANSFORM_FEEDBACK_BUFFER(GL30.GL_TRANSFORM_FEEDBACK_BUFFER),
    UNIFORM_BUFFER(GL31.GL_UNIFORM_BUFFER);

    private final int glTargetType;

    TargetType(final int glTargetType) {
        this.glTargetType = glTargetType;
    }

    int getGLTargetType() {
        return this.glTargetType;
    }

    static TargetType getTargetType(final int glTargetType) {
        return EnumSet.allOf(TargetType.class)
                .stream()
                .filter(targetType -> targetType.glTargetType == glTargetType)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
