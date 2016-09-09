package com.kraz3d.opengl;

public class DrawElementsCommand implements DrawCommand {

    private final Program program;

    private final VertexArray vertexArray;

    private final PrimitiveType primitiveType;

    private final int count;

    private final long byteOffset;

    private DrawElementsCommand(final Builder builder) {
        this.program = builder.program;
        this.vertexArray = builder.vertexArray;
        this.primitiveType = builder.primitiveType;
        this.count = builder.count;
        this.byteOffset = builder.byteOffset;
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

    public long getByteOffset() {
        return this.byteOffset;
    }

    public static class Builder {

        private Program program;

        private VertexArray vertexArray;

        private PrimitiveType primitiveType;

        private int count;

        private long byteOffset;

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

        public Builder setByteOffset(final long byteOffset) {
            this.byteOffset = byteOffset;
            return this;
        }

        public DrawElementsCommand build() {
            return new DrawElementsCommand(this);
        }

    }

}
