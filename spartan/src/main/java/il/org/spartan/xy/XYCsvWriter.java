package il.org.spartan.xy;

import il.org.spartan.CSVLineWriter;

/** @author Yossi Gil
 * @since February 24, 2012 */
public class XYCsvWriter extends XYProcessor.Vacuous {
  CSVLineWriter inner;

  public XYCsvWriter(final String fileName) {
    inner = new CSVLineWriter(fileName);
  }

  @Override public void done() {
    inner.close();
  }

  @Override public void p(final double x, final double y) {
    inner.put("X", x).put("Y", y);
    inner.nl();
  }

  @Override public void p(final double x, final double y, final double dy) {
    inner.put("X", x).put("Y", y).put("DY", dy);
    inner.nl();
  }

  @Override public void p(final int x, final int y) {
    inner.put("X", x).put("Y", y);
    inner.nl();
  }
}
