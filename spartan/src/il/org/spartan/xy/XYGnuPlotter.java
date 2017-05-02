package il.org.spartan.xy;

import static java.lang.String.*;
import static fluent.ly.box.*;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.utils.*;

/** @author Yossi Gil
 * @since February 13, 2012 */
public class XYGnuPlotter {
  @NotNull private static BufferedWriter getWriter(@NotNull final Process ¢) {
    return new BufferedWriter(new OutputStreamWriter(¢.getOutputStream()));
  }

  @NotNull private static String gformat(final double ¢) {
    return ¢ == (long) ¢ ? (long) ¢ + "" : String.format("%g", Double.valueOf(¢));
  }

  private static Process gnuplot() {
    try {
      return Runtime.getRuntime().exec(new String[] { "/usr/bin/gnuplot", "-persist" });
    } catch (@NotNull final IOException ¢) {
      ¢.printStackTrace();
      return null;
    }
  }

  @Nullable private final Process process;
  @NotNull private final BufferedWriter writer;
  @NotNull private final Thread stdoutRedirector;
  @NotNull private final Thread stderrRedirector;
  @NotNull private final Settings settings;

  /** Instantiate {@link XYGnuPlotter}. */
  public XYGnuPlotter() {
    this(new Settings());
  }

  /** Instantiate {@link XYGnuPlotter}.
   * @param settings */
  public XYGnuPlotter(@NotNull final Settings settings) {
    this.settings = settings;
    process = gnuplot();
    writer = getWriter(process);
    stdoutRedirector = new OutputStreamRedirector(System.out, process.getInputStream());
    stderrRedirector = new OutputStreamRedirector(System.err, process.getErrorStream());
    write(settings.before());
  }

  public void close() throws Throwable {
    process.waitFor();
    stdoutRedirector.join();
    stderrRedirector.join();
    super.finalize();
  }

  public void done() {
    write(settings.after());
    write("exit\n");
  }

  @NotNull public XYGnuPlotter feed(@NotNull final XYSeries s) {
    for (int ¢ = 0; ¢ < s.n(); ++¢)
      write(gformat(s.x[¢]) + " " + gformat(s.y[¢]) + " " + gformat(s.dy[¢]) + "\n");
    write("e\n");
    return this;
  }

  public void plot() {
    write(settings.plot());
  }

  public void write(@NotNull final String s) {
    try {
      writer.write(s);
      writer.flush();
    } catch (@NotNull final IOException ¢) {
      ¢.printStackTrace();
      System.exit(1);
    }
  }

  public static class Curve {
    private String curve;

    public Curve(final String curve) {
      this.curve = curve;
    }

    public Curve(@NotNull final String curve, @NotNull final double... ds) {
      this(curve, (Object[]) box(ds));
    }

    public Curve(@NotNull final String curve, final Object... os) {
      this.curve = format(curve, os);
    }

    @NotNull public Curve annotate(final String annotation) {
      curve += " " + annotation;
      return this;
    }

    @NotNull public Curve annotate(@NotNull final String annotation, final Object... os) {
      return annotate(format(annotation, os));
    }

    @Override public String toString() {
      return curve;
    }
  }

  public static class Settings {
    @NotNull private String before = "";
    @NotNull private String after = "";
    private final List<Curve> curves = new ArrayList<>();

    @NotNull public String after() {
      return after;
    }

    @NotNull public String before() {
      return before;
    }

    @NotNull public Settings characterize(final String statement) {
      before += statement + ";\n";
      return this;
    }

    @NotNull public Settings characterize(@NotNull final String statement, @NotNull final double... ds) {
      return characterize(statement, (Object[]) box(ds));
    }

    @NotNull public Settings characterize(@NotNull final String statement, final Object... os) {
      return characterize(format(statement, os));
    }

    @NotNull public Settings finalize(final String statement) {
      after += statement + ";\n";
      return this;
    }

    @NotNull public Settings finalize(@NotNull final String statement, @NotNull final double... ds) {
      return finalize(statement, (Object[]) box(ds));
    }

    @NotNull public Settings finalize(@NotNull final String statement, final Object... os) {
      return finalize(format(statement, os));
    }

    @NotNull public Curve newCurve(final String curve) {
      @NotNull final Curve $ = new Curve(curve);
      curves.add($);
      return $;
    }

    @NotNull public Curve newCurve(@NotNull final String curve, final Object... os) {
      return newCurve(format(curve, os));
    }

    @NotNull public Curve newCurveD(@NotNull final String curve, @NotNull final double... ds) {
      return newCurve(curve, (Object[]) box(ds));
    }

    @NotNull public String plot() {
      return "plot " + Separate.by(curves, ", ") + ";\n";
    }
  }
}
