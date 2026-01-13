# Polygonal Billiards Simulator + Periodic Orbit Finder (Java)

A small, research-style computational geometry tool for **polygonal billiards**:
- Simulate billiard trajectories in **convex polygons** (triangles included)
- Search for **periodic orbits** (approximate recurrence of position + direction)
- Flag **near-perpendicular hits** (trajectory hits a side at ~90°)
- Export results to **CSV** (for analysis) and **SVG** (for clean visualization)

This project was built as a practical, reproducible workflow for exploratory experiments in polygonal billiards (high-volume trials + saving “interesting” orbits for inspection).

---

## Why this is research-aligned

Polygonal billiards work often starts with:
1) running many initial conditions,
2) detecting candidate periodic behavior (numerically / approximately),
3) exporting a short list of promising orbits for deeper study.

This tool supports exactly that loop:
- **randomized sampling** of initial states
- **approximate periodicity detection** via quantized state recurrence
- **reproducible runs** via a fixed RNG seed
- **artifacts** you can share quickly (CSV summary + SVG plots)

---

## Quick start

### 1) Build
Requires Java + Maven.

```bash
mvn -q -DskipTests package
