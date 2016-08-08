package com.kraz3d.opengl;

import com.google.common.base.MoreObjects;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.io.Serializable;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VertexArray implements Serializable {

    private final int glArray;

    private VertexArray(final int glArray) {
        GL30.glGenVertexArrays();
        this.glArray = glArray;
    }

    public static Collection<VertexArray> generate(final int size) {
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer arraysBuffer = memoryStack.mallocInt(size);
            GL30.glGenVertexArrays(arraysBuffer);
            return IntStream.range(0, size)
                    .map(arraysBuffer::get)
                    .mapToObj(VertexArray::new)
                    .collect(Collectors.toList());
        }
    }

    public static void delete(final Collection<VertexArray> vertexArrays) {
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer arraysBuffer = memoryStack.ints(
                    vertexArrays.stream()
                            .mapToInt(VertexArray::getGLArray)
                            .toArray());
            GL30.glDeleteVertexArrays(arraysBuffer);
        }
    }

    public void bind() {
        GL30.glBindVertexArray(this.glArray);
    }

    public static void unbind() {
        GL30.glBindVertexArray(0);
    }

    int getGLArray() {
        return this.glArray;
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
        final VertexArray other = (VertexArray) object;
        return this.glArray == other.glArray;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.glArray);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(VertexArray.class)
                .add("glArray", this.glArray)
                .toString();
    }

}
