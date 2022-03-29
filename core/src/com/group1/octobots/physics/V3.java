package com.group1.octobots.physics;

import static com.badlogic.gdx.math.MathUtils.floor;
import static com.badlogic.gdx.math.MathUtils.isEqual;
import static java.lang.Math.sqrt;

/**
 * Created on 4/16/2017.
 */
public class V3 {

    /**
     * The x-component of this vector
     */
    public float x;

    /**
     * The y-component of this vector
     */
    public float y;

    /**
     * The z-component of this vector
     */
    public float z;

    /**
     * Already instantiated "constant" vectors for ease of use
     */
    public static final V3 X = new V3(1, 0, 0);
    public static final V3 Y = new V3(0, 1, 0);
    public static final V3 Z = new V3(0, 0, 1);
    public static final V3 NEG_X = new V3(-1, 0, 0);
    public static final V3 NEG_Y = new V3(0, -1, 0);
    public static final V3 NEG_Z = new V3(0, 0, -1);
    public static final V3 Zero = new V3(0, 0, 0);
    public static final V3 One = new V3(1, 1, 1);

    /**
     * Constructs a new vector with coordinates (0,0,0).
     */
    public V3() {
    }

    /**
     * Constructs a new vector with the given coordinates.
     */
    public V3(float x, float y, float z) {
        set(x, y, z);
    }

    /**
     * Constructs a new vector from the given vector.
     */
    public V3(V3 other) {
        set(other.x, other.y, other.z);
    }

    /**
     * Sets the given arguments as the coordinates of this vector.
     * @return The same vector, for chaining.
     */
    public V3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Sets the coordinates of this vector to the coordinates of the given vector.
     * @return The same vector, for chaining.
     */
    public V3 set(V3 other) {
        x = other.x;
        y = other.y;
        z = other.z;
        return this;
    }

    public V3 cpy() {
        return new V3(this);
    }

    public V3 add(V3 other){//BREAKPOINT CONDITION1: this == V3.X || this == V3.Y || this == V3.Z || this == V3.NEG_X || this == V3.NEG_Y || this == V3.NEG_Z
        return this.set(x + other.x, y + other.y, z + other.z);
    }

    public V3 add(float value) {
        return this.set(x + value, y + value, z + value);
    }

    public V3 subtract(V3 other) {
        return this.set(x - other.x, y - other.y, z - other.z);
    }

    public V3 scale(float f) {
        return this.set(x * f, y * f, z * f);
    }

    public V3 lerp(V3 target, float alpha) {
        x += alpha * (target.x - x);
        y += alpha * (target.y - y);
        z += alpha * (target.z - z);
        return this;
    }

    public V3 truncate() {
        return this.set(
                floor(x),
                floor(y),
                floor(z)
        );
    }

    public V3 normalize() {
        return scale(1 / mag());
    }

    public V3 truncated() {
        return new V3(
                floor(x),
                floor(y),
                floor(z)
        );
    }

    public V3 normalized() {
        return new V3(this).scale(1 / mag());
    }

    public float mag() {
        return (float) sqrt(x*x + y*y + z*z);
    }

    public V3 decimals() {
        return new V3(
                x - floor(x),
                y - floor(y),
                z - floor(z)
        );
    }

    public float max() {
        return Math.max(x, Math.max(y, z));
    }

    public float sumComponents() {
        return x + y + z;
    }



    public boolean isWhole() {
        return isEqual(x, floor(x)) &&
               isEqual(y, floor(y)) &&
               isEqual(z, floor(z));
    }

    public float distance(V3 other) {
        return other.cpy().subtract(this).mag();
    }

    public static V3 max(V3 v1, V3 v2) {
        return new V3(
                Math.max(v1.x, v2.x),
                Math.max(v1.y, v2.y),
                Math.max(v1.z, v2.z)
        );
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    public boolean equals(V3 other, float tolerance) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        return isEqual(x, other.x, tolerance) &&
                isEqual(y, other.y, tolerance) &&
                isEqual(z, other.z, tolerance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        V3 v3 = (V3) o;

        return isEqual(v3.x, x) &&
               isEqual(v3.y, y) &&
               isEqual(v3.z, z);
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }
}
