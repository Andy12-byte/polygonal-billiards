
package io.andywang.billiards.core;

import io.andywang.billiards.geom.Point2;
import io.andywang.billiards.geom.Vec2;

import java.util.List;
import java.util.Random;

public class Samplers {

    /**
     * Random point inside convex polygon using triangle fan (assumes convex + CCW).
     * For better numerical stability, we do area-weighted fan triangulation from v0.
     */
    public static Point2 randomPointInside(ConvexPolygon poly, Random rng) {
        List<Point2> v = poly.vertices();
        if (v.size() == 3) {
            return randomPointInTriangle(v.get(0), v.get(1), v.get(2), rng);
        }

        // Fan triangles: (v0, vi, v(i+1))
        double[] areas = new double[v.size() - 2];
        double total = 0.0;
        for (int i = 1; i < v.size() - 1; i++) {
            double a = triArea(v.get(0), v.get(i), v.get(i + 1));
            areas[i - 1] = a;
            total += a;
        }

        double r = rng.nextDouble() * total;
        int triIndex = 0;
        double acc = 0.0;
        for (int i = 0; i < areas.length; i++) {
            acc += areas[i];
            if (r <= acc) {
                triIndex = i;
                break;
            }
        }

        Point2 a = v.get(0);
        Point2 b = v.get(triIndex + 1);
        Point2 c = v.get(triIndex + 2);
        return randomPointInTriangle(a, b, c, rng);
    }

    private static double triArea(Point2 a, Point2 b, Point2 c) {
        double x1 = b.x - a.x, y1 = b.y - a.y;
        double x2 = c.x - a.x, y2 = c.y - a.y;
        return 0.5 * Math.abs(x1 * y2 - y1 * x2);
    }

    private static Point2 randomPointInTriangle(Point2 a, Point2 b, Point2 c, Random rng) {
        // Uniform barycentric sampling (Turk 1990 trick)
        double r1 = Math.sqrt(rng.nextDouble());
        double r2 = rng.nextDouble();
        double u = 1 - r1;
        double v = r1 * (1 - r2);
        double w = r1 * r2;

        return new Point2(
                u * a.x + v * b.x + w * c.x,
                u * a.y + v * b.y + w * c.y
        );
    }

    public static Vec2 randomUnitDirection(Random rng) {
        double ang = rng.nextDouble() * 2.0 * Math.PI;
        return new Vec2(Math.cos(ang), Math.sin(ang));
    }
}
