package com.kraz3d.opengl;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Uniform {

    private final String name;

    private final DataType dataType;

    private final int size;

    private final int location;

//    private boolean enabled;

//    private boolean normalized;

//    private int stride;

//    private int offset;

    public Uniform(final String name, final DataType dataType, final int size, final int location) {
        this.name = Objects.requireNonNull(name);
        this.dataType = Objects.requireNonNull(dataType);
        this.size = size;
        this.location = location;
    }

    public String getName() {
        return this.name;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public int getSize() {
        return this.size;
    }

    public int getLocation() {
        return this.location;
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
        final Uniform other = (Uniform) object;
        return Objects.equals(this.name, other.name)
                && this.dataType == other.dataType
                && this.size == other.size
                && this.location == other.location;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.name,
                this.dataType,
                this.size,
                this.location);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(Uniform.class)
                .add("name", this.name)
                .add("dataType", this.dataType)
                .add("size", this.size)
                .add("location", this.location)
                .toString();
    }

}
