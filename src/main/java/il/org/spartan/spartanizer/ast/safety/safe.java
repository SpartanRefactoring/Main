/** TODO:  orimarco <marcovitch.ori@gmail.com> please add a description 
 * @author  orimarco <marcovitch.ori@gmail.com>
 * @since Dec 19, 2016
 */
package il.org.spartan.spartanizer.ast.safety;

public enum safe {
  ;
  /** Divide but if b == 0 return 1.
   * @param a
   * @param d
   * @return */
  public static double div(final double a, final double d) {
    return d == 0 ? 1 : a / d;
  }
}

