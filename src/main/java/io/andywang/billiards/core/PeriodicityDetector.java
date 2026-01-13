
package io.andywang.billiards.core;

import io.andywang.billiards.geom.Point2;
import io.andywang.billiards.geom.Vec2;

import java.util.HashMap;
import java.util.Map;

public class PeriodicityDetector {
    private final double tol;
    private final double tolAngle;

    private final Map<StateKey, Integer> seen = new HashMap<>();

    public PeriodicityDetector(double tol, double tolAngle) {
        this.tol = tol;
        this.tolAngle = tolAngle;
    }

    public static long quantize(double x, double q) {
        return Math.round(x / q);
    }

    public static double angleOf(Vec2 v) {
        return Math.atan2(v.y, v.x);
    }

    public Integer observe(Point2 p, Vec2 v, int edgeIndex, int stepIndex) {
        long xb = quantize(p.x, tol);
        long yb = quantize(p.y, tol);
        long thb = quantize(angleOf(v), tolAngle);
        StateKey k = new StateKey(xb, yb, thb, edgeIndex);

        Integer prev = seen.get(k);
        if (prev == null) {
            seen.put(k, stepIndex);
        }
        return prev;
    }
}
