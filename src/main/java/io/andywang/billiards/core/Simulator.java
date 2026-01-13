
package io.andywang.billiards.core;

import io.andywang.billiards.geom.*;

import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private final ConvexPolygon poly;
    private final SimulationConfig cfg;

    public Simulator(ConvexPolygon poly, SimulationConfig cfg) {
        this.poly = poly;
        this.cfg = cfg;
    }

    public SimulationResult run(Point2 start, Vec2 dirUnit) {
        Point2 p = start;
        Vec2 v = dirUnit.normalized();

        List<Point2> path = new ArrayList<>();
        path.add(p);

        List<Integer> edgeSeq = new ArrayList<>();
        int perpHits = 0;

        PeriodicityDetector det = new PeriodicityDetector(cfg.tol, cfg.tolAngle);

        // Epsilon step to avoid hitting the same edge due to floating errors
        final double eps = 1e-9;

        // We record state right AFTER a hit (edge index, position, direction leaving)
        int lastEdge = -1;

        int period = -1;
        boolean periodic = false;

        for (int bounce = 0; bounce < cfg.maxBounces; bounce++) {
            Ray2 ray = new Ray2(p, v);

            // Find nearest intersection among edges
            int hitEdge = -1;
            Intersections.RaySegmentHit best = null;

            for (int i = 0; i < poly.n(); i++) {
                Segment2 e = poly.edge(i);

                Intersections.RaySegmentHit hit = Intersections.raySegment(ray, e, eps);
                if (!hit.hit) continue;

                // Avoid immediate re-hit of the same edge due to numerical jitter
                if (i == lastEdge && hit.t < 1e-7) continue;

                if (best == null || hit.t < best.t) {
                    best = hit;
                    hitEdge = i;
                }
            }

            if (best == null) {
                // Shouldn't happen for closed convex polygon; treat as stop.
                break;
            }

            Point2 hitPoint = best.point;
            path.add(hitPoint);
            lastEdge = hitEdge;

            // Perpendicular hit? (incoming direction perpendicular to edge direction)
            Vec2 edgeDir = poly.edgeUnitDir(hitEdge);
            if (Math.abs(v.dot(edgeDir)) < cfg.perpEps) {
                perpHits++;
            }

            // Reflect across outward normal (sign doesn't matter for reflection formula)
            Vec2 n = poly.edgeOutwardNormalUnit(hitEdge);
            Vec2 v2 = Intersections.reflectAcrossUnitNormal(v, n).normalized();

            // Move a tiny distance inside to avoid stuck-on-edge
            Point2 p2 = hitPoint.add(v2.mul(1e-10));

            // Record edge sequence (capped)
            if (edgeSeq.size() < cfg.maxEdgeSeq) {
                edgeSeq.add(hitEdge);
            }

            // Observe periodicity on (state after bounce)
            Integer prev = det.observe(p2, v2, hitEdge, bounce);
            if (prev != null) {
                int candidatePeriod = bounce - prev;
                if (candidatePeriod > 0) {
                    periodic = true;
                    period = candidatePeriod;
                    // We can stop early when we see a recurrence.
                    p = p2; v = v2;
                    break;
                }
            }

            p = p2;
            v = v2;
        }

        int bounces = path.size() - 1;
        return new SimulationResult(periodic, period, bounces, perpHits, edgeSeq, path);
    }
}
