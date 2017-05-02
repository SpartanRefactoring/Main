package il.org.spartan.strings;

import org.jetbrains.annotations.*;

import il.org.spartan.utils.*;
import il.org.spatan.iteration.Iterables.*;
import nano.ly.*;

/** @author Yossi Gil
 * @since Apr 27, 2012 */
public enum Tandem {
  ;
  @NotNull public static String arrays(@NotNull final double[] x, @NotNull final double[] y) {
    return Separate.by(() -> new RangeIterator<String>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,6g:%,6g>", box.it(x[i()]), box.it(y[i()]));
      }
    }, ";");
  }

  @NotNull public static String arrays(@NotNull final double[] x, @NotNull final int[] y) {
    return Separate.by(() -> new RangeIterator<String>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,g:%,d>", box.it(x[i()]), box.it(y[i()]));
      }
    }, ";");
  }

  @NotNull public static String arrays(@NotNull final int[] x, @NotNull final double[] y) {
    return Separate.by(() -> new RangeIterator<String>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,d:%,g>", box.it(x[i()]), box.it(y[i()]));
      }
    }, ";");
  }

  @NotNull public static String arrays(@NotNull final int[] x, @NotNull final int[] y) {
    return Separate.by(() -> new RangeIterator<String>(worst(x, y)) {
      @Override public String value() {
        return String.format("<%,d:%,d>", box.it(x[i()]), box.it(y[i()]));
      }
    }, ";");
  }

  static int worst(@NotNull final double[] a1, @NotNull final double[] a2) {
    return Math.max(a1.length, a2.length);
  }

  static int worst(@NotNull final double[] a1, @NotNull final int[] a2) {
    return Math.max(a1.length, a2.length);
  }

  static int worst(@NotNull final int[] a1, @NotNull final double[] a2) {
    return Math.max(a1.length, a2.length);
  }

  static int worst(@NotNull final int[] a1, @NotNull final int[] a2) {
    return Math.max(a1.length, a2.length);
  }

  static int worst(@NotNull final int[] a1, @NotNull final Object[] a2) {
    return Math.max(a1.length, a2.length);
  }

  static int worst(@NotNull final Object[] a1, @NotNull final Object[] a2) {
    return Math.max(a1.length, a2.length);
  }
}
