package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;

import java.io.Serializable;
import java.util.EnumSet;

public enum UsageType implements Serializable {

    STREAM_DRAW(GL15.GL_STREAM_DRAW),
    STREAM_READ(GL15.GL_STREAM_READ),
    STREAM_COPY(GL15.GL_STREAM_COPY),
    STATIC_DRAW(GL15.GL_STATIC_DRAW),
    STATIC_READ(GL15.GL_STATIC_READ),
    STATIC_COPY(GL15.GL_STATIC_COPY),
    DYNAMIC_DRAW(GL15.GL_DYNAMIC_DRAW),
    DYNAMIC_READ(GL15.GL_DYNAMIC_READ),
    DYNAMIC_COPY(GL15.GL_DYNAMIC_COPY);

    private final int glUsageType;

    UsageType(final int glUsageType) {
        this.glUsageType = glUsageType;
    }

    int getGLUsageType() {
        return this.glUsageType;
    }

    static UsageType getErrorType(final int glUsageType) {
        return EnumSet.allOf(UsageType.class)
                .stream()
                .filter(usageType -> usageType.glUsageType == glUsageType)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
