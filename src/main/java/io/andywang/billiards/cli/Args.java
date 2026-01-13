
package io.andywang.billiards.cli;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Args {
    public boolean help = false;

    public String shape = "triangle";

    // Triangle presets
    public String triangle = "equilateral"; // equilateral | right45 | right345 | isosceles
    public double triBase = 1.0;      // used for isosceles
    public double triHeight = 0.8;    // used for isosceles

    // Polygon vertices
    public String vertices = null;

    // Regular polygon
    public int regularN = 5;
    public double radius = 1.0;

    // Search config
    public int samples = 2000;
    public int maxBounces = 5000;
    public long seed = 42L;

    // Tolerances
    public double tol = 1e-6;
    public double tolAngle = 1e-6;
    public double perpEps = 1e-6;

    // Output
    public String outCsvPath = "results.csv";
    public String svgOutDir = "out_svgs";
    public int maxSvgs = 15;

    public int maxEdgeSeq = 120;
    public int progressEvery = 250;

    public static Args parse(String[] argv) {
        Args a = new Args();
        Map<String, String> m = new HashMap<>();

        for (int i = 0; i < argv.length; i++) {
            String s = argv[i];
            if (s.equals("--help") || s.equals("-h")) {
                a.help = true;
                return a;
            }
            if (s.startsWith("--")) {
                String key = s.substring(2).toLowerCase(Locale.ROOT);
                String val = "true";
                if (i + 1 < argv.length && !argv[i + 1].startsWith("--")) {
                    val = argv[++i];
                }
                m.put(key, val);
            }
        }

        a.shape = m.getOrDefault("shape", a.shape);
        a.triangle = m.getOrDefault("triangle", a.triangle);
        a.triBase = parseDouble(m.getOrDefault("tribase", Double.toString(a.triBase)), a.triBase);
        a.triHeight = parseDouble(m.getOrDefault("triheight", Double.toString(a.triHeight)), a.triHeight);

        a.vertices = m.getOrDefault("vertices", a.vertices);

        a.regularN = parseInt(m.getOrDefault("regularn", Integer.toString(a.regularN)), a.regularN);
        a.radius = parseDouble(m.getOrDefault("radius", Double.toString(a.radius)), a.radius);

        a.samples = parseInt(m.getOrDefault("samples", Integer.toString(a.samples)), a.samples);
        a.maxBounces = parseInt(m.getOrDefault("maxbounces", Integer.toString(a.maxBounces)), a.maxBounces);
        a.seed = parseLong(m.getOrDefault("seed", Long.toString(a.seed)), a.seed);

        a.tol = parseDouble(m.getOrDefault("tol", Double.toString(a.tol)), a.tol);
        a.tolAngle = parseDouble(m.getOrDefault("tolangle", Double.toString(a.tolAngle)), a.tolAngle);
        a.perpEps = parseDouble(m.getOrDefault("perpeps", Double.toString(a.perpEps)), a.perpEps);

        a.outCsvPath = m.getOrDefault("out", a.outCsvPath);
        a.svgOutDir = m.getOrDefault("svgoutdir", a.svgOutDir);
        a.maxSvgs = parseInt(m.getOrDefault("maxsvgs", Integer.toString(a.maxSvgs)), a.maxSvgs);

        a.maxEdgeSeq = parseInt(m.getOrDefault("maxedgeseq", Integer.toString(a.maxEdgeSeq)), a.maxEdgeSeq);
        a.progressEvery = parseInt(m.getOrDefault("progressevery", Integer.toString(a.progressEvery)), a.progressEvery);

        return a;
    }

    private static int parseInt(String s, int fallback) {
        try { return Integer.parseInt(s); } catch (Exception e) { return fallback; }
    }
    private static long parseLong(String s, long fallback) {
        try { return Long.parseLong(s); } catch (Exception e) { return fallback; }
    }
    private static double parseDouble(String s, double fallback) {
        try { return Double.parseDouble(s); } catch (Exception e) { return fallback; }
    }

    public static String usage() {
        return "" +
                "Polygonal Billiards Simulator (Java)\\n" +
                "\\n" +
                "Build: mvn -q -DskipTests package\\n" +
                "Run:   java -jar target/polygonal-billiards-1.0.0-all.jar [options]\\n" +
                "\\n" +
                "Required options depend on shape:\\n" +
                "  --shape triangle | polygon | regular\\n" +
                "    triangle: --triangle equilateral|right45|right345|isosceles  [optional]\\n" +
                "             --triBase <double> --triHeight <double> (for isosceles)\\n" +
                "    polygon:  --vertices \"x,y;x,y;...\" (CCW, convex)\\n" +
                "    regular:  --regularN <int>=5 --radius <double>=1.0\\n" +
                "\\n" +
                "Search options:\\n" +
                "  --samples <int>        (default 2000)\\n" +
                "  --maxBounces <int>     (default 5000)\\n" +
                "  --seed <long>          (default 42)\\n" +
                "\\n" +
                "Tolerances:\\n" +
                "  --tol <double>         position quantization (default 1e-6)\\n" +
                "  --tolAngle <double>    direction quantization (default 1e-6)\\n" +
                "  --perpEps <double>     perpendicular hit test (default 1e-6)\\n" +
                "\\n" +
                "Outputs:\\n" +
                "  --out <path>           CSV output (default results.csv)\\n" +
                "  --svgOutDir <dir>      SVG output directory (default out_svgs)\\n" +
                "  --maxSvgs <int>        max periodic SVGs to export (default 15)\\n" +
                "  --maxEdgeSeq <int>     max hit sequence length stored in CSV (default 120)\\n" +
                "  --progressEvery <int>  progress print interval (default 250)\\n" +
                "";
    }
}
