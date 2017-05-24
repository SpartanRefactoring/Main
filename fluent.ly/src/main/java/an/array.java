package an;

/** We don't simply overload because it makes some compilers fail apparently.
 * @author Yossi Gil
 * @since 2017-05-20 */
public interface array {
  interface of {
    static boolean[] booleans(final boolean... ¢) {
      return ¢;
    }
    static double[] doubles(final double... ¢) {
      return ¢;
    }
    static int[] ints(final int... ¢) {
      return ¢;
    }
  }

  @SafeVarargs static <T> T[] of(final T... ¢) {
    return ¢;
  }
}
