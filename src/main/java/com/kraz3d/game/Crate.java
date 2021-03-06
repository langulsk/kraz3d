package com.kraz3d.game;

import com.google.common.base.MoreObjects;
import com.kraz3d.opengl.ArrayBuffer;
import com.kraz3d.opengl.DrawElementsCommand;
import com.kraz3d.opengl.ElementArrayBuffer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class Crate implements Serializable {

    private final Collection<DrawElementsCommand> drawElementsCommand;

    private final ArrayBuffer arrayBuffer;

    private final ElementArrayBuffer elementArrayBuffer;

    private Crate(final Builder builder) {
        this.drawElementsCommand = Collections.unmodifiableCollection(new ArrayList<>(builder.drawElementsCommands));
        this.arrayBuffer = builder.arrayBuffer;
        this.elementArrayBuffer = builder.elementArrayBuffer;
    }

    public Collection<DrawElementsCommand> getDrawElementsCommand() {
        return this.drawElementsCommand;
    }

    public ArrayBuffer getArrayBuffer() {
        return this.arrayBuffer;
    }

    public ElementArrayBuffer getElementArrayBuffer() {
        return this.elementArrayBuffer;
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
        //TODO
        return this.arrayBuffer == other.arrayBuffer
                && this.elementArrayBuffer == other.elementArrayBuffer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                //TODO
                this.arrayBuffer,
                this.elementArrayBuffer
        );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Crate.class)
                //TODO
                .add("arrayBuffer", this.arrayBuffer)
                .add("elementArrayBuffer", this.elementArrayBuffer)
                .toString();
    }

    public static class Builder {

        private Collection<DrawElementsCommand> drawElementsCommands;

        private ArrayBuffer arrayBuffer;

        private ElementArrayBuffer elementArrayBuffer;

        public Builder setDrawElementsCommands(final Collection<DrawElementsCommand> drawElementsCommands) {
            this.drawElementsCommands = drawElementsCommands;
            return this;
        }

        public Builder setArrayBuffer(final ArrayBuffer arrayBuffer) {
            this.arrayBuffer = arrayBuffer;
            return this;
        }

        public Builder setElementArrayBuffer(final ElementArrayBuffer elementArrayBuffer) {
            this.elementArrayBuffer = elementArrayBuffer;
            return this;
        }

        public Crate build() {
            return new Crate(this);
        }

    }

}
