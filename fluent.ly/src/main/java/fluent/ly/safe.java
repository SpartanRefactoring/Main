/* TODO orimarco <marcovitch.ori@gmail.com> please add a description
 *
 * @author orimarco <marcovitch.ori@gmail.com>
 *
 * @since Dec 19, 2016 */
package fluent.ly;

public enum safe {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Divide but if denominator 0 return 1
   * @param a
   * @param d
   * @return result of division, or {@code 1} is defined */
  public static double div(final double a, final double d) {
    return d == 0 ? 1 : a / d;
  }
}
