package com.kraz3d.opengl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import java.io.Serializable;
import java.util.EnumSet;

public enum PrimitiveType implements Serializable {

    POINTS(GL11.GL_POINTS),
    LINE_STRIP(GL11.GL_LINE_STRIP),
    LINE_LOOP(GL11.GL_LINE_LOOP),
    LINES(GL11.GL_LINES),
    LINE_STRIP_ADJACENCY(GL32.GL_LINE_STRIP_ADJACENCY),
    LINES_ADJACENCY(GL32.GL_LINES_ADJACENCY),
    TRIANGLE_STRIP(GL11.GL_TRIANGLE_STRIP),
    TRIANGLE_FAN(GL11.GL_TRIANGLE_FAN),
    TRIANGLES(GL11.GL_TRIANGLES),
    TRIANGLE_STRIP_ADJACENCY(GL32.GL_TRIANGLE_STRIP_ADJACENCY),
    TRIANGLES_ADJACENCY(GL32.GL_TRIANGLES_ADJACENCY);

    private final int glPrimitiveType;

    PrimitiveType(final int glPrimitiveType) {
        this.glPrimitiveType = glPrimitiveType;
    }

    public int getGLPrimitiveType() {
        return this.glPrimitiveType;
    }

    static PrimitiveType getPrimitiveType(final int glPrimitiveType) {
        return EnumSet.allOf(PrimitiveType.class)
                .stream()
                .filter(primitiveType -> primitiveType.glPrimitiveType == glPrimitiveType)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
