package com.kraz3d.opengl;

import com.google.common.base.MoreObjects;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CopyWriteBuffer extends Buffer {

    private CopyWriteBuffer(final int glBuffer) {
        super(TargetType.COPY_WRITE_BUFFER, glBuffer);
    }

    public static Collection<CopyWriteBuffer> generate(final int size) {
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer buffer = memoryStack.mallocInt(size);
            GL15.glGenBuffers(buffer);
            return IntStream.range(0, size)
                    .map(buffer::get)
                    .mapToObj(CopyWriteBuffer::new)
                    .collect(Collectors.toList());
        }
    }

    public static void unbind() {
        GL15.glBindBuffer(TargetType.COPY_WRITE_BUFFER.getGLTargetType(), 0);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (object.getClass() != getClass()) {
            return false;
        }
        final CopyWriteBuffer other = (CopyWriteBuffer) object;
        return this.targetType == other.targetType
                && this.glBuffer == other.glBuffer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.targetType, this.glBuffer);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(CopyWriteBuffer.class)
                .add("targetType", this.targetType)
                .add("glBuffer", this.glBuffer)
                .toString();
    }

}
