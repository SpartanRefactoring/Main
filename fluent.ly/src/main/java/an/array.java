package an;

/** @author Yossi Gil
 * @since 2017-05-20 */
public interface array {
  static boolean[] of(final boolean... ¢) {
    return ¢;
  }
  static double[] of(final double... ¢) {
    return ¢;
  }
  static int[] of(final int... ¢) {
    return ¢;
  }
  @SafeVarargs static <T> T[] of(final T... ¢) {
    return ¢;
  }
}
