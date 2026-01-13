#!/usr/bin/env bash
set -euo pipefail

mvn -q -DskipTests package

java -jar target/polygonal-billiards-1.0.0-all.jar \
  --shape triangle \
  --triangle equilateral \
  --samples 2000 \
  --maxBounces 4000 \
  --out results.csv \
  --svgOutDir out_svgs \
  --maxSvgs 12 \
  --seed 42
