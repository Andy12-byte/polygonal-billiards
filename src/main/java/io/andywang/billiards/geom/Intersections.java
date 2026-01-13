
package io.andywang.billiards.geom;

public class Intersections {

    public static class RaySegmentHit {
        public final boolean hit;
        public final double t;  // ray parameter
        public final double u;  // segment parameter
        public final Point2 point;

        public RaySegmentHit(boolean hit, double t, double u, Point2 point) {
            this.hit = hit;
            this.t = t;
            this.u = u;
            this.point = point;
        }
    }

    /**
     * Intersect ray p + t*v with segment a + u*(b-a), u in [0,1].
     * Returns closest hit with t>eps.
     */
    public static RaySegmentHit raySegment(Ray2 ray, Segment2 seg, double eps) {
        Vec2 r = ray.v;
        Vec2 s = seg.b.sub(seg.a);
        Vec2 qp = seg.a.sub(ray.p);

        double rxs = r.cross(s);
        double qpxr = qp.cross(r);

        if (Math.abs(rxs) < 1e-15) {
            // Parallel (or collinear) -> treat as no reliable intersection.
            return new RaySegmentHit(false, Double.POSITIVE_INFINITY, Double.NaN, null);
        }

        double t = qp.cross(s) / rxs;
        double u = qpxr / rxs;

        if (t > eps && u >= -eps && u <= 1.0 + eps) {
            Point2 p = ray.at(t);
            return new RaySegmentHit(true, t, u, p);
        }
        return new RaySegmentHit(false, Double.POSITIVE_INFINITY, Double.NaN, null);
    }

    public static Vec2 reflectAcrossUnitNormal(Vec2 v, Vec2 nUnit) {
        // v' = v - 2*(v·n)*n
        double dn = v.dot(nUnit);
        return v.sub(nUnit.mul(2.0 * dn));
    }
}
