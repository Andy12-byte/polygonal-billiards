
package io.andywang.billiards.geom;

public class Point2 {
    public final double x;
    public final double y;

    public Point2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2 add(Vec2 v) {
        return new Point2(x + v.x, y + v.y);
    }

    public Vec2 sub(Point2 p) {
        return new Vec2(x - p.x, y - p.y);
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "(%.9f, %.9f)", x, y);
    }
}
