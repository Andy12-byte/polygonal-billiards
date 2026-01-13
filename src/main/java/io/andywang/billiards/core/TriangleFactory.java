
package io.andywang.billiards.core;

import io.andywang.billiards.cli.Args;
import io.andywang.billiards.geom.Point2;

import java.util.ArrayList;
import java.util.List;

public class TriangleFactory {

    public static ConvexPolygon buildTriangle(Args a) {
        String t = a.triangle == null ? "equilateral" : a.triangle.toLowerCase();
        switch (t) {
            case "equilateral":
                return equilateral();
            case "right45":
                return rightIsosceles45();
            case "right345":
                return right345();
            case "isosceles":
                return isosceles(a.triBase, a.triHeight);
            default:
                return equilateral();
        }
    }

    private static ConvexPolygon equilateral() {
        // side length 1
        List<Point2> pts = new ArrayList<>();
        pts.add(new Point2(0, 0));
        pts.add(new Point2(1, 0));
        pts.add(new Point2(0.5, Math.sqrt(3) / 2.0));
        return ConvexPolygon.fromCCWVertices(pts);
    }

    private static ConvexPolygon rightIsosceles45() {
        // right triangle with legs 1,1
        List<Point2> pts = new ArrayList<>();
        pts.add(new Point2(0, 0));
        pts.add(new Point2(1, 0));
        pts.add(new Point2(0, 1));
        return ConvexPolygon.fromCCWVertices(pts);
    }

    private static ConvexPolygon right345() {
        // 30-60-90 style (approx): legs 1 and sqrt(3), hyp 2
        List<Point2> pts = new ArrayList<>();
        pts.add(new Point2(0, 0));
        pts.add(new Point2(2, 0));
        pts.add(new Point2(0.5, Math.sqrt(3)));
        return ConvexPolygon.fromCCWVertices(pts);
    }

    private static ConvexPolygon isosceles(double base, double height) {
        List<Point2> pts = new ArrayList<>();
        pts.add(new Point2(0, 0));
        pts.add(new Point2(base, 0));
        pts.add(new Point2(base / 2.0, height));
        return ConvexPolygon.fromCCWVertices(pts);
    }
}
