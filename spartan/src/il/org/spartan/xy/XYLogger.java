package il.org.spartan.xy;

import il.org.spartan.*;

/** @author Yossi Gil
 * @since February 21, 2012 */
public final class XYLogger extends XYProcessor.Vacuous {
  @Override public void done() {
    Log.flush();
  }

  @Override public void p(final double x, final double y) {
    Log.ln(x + "\t" + y);
  }

  @Override public void p(final double x, final double y, final double dy) {
    Log.ln(x + "\t" + y + "\t" + dy);
  }

  @Override public void p(final int x, final int y) {
    Log.ln(x + "\t" + y);
  }
}