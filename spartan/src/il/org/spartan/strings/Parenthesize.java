// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.strings;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;
import fluent.ly.*;

/** A utility class providing library functions that take an array or a
 * collection of objects, and return a parenthesized {@link String} of the non-
 * <code><b>null</b></code> elements in this collection. collection, separated
 * by a given {@link String} or <code><b>char</b></code>.
 * @author Yossi Gil, the Technion.
 * @since 07/08/2008 */
@Utility public enum Parenthesize {
  ;
  private static final String COMMA = ",";

  @NotNull public static <T> String angular(@NotNull final Collection<T> ¢) {
    return make("<", prune(¢), ">");
  }

  @NotNull public static <T> String angular(@NotNull final T[] ¢) {
    return make("<", prune(¢), ">");
  }

  @NotNull public static <T> String circual(@NotNull final Collection<T> ¢) {
    return make("<", prune(¢), ">");
  }

  @NotNull public static <T> String circular(@NotNull final T[] ¢) {
    return make("<", prune(¢), ">");
  }

  @NotNull public static <T> String curly(@NotNull final Collection<T> ¢) {
    return make("{", prune(¢), "}");
  }

  @NotNull public static <T> String curly(@NotNull final T[] ¢) {
    return make("{", prune(¢), "}");
  }

  @NotNull public static <T> String make(final String begin, final Collection<T> ts, final String end) {
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
  @NotNull public static <T> String make(final String begin, @Nullable final Collection<T> ts, final String between, final String end) {
    return ts == null || ts.isEmpty() ? "" : begin + Separate.by(ts, between) + end;
  }

  @NotNull public static <T> String make(final String begin, final T[] ts, final String end) {
    return make(begin, ts, COMMA, end);
  }

  @NotNull public static <T> String make(final String begin, @Nullable final T[] ts, final String between, final String end) {
    return ts == null || ts.length == 0 ? "" : begin + Separate.by(ts, between) + end;
  }

  @NotNull public static <T> String square(@NotNull final Collection<T> ¢) {
    return make("[", prune(¢), "]");
  }

  @NotNull public static <T> String square(@NotNull final T[] ¢) {
    return make("[", prune(¢), "]");
  }

  private static <T> Collection<T> prune(@NotNull final Collection<T> ¢) {
    return prune.nulls(¢);
  }

  private static <T> T[] prune(@NotNull final T[] ¢) {
    return prune.nulls(¢);
  }
}
