package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;

import java.io.Serializable;
import java.nio.*;

public abstract class Buffer implements Serializable {

    private final TargetType targetType;
    private final int glBuffer;

    protected Buffer(final TargetType targetType, final int glBuffer) {
        this.targetType = targetType;
        this.glBuffer = glBuffer;
    }

    public void bind() {
        final int glTargetType = this.targetType.getGLTargetType();
        GL15.glBindBuffer(glTargetType, this.glBuffer);
    }

    public void data(final long size, final int usage) {
        GL15.glBufferData(this.targetType.getGLTargetType(), size, usage);
    }

    public void data(final ByteBuffer data, final int usage) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usage);
    }

    public void data(final ShortBuffer data, final int usage) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usage);
    }

    public void data(final IntBuffer data, final int usage) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usage);
    }

    public void data(final FloatBuffer data, final int usage) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usage);
    }

    public void data(final DoubleBuffer data, final int usage) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usage);
    }

    public void data(final short[] data, final int usage) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usage);
    }

    public void data(final int[] data, final int usage) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usage);
    }

    public void data(final float[] data, final int usage) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usage);
    }

    public void data(final double[] data, final int usage) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usage);
    }

}
