
package io.andywang.billiards.core;

import io.andywang.billiards.geom.Point2;
import io.andywang.billiards.geom.Segment2;
import io.andywang.billiards.geom.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConvexPolygon {
    private final List<Point2> v; // CCW vertices
    private final List<Segment2> edges;

    private ConvexPolygon(List<Point2> verticesCCW) {
        this.v = Collections.unmodifiableList(new ArrayList<>(verticesCCW));
        this.edges = buildEdges(verticesCCW);
    }

    public static ConvexPolygon fromCCWVertices(List<Point2> verticesCCW) {
        if (verticesCCW == null || verticesCCW.size() < 3) {
            throw new IllegalArgumentException("Need at least 3 vertices.");
        }
        return new ConvexPolygon(verticesCCW);
    }

    private static List<Segment2> buildEdges(List<Point2> vv) {
        List<Segment2> e = new ArrayList<>();
        for (int i = 0; i < vv.size(); i++) {
            Point2 a = vv.get(i);
            Point2 b = vv.get((i + 1) % vv.size());
            e.add(new Segment2(a, b));
        }
        return Collections.unmodifiableList(e);
    }

    public int n() { return v.size(); }
    public List<Point2> vertices() { return v; }
    public List<Segment2> edges() { return edges; }

    public Segment2 edge(int i) { return edges.get(i); }

    /** Unit outward normal for edge i (CCW polygon). */
    public Vec2 edgeOutwardNormalUnit(int i) {
        Segment2 e = edge(i);
        Vec2 dir = e.unitDir();
        // For CCW polygon, outward normal is (dir.y, -dir.x) = right normal
        Vec2 n = new Vec2(dir.y, -dir.x);
        return n.normalized();
    }

    public Vec2 edgeUnitDir(int i) {
        return edge(i).unitDir();
    }
}
