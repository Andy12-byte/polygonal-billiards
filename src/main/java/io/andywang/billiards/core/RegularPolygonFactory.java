
package io.andywang.billiards.core;

import io.andywang.billiards.geom.Point2;

import java.util.ArrayList;
import java.util.List;

public class RegularPolygonFactory {
    public static ConvexPolygon build(int n, double radius) {
        List<Point2> pts = new ArrayList<>();
        // CCW, centered at origin
        for (int i = 0; i < n; i++) {
            double ang = 2.0 * Math.PI * i / n;
            pts.add(new Point2(radius * Math.cos(ang), radius * Math.sin(ang)));
        }
        return ConvexPolygon.fromCCWVertices(pts);
    }
}
