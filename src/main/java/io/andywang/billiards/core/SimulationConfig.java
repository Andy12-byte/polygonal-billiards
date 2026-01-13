
package io.andywang.billiards.core;

public class SimulationConfig {
    public final int maxBounces;
    public final double tol;
    public final double tolAngle;
    public final double perpEps;
    public final int maxEdgeSeq;

    public SimulationConfig(int maxBounces, double tol, double tolAngle, double perpEps, int maxEdgeSeq) {
        this.maxBounces = maxBounces;
        this.tol = tol;
        this.tolAngle = tolAngle;
        this.perpEps = perpEps;
        this.maxEdgeSeq = maxEdgeSeq;
    }
}
