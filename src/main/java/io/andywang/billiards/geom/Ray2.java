
package io.andywang.billiards.geom;

public class Ray2 {
    public final Point2 p;
    public final Vec2 v; // unit direction

    public Ray2(Point2 p, Vec2 v) {
        this.p = p;
        this.v = v;
    }

    public Point2 at(double t) {
        return new Point2(p.x + t * v.x, p.y + t * v.y);
    }
}
