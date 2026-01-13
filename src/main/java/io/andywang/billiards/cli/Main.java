
package io.andywang.billiards.cli;

import io.andywang.billiards.core.*;
import io.andywang.billiards.export.CsvWriter;
import io.andywang.billiards.export.SvgExporter;
import io.andywang.billiards.geom.*;

import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Args a = Args.parse(args);

        if (a.help) {
            System.out.println(Args.usage());
            return;
        }

        Random rng = new Random(a.seed);

        ConvexPolygon poly = buildPolygon(a);
        if (poly == null) {
            System.err.println("Error: could not build polygon. Use --shape triangle|polygon|regular and provide required args.");
            System.err.println(Args.usage());
            System.exit(2);
        }

        File svgDir = a.svgOutDir != null ? new File(a.svgOutDir) : null;
        if (svgDir != null && !svgDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            svgDir.mkdirs();
        }

        CsvWriter csv = new CsvWriter(a.outCsvPath);
        csv.writeHeader();

        int exported = 0;
        int periodicFound = 0;

        for (int i = 0; i < a.samples; i++) {
            // Sample random initial condition (point inside + direction)
            Point2 p0 = Samplers.randomPointInside(poly, rng);
            Vec2 v0 = Samplers.randomUnitDirection(rng);

            SimulationConfig cfg = new SimulationConfig(a.maxBounces, a.tol, a.tolAngle, a.perpEps, a.maxEdgeSeq);
            Simulator sim = new Simulator(poly, cfg);

            SimulationResult res = sim.run(p0, v0);

            if (res.isPeriodic()) {
                periodicFound++;
                if (svgDir != null && exported < a.maxSvgs) {
                    String name = String.format(Locale.US, "periodic_%04d_period_%d.svg", periodicFound, res.getPeriod());
                    File out = new File(svgDir, name);
                    SvgExporter.export(poly, res.getPathPoints(), out, 900, 900, 20);
                    exported++;
                }
            }

            csv.writeRow(poly, p0, v0, res);

            if ((i + 1) % Math.max(1, a.progressEvery) == 0) {
                System.out.printf(Locale.US,
                        "Progress: %d/%d | periodic=%d | exportedSVG=%d%n",
                        (i + 1), a.samples, periodicFound, exported);
            }
        }

        csv.close();
        System.out.println("Done.");
        System.out.println("CSV: " + a.outCsvPath);
        if (svgDir != null) System.out.println("SVG dir: " + svgDir.getAbsolutePath());
    }

    private static ConvexPolygon buildPolygon(Args a) {
        switch (a.shape) {
            case "triangle":
                return TriangleFactory.buildTriangle(a);
            case "polygon":
                if (a.vertices == null || a.vertices.trim().isEmpty()) return null;
                List<Point2> pts = VertexParser.parseVertices(a.vertices);
                return ConvexPolygon.fromCCWVertices(pts);
            case "regular":
                if (a.regularN < 3) return null;
                return RegularPolygonFactory.build(a.regularN, a.radius);
            default:
                return null;
        }
    }
}
