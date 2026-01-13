
package io.andywang.billiards.core;

import java.util.Objects;

public class StateKey {
    public final long xb;
    public final long yb;
    public final long thb;
    public final int edge;

    public StateKey(long xb, long yb, long thb, int edge) {
        this.xb = xb;
        this.yb = yb;
        this.thb = thb;
        this.edge = edge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateKey)) return false;
        StateKey stateKey = (StateKey) o;
        return xb == stateKey.xb && yb == stateKey.yb && thb == stateKey.thb && edge == stateKey.edge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xb, yb, thb, edge);
    }
}
