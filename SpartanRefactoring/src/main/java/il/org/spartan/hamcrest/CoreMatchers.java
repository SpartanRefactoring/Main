package il.org.spartan.hamcrest;

import org.hamcrest.*;

/**
 * @author Yossi Gil
 * @since 2015-07-18
 */
public class CoreMatchers extends org.hamcrest.CoreMatchers {
  /**
   * A shortcut to the frequently used <code>is(new Byte(...))</code>.
   *
   * @param b
   *          JD
   * @return a matcher for the value specified by the parameter
   */
  public static Matcher<Byte> is(final byte b) {
    return is(Byte.valueOf(b));
  }
  /**
   * A shortcut to the frequently used <code>is(new Short(...))</code>.
   *
   * @param s
   *          JD
   * @return a matcher for the value specified by the parameter
   */
  public static Matcher<Short> is(final short s) {
    return is(Short.valueOf(s));
  }
  /**
   * A shortcut to the frequently used <code>is(new Integer(...))</code>.
   *
   * @param i
   *          JD
   * @return a matcher for the value specified by the parameter
   */
  public static Matcher<Integer> is(final int i) {
    return is(Integer.valueOf(i));
  }
  /**
   * A shortcut to the frequently used <code>is(new Long(...))</code>.
   *
   * @param l
   *          JD
   * @return a matcher for the value specified by the parameter
   */
  public static Matcher<Long> is(final long l) {
    return is(Long.valueOf(l));
  }
  /**
   * A shortcut to the frequently used <code>is(new Float(...))</code>.
   *
   * @param f
   *          JD
   * @return a matcher for the value specified by the parameter
   */
  public static Matcher<Float> is(final float f) {
    return is(Float.valueOf(f));
  }
  /**
   * A shortcut to the frequently used <code>is(new Double(...))</code>.
   *
   * @param d
   *          JD
   * @return a matcher for the value specified by the parameter
   */
  public static Matcher<Double> is(final double d) {
    return is(Double.valueOf(d));
  }
  /**
   * A shortcut to the frequently used <code>is(new Boolean(...))</code>.
   *
   * @param b
   *          JD
   * @return a matcher for the value specified by the parameter
   */
  public static Matcher<Boolean> is(final boolean b) {
    return is(Boolean.valueOf(b));
  }
  /**
   * A shortcut to the frequently used <code>is(new Character(...))</code>.
   *
   * @param c
   *          JD
   * @return a matcher for the value specified by the parameter
   */
  public static Matcher<Character> is(final char c) {
    return is(Character.valueOf(c));
  }
  /**
   * A vacuous wrapper around {@link org.hamcrest.CoreMatchers#is(Matcher)};
   * placed in this class to make it possible to use one <code><b>static
   * import</b></code> declaration for the current class, i.e.,
   * {@link il.org.spartan.hamcrest.CoreMatchers} which eliminates the need to
   * <code><b>static import</b></code> the original
   * {@link org.hamcrest.CoreMatchers#is(Matcher)}.
   */
  public static <T> org.hamcrest.Matcher<T> is(final T value) {
    return org.hamcrest.core.Is.<T> is(value);
  }
}
