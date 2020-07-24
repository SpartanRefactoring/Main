// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.strings;

import java.util.Collection;

import fluent.ly.prune;
import il.org.spartan.streotypes.Utility;
import il.org.spartan.utils.Separate;

/** A utility class providing library functions that take an array or a
 * collection of objects, and return a parenthesized {@link String} of the non-
 * <code><b>null</b></code> elements in this collection. collection, separated
 * by a given {@link String} or <code><b>char</b></code>.
 * @author Yossi Gil, the Technion.
 * @since 07/08/2008 */
@Utility
public enum Parenthesize {
  ;
  private static final String COMMA = ",";

  public static <T> String angular(final Collection<T> ¢) {
    return make("<", prune(¢), ">");
  }
  public static <T> String angular(final T[] ¢) {
    return make("<", prune(¢), ">");
  }
  public static <T> String circual(final Collection<T> ¢) {
    return make("<", prune(¢), ">");
  }
  public static <T> String circular(final T[] ¢) {
    return make("<", prune(¢), ">");
  }
  public static <T> String curly(final Collection<T> ¢) {
    return make("{", prune(¢), "}");
  }
  public static <T> String curly(final T[] ¢) {
    return make("{", prune(¢), "}");
  }
  public static <T> String make(final String begin, final Collection<T> ts, final String end) {
    return make(begin, ts, COMMA, end);
  }
  /** @param <T> type of items in the collection.
   * @param begin the opening parenthesis.
   * @param ts the actual items in the list, method <code>toString()</code> is
   *        used to compute obtain each item string representation.
   * @param between a string so separate these items
   * @param end the closing parenthesis
   * @return the string equivalent of the <code>ts</code> parameter in the
   *         following structure:
   *         <p>
   *         <code>begin</code> <code>t</code><sub>1</sub> <code>between</code>
   *         <code>t</code><sub>2</sub> <code>between</code> ... <code>t</code>
   *         <sub>n</sub> <code>end</code>
   *         <p>
   *         where
   *         <p>
   *         <code>t</code><sub>1</sub> <code>t</code><sub>2</sub> ...
   *         <code>t</code><sub>n</sub>
   *         <p>
   *         are the non-<code><b>null</b></code> elements of the
   *         <code>ts</code> parameter. If however there are no on-
   *         <code><b>null</b></code> elements in <code>ts</code> or
   *         <code>ts</code> is <code>null</code>, then the empty string is
   *         returned. */
  public static <T> String make(final String begin, final Collection<T> ts, final String between, final String end) {
    return ts == null || ts.isEmpty() ? "" : begin + Separate.by(ts, between) + end;
  }
  public static <T> String make(final String begin, final T[] ts, final String end) {
    return make(begin, ts, COMMA, end);
  }
  public static <T> String make(final String begin, final T[] ts, final String between, final String end) {
    return ts == null || ts.length == 0 ? "" : begin + Separate.by(ts, between) + end;
  }
  public static <T> String square(final Collection<T> ¢) {
    return make("[", prune(¢), "]");
  }
  public static <T> String square(final T[] ¢) {
    return make("[", prune(¢), "]");
  }
  private static <T> Collection<T> prune(final Collection<T> ¢) {
    return prune.nils(¢);
  }
  private static <T> T[] prune(final T[] ¢) {
    return prune.nulls(¢);
  }
}
