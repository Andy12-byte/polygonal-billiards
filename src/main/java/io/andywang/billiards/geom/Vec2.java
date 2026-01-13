
package io.andywang.billiards.geom;

public class Vec2 {
    public final double x;
    public final double y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double dot(Vec2 o) {
        return x * o.x + y * o.y;
    }

    public double cross(Vec2 o) {
        return x * o.y - y * o.x;
    }

    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    public Vec2 mul(double s) {
        return new Vec2(x * s, y * s);
    }

    public Vec2 add(Vec2 o) {
        return new Vec2(x + o.x, y + o.y);
    }

    public Vec2 sub(Vec2 o) {
        return new Vec2(x - o.x, y - o.y);
    }

    public Vec2 normalized() {
        double n = norm();
        if (n == 0) return new Vec2(0, 0);
        return new Vec2(x / n, y / n);
    }

    public Vec2 perpLeft() {
        return new Vec2(-y, x);
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "<%.9f, %.9f>", x, y);
    }
}
