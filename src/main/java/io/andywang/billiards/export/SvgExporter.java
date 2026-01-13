
package io.andywang.billiards.export;

import io.andywang.billiards.core.ConvexPolygon;
import io.andywang.billiards.geom.Point2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SvgExporter {

    public static void export(ConvexPolygon poly, List<Point2> path, File outFile,
                              int width, int height, int pad) {
        Bounds b = Bounds.compute(poly, path);
        Transform t = Transform.fit(b, width, height, pad);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "<svg xmlns='http://www.w3.org/2000/svg' width='%d' height='%d'>\n", width, height));
        sb.append("<rect x='0' y='0' width='100%' height='100%' fill='white'/>\n");

        // Polygon outline
        sb.append("<polyline fill='none' stroke='black' stroke-width='2' points='");
        for (Point2 p : poly.vertices()) {
            double x = t.x(p.x);
            double y = t.y(p.y);
            sb.append(String.format(Locale.US, "%.3f,%.3f ", x, y));
        }
        // close
        Point2 p0 = poly.vertices().get(0);
        sb.append(String.format(Locale.US, "%.3f,%.3f", t.x(p0.x), t.y(p0.y)));
        sb.append("'/>\n");

        // Trajectory
        sb.append("<polyline fill='none' stroke='red' stroke-width='1.2' points='");
        for (Point2 p : path) {
            sb.append(String.format(Locale.US, "%.3f,%.3f ", t.x(p.x), t.y(p.y)));
        }
        sb.append("'/>\n");

        // Points
        for (int i = 0; i < path.size(); i++) {
            Point2 p = path.get(i);
            sb.append(String.format(Locale.US,
                    "<circle cx='%.3f' cy='%.3f' r='2' fill='%s'/>\n",
                    t.x(p.x), t.y(p.y),
                    (i == 0 ? "blue" : "red")
            ));
        }

        sb.append("</svg>\n");

        try (BufferedWriter w = new BufferedWriter(new FileWriter(outFile))) {
            w.write(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write SVG: " + outFile.getAbsolutePath(), e);
        }
    }

    private static class Bounds {
        double minX, maxX, minY, maxY;

        static Bounds compute(ConvexPolygon poly, List<Point2> path) {
            Bounds b = new Bounds();
            b.minX = Double.POSITIVE_INFINITY;
            b.maxX = Double.NEGATIVE_INFINITY;
            b.minY = Double.POSITIVE_INFINITY;
            b.maxY = Double.NEGATIVE_INFINITY;

            for (Point2 p : poly.vertices()) b.include(p);
            for (Point2 p : path) b.include(p);

            // Make square-ish bounds
            double dx = b.maxX - b.minX;
            double dy = b.maxY - b.minY;
            double d = Math.max(dx, dy);
            double cx = (b.minX + b.maxX) / 2.0;
            double cy = (b.minY + b.maxY) / 2.0;
            b.minX = cx - d / 2.0;
            b.maxX = cx + d / 2.0;
            b.minY = cy - d / 2.0;
            b.maxY = cy + d / 2.0;

            return b;
        }

        void include(Point2 p) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }
    }

    private static class Transform {
        final double sx, sy;
        final double ox, oy;

        Transform(double sx, double sy, double ox, double oy) {
            this.sx = sx; this.sy = sy; this.ox = ox; this.oy = oy;
        }

        double x(double xx) { return ox + sx * xx; }
        double y(double yy) { return oy + sy * (-yy); } // flip y for SVG

        static Transform fit(Bounds b, int width, int height, int pad) {
            double dx = b.maxX - b.minX;
            double dy = b.maxY - b.minY;
            double sx = (width - 2.0 * pad) / dx;
            double sy = (height - 2.0 * pad) / dy;
            double s = Math.min(sx, sy);

            // Move minX to pad; for y we flip, so use maxY
            double ox = pad - s * b.minX;
            double oy = pad + s * b.maxY;

            return new Transform(s, s, ox, oy);
        }
    }
}
