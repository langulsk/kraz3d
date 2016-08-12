package com.kraz3d.opengl;

import com.google.common.base.MoreObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.io.Serializable;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VertexArray implements Serializable {

    private final int glVertexArray;

    private VertexArray(final int glVertexArray) {
        this.glVertexArray = glVertexArray;
    }

    public static Collection<VertexArray> generate(final int size) {
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer vertexArraysBuffer = memoryStack.mallocInt(size);
            GL30.glGenVertexArrays(vertexArraysBuffer);
            return IntStream.range(0, size)
                    .map(vertexArraysBuffer::get)
                    .mapToObj(VertexArray::new)
                    .collect(Collectors.toList());
        }
    }

    public static void delete(final Collection<VertexArray> vertexArrays) {
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer vertexArraysBuffer = memoryStack.ints(
                    vertexArrays.stream()
                            .mapToInt(VertexArray::getGLArray)
                            .toArray());
            GL30.glDeleteVertexArrays(vertexArraysBuffer);
        }
    }

    public void bind() {
        GL30.glBindVertexArray(this.glVertexArray);
    }

    public static void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void enable(final Attribute attribute) {
        GL20.glEnableVertexAttribArray(attribute.getLocation());
    }

    public void disable(final Attribute attribute) {
        GL20.glDisableVertexAttribArray(attribute.getLocation());
    }

    public static Optional<VertexArray> get() {
        try (final MemoryStack memoryStack = MemoryStack.stackPush()) {
            final IntBuffer buffer = memoryStack.mallocInt(1);
            GL11.glGetIntegerv(GL30.GL_VERTEX_ARRAY_BINDING, buffer);
            final int glVertexArray = buffer.get(0);
            return Optional.of(glVertexArray)
                    .filter(vertexArray -> vertexArray != 0)
                    .map(VertexArray::new);
        }
    }

    int getGLArray() {
        return this.glVertexArray;
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
        return this.glVertexArray == other.glVertexArray;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.glVertexArray);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(VertexArray.class)
                .add("glVertexArray", this.glVertexArray)
                .toString();
    }

}
