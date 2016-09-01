package com.kraz3d.opengl;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.Objects;

public class DrawCommand implements Serializable {

    private final Program program;

    private final VertexArray vertexArray;

    private final PrimitiveType primitiveType;

    private final int count;

    private final int instanceCount;

    private final int firstIndex;

    private final int baseVertex;

    private final int baseInstance;

    public DrawCommand(final Builder builder) {
        this.program = builder.program;
        this.vertexArray = builder.vertexArray;
        this.primitiveType = builder.primitiveType;
        this.count = builder.count;
        this.instanceCount = builder.instanceCount;
        this.firstIndex = builder.firstIndex;
        this.baseVertex = builder.baseVertex;
        this.baseInstance = builder.baseInstance;
    }

    public Program getProgram() {
        return this.program;
    }

    public VertexArray getVertexArray() {
        return this.vertexArray;
    }

    public PrimitiveType getPrimitiveType() {
        return this.primitiveType;
    }

    public int getCount() {
        return this.count;
    }

    public int getInstanceCount() {
        return this.instanceCount;
    }

    public int getFirstIndex() {
        return this.firstIndex;
    }

    public int getBaseVertex() {
        return this.baseVertex;
    }

    public int getBaseInstance() {
        return this.baseInstance;
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
        final DrawCommand other = (DrawCommand) object;
        return Objects.equals(this.program, other.program)
                && Objects.equals(this.vertexArray, other.vertexArray)
                && this.primitiveType == other.primitiveType
                && this.count == other.count
                && this.instanceCount == other.instanceCount
                && this.firstIndex == other.firstIndex
                && this.baseVertex == other.baseVertex
                && this.baseInstance == other.baseInstance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.program,
                this.vertexArray,
                this.primitiveType,
                this.count,
                this.instanceCount,
                this.firstIndex,
                this.baseVertex,
                this.baseInstance);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(DrawCommand.class)
                .add("program", this.program)
                .add("vertexArray", this.vertexArray)
                .add("primitiveType", this.primitiveType)
                .add("count", this.count)
                .add("instanceCount", this.instanceCount)
                .add("firstIndex", this.firstIndex)
                .add("baseVertex", this.baseVertex)
                .add("baseInstance", this.baseInstance)
                .toString();
    }

    public static class Builder {

        private Program program;

        private VertexArray vertexArray;

        private PrimitiveType primitiveType;

        private int count;

        private int instanceCount;

        private int firstIndex;

        private int baseVertex;

        private int baseInstance;

        public Builder setProgram(final Program program) {
            this.program = program;
            return this;
        }

        public Builder setVertexArray(final VertexArray vertexArray) {
            this.vertexArray = vertexArray;
            return this;
        }

        public Builder setPrimitiveType(final PrimitiveType primitiveType) {
            this.primitiveType = primitiveType;
            return this;
        }

        public Builder setCount(final int count) {
            this.count = count;
            return this;
        }

        public Builder setInstanceCount(final int instanceCount) {
            this.instanceCount = instanceCount;
            return this;
        }

        public Builder setFirstIndex(final int firstIndex) {
            this.firstIndex = firstIndex;
            return this;
        }

        public Builder setBaseVertex(final int baseVertex) {
            this.baseVertex = baseVertex;
            return this;
        }

        public Builder setBaseInstance(final int baseInstance) {
            this.baseInstance = baseInstance;
            return this;
        }

        public DrawCommand build() {
            return new DrawCommand(this);
        }

    }

}
