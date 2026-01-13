
package io.andywang.billiards.geom;

import java.util.ArrayList;
import java.util.List;

public class VertexParser {
    public static List<Point2> parseVertices(String s) {
        // "x,y;x,y;..."
        String[] parts = s.trim().split(";");
        List<Point2> pts = new ArrayList<>();
        for (String p : parts) {
            String[] xy = p.trim().split(",");
            if (xy.length != 2) throw new IllegalArgumentException("Bad vertex: " + p);
            double x = Double.parseDouble(xy[0].trim());
            double y = Double.parseDouble(xy[1].trim());
            pts.add(new Point2(x, y));
        }
        return pts;
    }
}
