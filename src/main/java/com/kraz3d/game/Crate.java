package com.kraz3d.game;

import com.google.common.base.MoreObjects;
import com.kraz3d.opengl.Program;
import com.kraz3d.opengl.VertexArray;

import java.io.Serializable;
import java.util.Objects;

public class Crate implements Serializable {

    private final Program program;

    private final VertexArray vertexArray;

    private final int arrayBuffer;

    private Crate(final Builder builder) {
        this.program = builder.program;
        this.vertexArray = builder.vertexArray;
        this.arrayBuffer = builder.arrayBuffer;
    }

    public Program getProgram() {
        return this.program;
    }

    public VertexArray getVertexArray() {
        return this.vertexArray;
    }

    public int getArrayBuffer() {
        return this.arrayBuffer;
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
        final Crate other = (Crate) object;
        return Objects.equals(this.program, other.program)
                && Objects.equals(this.vertexArray, other.vertexArray)
                && this.arrayBuffer == other.arrayBuffer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.program,
                this.vertexArray,
                this.arrayBuffer
        );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Crate.class)
                .add("program", this.program)
                .add("vertexArray", this.vertexArray)
                .add("arrayBuffer", this.arrayBuffer)
                .toString();
    }

    public static class Builder {

        private Program program;

        private VertexArray vertexArray;

        private int arrayBuffer;

        public Builder setProgram(final Program program) {
            this.program = program;
            return this;
        }

        public Builder setVertexArray(final VertexArray vertexArray) {
            this.vertexArray = vertexArray;
            return this;
        }

        public Builder setArrayBuffer(final int arrayBuffer) {
            this.arrayBuffer = arrayBuffer;
            return this;
        }

        public Crate build() {
            return new Crate(this);
        }

    }

}
