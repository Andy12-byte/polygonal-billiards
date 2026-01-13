
package io.andywang.billiards.export;

import io.andywang.billiards.core.ConvexPolygon;
import io.andywang.billiards.core.SimulationResult;
import io.andywang.billiards.geom.Point2;
import io.andywang.billiards.geom.Vec2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CsvWriter implements AutoCloseable {
    private final BufferedWriter w;

    public CsvWriter(String path) {
        try {
            this.w = new BufferedWriter(new FileWriter(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to open CSV output: " + path, e);
        }
    }

    public void writeHeader() {
        try {
            w.write("shape,vertex_count,vertices,start_x,start_y,dir_x,dir_y,periodic,period,bounces,perp_hits,edge_seq\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeRow(ConvexPolygon poly, Point2 p0, Vec2 v0, SimulationResult res) {
        try {
            String vertices = poly.vertices().stream()
                    .map(p -> String.format(Locale.US, "%.8f:%.8f", p.x, p.y))
                    .collect(Collectors.joining("|"));

            String edgeSeq = joinInts(res.getEdgeSequence());

            w.write(String.format(Locale.US,
                    "polygon,%d,\"%s\",%.10f,%.10f,%.10f,%.10f,%s,%d,%d,%d,\"%s\"\n",
                    poly.n(),
                    vertices,
                    p0.x, p0.y,
                    v0.x, v0.y,
                    res.isPeriodic() ? "true" : "false",
                    res.isPeriodic() ? res.getPeriod() : -1,
                    res.getBounces(),
                    res.getPerpendicularHits(),
                    edgeSeq
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String joinInts(List<Integer> xs) {
        if (xs == null || xs.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < xs.size(); i++) {
            if (i > 0) sb.append("-");
            sb.append(xs.get(i));
        }
        return sb.toString();
    }

    @Override
    public void close() {
        try {
            w.flush();
            w.close();
        } catch (IOException e) {
            // ignore
        }
    }
}
