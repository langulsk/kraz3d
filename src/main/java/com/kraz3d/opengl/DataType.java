package com.kraz3d.opengl;

import java.util.EnumSet;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL40.*;

public enum DataType {

    FLOAT(GL_FLOAT),
    FLOAT_VEC2(GL_FLOAT_VEC2),
    FLOAT_VEC3(GL_FLOAT_VEC3),
    FLOAT_VEC4(GL_FLOAT_VEC4),
    FLOAT_MAT2(GL_FLOAT_MAT2),
    FLOAT_MAT3(GL_FLOAT_MAT3),
    FLOAT_MAT4(GL_FLOAT_MAT4),
    FLOAT_MAT2x3(GL_FLOAT_MAT2x3),
    FLOAT_MAT2x4(GL_FLOAT_MAT2x4),
    FLOAT_MAT3x2(GL_FLOAT_MAT3x2),
    FLOAT_MAT3x4(GL_FLOAT_MAT3x4),
    FLOAT_MAT4x2(GL_FLOAT_MAT4x2),
    FLOAT_MAT4x3(GL_FLOAT_MAT4x3),
    INT(GL_INT),
    INT_VEC2(GL_INT_VEC2),
    INT_VEC3(GL_INT_VEC3),
    INT_VEC4(GL_INT_VEC4),
    UNSIGNED_INT(GL_UNSIGNED_INT),
    UNSIGNED_INT_VEC2(GL_UNSIGNED_INT_VEC2),
    UNSIGNED_INT_VEC3(GL_UNSIGNED_INT_VEC3),
    UNSIGNED_INT_VEC4(GL_UNSIGNED_INT_VEC4),
    DOUBLE(GL_DOUBLE),
    DOUBLE_VEC2(GL_DOUBLE_VEC2),
    DOUBLE_VEC3(GL_DOUBLE_VEC3),
    DOUBLE_VEC4(GL_DOUBLE_VEC4),
    DOUBLE_MAT2(GL_DOUBLE_MAT2),
    DOUBLE_MAT3(GL_DOUBLE_MAT3),
    DOUBLE_MAT4(GL_DOUBLE_MAT4),
    DOUBLE_MAT2x3(GL_DOUBLE_MAT2x3),
    DOUBLE_MAT2x4(GL_DOUBLE_MAT2x4),
    DOUBLE_MAT3x2(GL_DOUBLE_MAT3x2),
    DOUBLE_MAT3x4(GL_DOUBLE_MAT3x4),
    DOUBLE_MAT4x2(GL_DOUBLE_MAT4x2),
    DOUBLE_MAT4x3(GL_DOUBLE_MAT4x3);

    private final int glDataType;

    DataType(final int glDataType) {
        this.glDataType = glDataType;
    }

    int getGLDataType() {
        return this.glDataType;
    }

    static DataType getDataType(final int glDataType) {
        return EnumSet.allOf(DataType.class)
                .stream()
                .filter(dataType -> dataType.glDataType == glDataType)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
