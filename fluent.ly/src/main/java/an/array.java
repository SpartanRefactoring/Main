package an;

/** @author Yossi Gil
 * @since 2017-05-20 */
public interface array {
  // We don't simply overload because it makes some compilers fail apparently.
  static boolean[] ofBooleans(final boolean... ¢) {
    return ¢;
  }
  static double[] ofDoubles(final double... ¢) {
    return ¢;
  }
  static int[] ofInts(final int... ¢) {
    return ¢;
  }
  @SafeVarargs static <T> T[] of(final T... ¢) {
    return ¢;
  }
}
