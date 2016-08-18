package com.kraz3d.opengl;

import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryStack;

import java.io.Serializable;
import java.nio.*;
import java.util.Collection;

public abstract class Buffer implements Serializable {

    protected final TargetType targetType;
    protected final int glBuffer;

    protected Buffer(final TargetType targetType, final int glBuffer) {
        this.targetType = targetType;
        this.glBuffer = glBuffer;
    }

    public static void delete(final Collection<? extends Buffer> buffers) {
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer bufferBuffer = memoryStack.ints(
                    buffers.stream()
                            .mapToInt(Buffer::getGLBuffer)
                            .toArray());
            GL15.glDeleteBuffers(bufferBuffer);
        }
    }

    int getGLBuffer() {
        return this.glBuffer;
    }

    public void bind() {
        final int glTargetType = this.targetType.getGLTargetType();
        GL15.glBindBuffer(glTargetType, this.glBuffer);
    }

    public void data(final long size, final UsageType usageType) {
        GL15.glBufferData(this.targetType.getGLTargetType(), size, usageType.getGLUsageType());
    }

    public void data(final ByteBuffer data, final UsageType usageType) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usageType.getGLUsageType());
    }

    public void data(final ShortBuffer data, final UsageType usageType) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usageType.getGLUsageType());
    }

    public void data(final IntBuffer data, final UsageType usageType) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usageType.getGLUsageType());
    }

    public void data(final FloatBuffer data, final UsageType usageType) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usageType.getGLUsageType());
    }

    public void data(final DoubleBuffer data, final UsageType usageType) {
        GL15.glBufferData(this.targetType.getGLTargetType(), data, usageType.getGLUsageType());
    }

}
