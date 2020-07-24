package il.org.spartan.strings;

import fluent.ly.box;
import il.org.spartan.iterables.iterables.RangeIterator;
import il.org.spartan.utils.Separate;

/** @author Yossi Gil
 * @since Apr 27, 2012 */
public enum Tandem {
  ;
  public static String arrays(final double[] x, final double[] y) {
    return Separate.by(() -> new RangeIterator<>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,6g:%,6g>", box.it(x[i()]), box.it(y[i()]));
      }
    }, ";");
  }
  public static String arrays(final double[] x, final int[] y) {
    return Separate.by(() -> new RangeIterator<>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,g:%,d>", box.it(x[i()]), box.it(y[i()]));
      }
    }, ";");
  }
  public static String arrays(final int[] x, final double[] y) {
    return Separate.by(() -> new RangeIterator<>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,d:%,g>", box.it(x[i()]), box.it(y[i()]));
      }
    }, ";");
  }
  public static String arrays(final int[] x, final int[] y) {
    return Separate.by(() -> new RangeIterator<>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,d:%,d>", box.it(x[i()]), box.it(y[i()]));
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
