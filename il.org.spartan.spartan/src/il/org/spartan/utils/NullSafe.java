package il.org.spartan.utils;

/** @author Yossi Gil
 * @since Apr 20, 2012 */
public class NullSafe {
  public static <T> boolean equals(final T t1, final T t2) {
    return t1 == t2 || t1 != null && t1.equals(t2);
  }
  /** Return the hashCode of
   * @param <T> type of parameter to receive a default value
   * @param ¢ a possibly <code><b>null</b></code> value
   * @return <code>t.hashCode()</code> if <code>t</code> is not
   *         <code><b>null</b></code>, otherwise <code>0</code> */
  public static <T> int hashCode(final T ¢) {
    return ¢ == null ? 0 : ¢.hashCode();
  }
}
