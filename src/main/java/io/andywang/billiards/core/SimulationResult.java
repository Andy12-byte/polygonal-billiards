
package io.andywang.billiards.core;

import io.andywang.billiards.geom.Point2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulationResult {
    private final boolean periodic;
    private final int period;
    private final int bounces;
    private final int perpendicularHits;
    private final List<Integer> edgeSequence; // edges hit
    private final List<Point2> pathPoints;    // including start + bounce points

    public SimulationResult(boolean periodic, int period, int bounces, int perpendicularHits,
                            List<Integer> edgeSequence, List<Point2> pathPoints) {
        this.periodic = periodic;
        this.period = period;
        this.bounces = bounces;
        this.perpendicularHits = perpendicularHits;
        this.edgeSequence = Collections.unmodifiableList(new ArrayList<>(edgeSequence));
        this.pathPoints = Collections.unmodifiableList(new ArrayList<>(pathPoints));
    }

    public boolean isPeriodic() { return periodic; }
    public int getPeriod() { return period; }
    public int getBounces() { return bounces; }
    public int getPerpendicularHits() { return perpendicularHits; }
    public List<Integer> getEdgeSequence() { return edgeSequence; }
    public List<Point2> getPathPoints() { return pathPoints; }
}
