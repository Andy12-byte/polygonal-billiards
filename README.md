# Polygonal Billiards Simulator + Periodic Orbit Finder (Java)

This is a small, research-style computational geometry project:
- Simulate billiard trajectories on **convex polygons** (triangles included)
- Detect **periodic orbits** (return to same position + direction on same edge)
- Detect **perpendicular hits** (trajectory hits a side at ~90°)
- Export results to **CSV** and trajectories to **SVG** for clean visualization

## Quick start

### 1) Build
```bash
mvn -q -DskipTests package
```

This produces a runnable JAR:
- `target/polygonal-billiards-1.0.0-all.jar`

### 2) Run a batch search (example: equilateral triangle)
```bash
java -jar target/polygonal-billiards-1.0.0-all.jar \
  --shape triangle \
  --triangle equilateral \
  --samples 2000 \
  --maxBounces 4000 \
  --out results.csv \
  --svgOutDir out_svgs \
  --maxSvgs 15 \
  --seed 42
```

### 3) Run with custom vertices
Vertices are `x,y;x,y;...` in counter-clockwise order (convex).
```bash
java -jar target/polygonal-billiards-1.0.0-all.jar \
  --shape polygon \
  --vertices "0,0;2,0;2,1;0,1" \
  --samples 3000 \
  --maxBounces 5000 \
  --out results.csv \
  --svgOutDir out_svgs
```

### 4) Regular n-gon
```bash
java -jar target/polygonal-billiards-1.0.0-all.jar \
  --shape regular \
  --regularN 7 \
  --radius 1.0 \
  --samples 4000 \
  --maxBounces 8000 \
  --out results.csv \
  --svgOutDir out_svgs
```

## What you get

- `results.csv` includes:
  - polygon parameters
  - starting point + direction
  - detected period (if found)
  - number of perpendicular hits
  - edge hit sequence (first ~120 hits)

- `out_svgs/` contains SVG visualizations for the first `--maxSvgs` periodic orbits found.

## Notes
- This assumes the polygon is **convex**.
- Computation uses `double` precision with configurable tolerances:
  - `--tol` (position quantization)
  - `--tolAngle` (direction quantization)
  - `--perpEps` (perpendicular-hit test)

## Why it’s research-aligned
This matches typical exploratory workflows used in polygonal billiards research:
- high-volume randomized trials
- periodicity detection via approximate state recurrence
- reproducible outputs (CSV + SVG)

If you're using this to email a professor: include 1–2 SVGs + the CSV summary.
