package il.org.spartan.xy;

import java.io.BufferedWriter;
import java.io.IOException;

/** @author Yossi Gil
 * @since Mar 1, 2012 */
public class XYGnuPlotFeeder extends XYProcessor.Vacuous {
  private final BufferedWriter inner;

  public XYGnuPlotFeeder(final BufferedWriter inner) {
    this.inner = inner;
  }

  @Override public void done() {
    try {
      write("e\n");
    } catch (final Exception ¢) {
      ¢.printStackTrace();
      System.exit(1);
    }
  }

  @Override public void p(final double x, final double y) {
    write(x + " " + y + "\n");
  }

  @Override public void p(final double x, final double y, final double dy) {
    write(x + " " + y + " " + dy + "\n");
  }

  private void write(final String s) {
    try {
      inner.write(s);
      inner.flush();
    } catch (final IOException ¢) {
      ¢.printStackTrace();
      System.exit(1);
    }
  }
}
