// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import il.org.spartan.streotypes.Utility;

/** A utility class, providing functions realizing lazy three-way branching,
 * depending on the sign of a given integer
 * @author Yossi Gil, the Technion.
 * @since 24/07/2008 */
@Utility
public enum On {
  ;
  public static void main(final String[] args) {
    for (final String arg : args) {
      System.out.print("Argument " + arg + " is ");
      On.sign(Integer.valueOf(arg), () -> System.out.println("negative!"), () -> System.out.println("zero!"), () -> System.out.println("positive!"));
    }
  }
  /** Select between one of three actions to carry out, depending on the sign of
   * a given integer. Each action is given as an instance of a class
   * implementing the {@link Action}<code><T></code>
   * <code><b>interface</b></code>.
   * @param selector an integer whose sign determines the returned value
   * @param onNegative what to do in case <code>selector</code> is negative
   * @param onZero what to do in case <code>selector</code> is zero
   * @param onPositive what to do in case <code>selector</code> is positive */
  public static void sign(final int selector, final Action onNegative, final Action onZero, final Action onPositive) {
    sign(selector, asFunction(onNegative), asFunction(onZero), asFunction(onPositive));
  }
  /** A lazy selection between three expressions depending on the sign of a
   * given integer. Each expression is given as an instance of a class
   * implementing the {@link Function}<code><T></code>
   * <code><b>interface</b></code>.
   * @param <T> type of values from which to select a return value
   * @param selector an integer whose sign determines the returned value
   * @param onNegative expression to evaluate and return if
   *        <code>selector</code> is negative
   * @param onZero expression to evaluate and return if <code>selector</code> is
   *        zero
   * @param onPositive expression to evaluate and return if
   *        <code>selector</code> is positive
   * @return one of <code>onNegative.__()</code>, <code>onZero.__()</code> or
   *         <code>onPositive.__()</code>, depending on the sign of
   *         <code>selector</code> */
  public static <T> T sign(final int selector, final Function<T> onNegative, final Function<T> onZero, final Function<T> onPositive) {
    return (selector == 0 ? onZero : selector < 0 ? onNegative : onPositive).__();
  }
  /** A non-lazy selection between three values depending on the sign of a given
   * integer
   * @param <T> type of values from which to select a return value
   * @param selector an integer whose sign determines the returned value
   * @param onNegative value to return if <code>selector</code> is negative
   * @param onZero value to return if <code>selector</code> is zero
   * @param onPositive value to return if <code>selector</code> is positive
   * @return one of <code>onNegative</code>, <code>onZero</code> or
   *         <code>onPositive</code>, depending on the sign of
   *         <code>selector</code> */
  public static <T> T sign(final int selector, final T onNegative, final T onZero, final T onPositive) {
    return selector == 0 ? onZero : selector < 0 ? onNegative : onPositive;
  }
  /** Select between one of three actions to carry out, depending on the sign of
   * a given integer. Each action is given as an instance of a class
   * implementing the {@link Action}<code><T></code>
   * <code><b>interface</b></code>.
   * @param selector an integer whose sign determines the returned value
   * @param onNegative what to do in case <code>selector</code> is negative
   * @param onZero what to do in case <code>selector</code> is zero
   * @param onPositive what to do in case <code>selector</code> is positive */
  public static void sign(final Integer selector, final Action onNegative, final Action onZero, final Action onPositive) {
    sign(selector.intValue(), asFunction(onNegative), asFunction(onZero), asFunction(onPositive));
  }
  private static Function<Void> asFunction(final Action ¢) {
    return () -> {
      ¢.__();
      return null;
    };
  }

  public interface Action {
    void __();
  }

  public interface Function<T> {
    T __();
  }
}
