/**
 *
 */
package il.org.spartan.strings;

import il.org.spartan.utils.*;
import il.org.spatan.iteration.Iterables.*;

/** @author Yossi Gil
 * @since Apr 27, 2012 */
public enum Tandem {
  ;
  public static String arrays(final double[] x, final double[] y) {
    return Separate.by((Iterable<String>) () -> new RangeIterator<String>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,6g:%,6g>", Box.it(x[i()]), Box.it(y[i()]));
      }
    }, ";");
  }

  public static String arrays(final double[] x, final int[] y) {
    return Separate.by((Iterable<String>) () -> new RangeIterator<String>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,g:%,d>", Box.it(x[i()]), Box.it(y[i()]));
      }
    }, ";");
  }

  public static String arrays(final int[] x, final double[] y) {
    return Separate.by((Iterable<String>) () -> new RangeIterator<String>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,d:%,g>", Box.it(x[i()]), Box.it(y[i()]));
      }
    }, ";");
  }

  public static String arrays(final int[] x, final int[] y) {
    return Separate.by((Iterable<String>) () -> new RangeIterator<String>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,d:%,d>", Box.it(x[i()]), Box.it(y[i()]));
      }
    }, ";");
  }

  static int worst(final double[] a1, final double[] a2) {
    return Math.max(a1.length, a2.length);
  }

  static int worst(final double[] a1, final int[] a2) {
    return Math.max(a1.length, a2.length);
  }

  static int worst(final int[] a1, final double[] a2) {
    return Math.max(a1.length, a2.length);
  }

  static int worst(final int[] a1, final int[] a2) {
    return Math.max(a1.length, a2.length);
  }

  static int worst(final int[] a1, final Object[] a2) {
    return Math.max(a1.length, a2.length);
  }

  static int worst(final Object[] a1, final Object[] a2) {
    return Math.max(a1.length, a2.length);
  }
}
