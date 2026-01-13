
package io.andywang.billiards.geom;

public class Segment2 {
    public final Point2 a;
    public final Point2 b;

    public Segment2(Point2 a, Point2 b) {
        this.a = a;
        this.b = b;
    }

    public Vec2 direction() {
        return b.sub(a);
    }

    public Vec2 unitDir() {
        return direction().normalized();
    }
}
