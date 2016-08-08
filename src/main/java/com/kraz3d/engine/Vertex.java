package com.kraz3d.engine;

import com.google.common.base.MoreObjects;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Objects;
import java.util.stream.IntStream;

public class Vertex implements Serializable {

    private final Vector4f position;

    private final Vector4f color;

    private final Vector2f texture;

    public Vertex(final Vector4f position, final Vector4f color, final Vector2f texture) {
        this.position = position;
        this.color = color;
        this.texture = texture;
    }

    public Vector4f getPosition() {
        return position;
    }

    public Vector4f getColor() {
        return color;
    }

    public Vector2f getTexture() {
        return texture;
    }

    public static int getPositionBytes() {
        return Float.BYTES * 4;
    }

    public static int getColorBytes() {
        return Float.BYTES * 4;
    }

    public static int getTextureBytes() {
        return Float.BYTES * 2;
    }

    public ByteBuffer get(final int index, final ByteBuffer buffer) {
        this.position.get(index, buffer);
        this.color.get(index + 4 * 4, buffer);
        this.texture.get(index + 4 * 4 + 4 * 4, buffer);
        return buffer;
    }

    public FloatBuffer get(final int index, final FloatBuffer buffer) {
        this.position.get(index, buffer);
        this.color.get(index + 4, buffer);
        this.texture.get(index + 4 + 4, buffer);
        return buffer;
    }

    public static int getBytes() {
        return IntStream.of(
                getPositionBytes(),
                getColorBytes(),
                getTextureBytes()
        ).sum();
    }

    public static int getStride() {
        return Float.BYTES * 4 + Float.BYTES * 4 + Float.BYTES * 2;
    }

    public static int getPositionPointer() {
        return 0;
    }

    public static int getColorPointer() {
        return Float.BYTES * 4;
    }

    public static int getTexturePointer() {
        return Float.BYTES * 4 + Float.BYTES * 4;
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
        final Vertex other = (Vertex) object;
        return Objects.equals(this.position, other.position)
                && Objects.equals(this.color, other.color)
                && Objects.equals(this.texture, other.texture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.position,
                this.color,
                this.texture);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Vertex.class)
                .add("position", this.position)
                .add("color", this.color)
                .add("texture", this.texture)
                .toString();
    }

}
