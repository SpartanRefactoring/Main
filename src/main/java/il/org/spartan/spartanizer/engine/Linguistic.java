package il.org.spartan.spartanizer.engine;

import java.text.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import il.org.spartan.spartanizer.utils.*;

/** Utility class for linguistic issues. Used by GUI dialogs.
 * @author Ori Roth
 * @since 2.6 */
public interface Linguistic {
  /** Error string, replacing null/error value. */
  String UNKNOWN = "???";
  String SEPARATOR = ", ";
  String DOUBLE_FORMAT = "0.00";
  String TRIM_SUFFIX = "...";
  int TRIM_THRESHOLD = 50;

  /** Cut string's suffix to maximal length for every row.
   * @param s JD
   * @return cut string */
  static String trim(final String s) {
    final String[] $ = s.split("\n");
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = trimAbsolute($[¢], TRIM_THRESHOLD, TRIM_SUFFIX);
    return String.join("\n", $);
  }

  /** Cut string's suffix to maximal length.
   * @param s JD
   * @param l JD
   * @param x replacement suffix string
   * @return cut string */
  static String trimAbsolute(final String s, final int l, final String x) {
    assert l - x.length() >= 0;
    return s == null || s.length() <= l ? s : s.substring(0, l - x.length()) + x;
  }

  static String time(final long $) {
    try {
      return new DecimalFormat(DOUBLE_FORMAT).format($ / 1000000000.0);
    } catch (final ArithmeticException ¢) {
      monitor.log(¢);
      return UNKNOWN;
    }
  }

  /** Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  static String plurals(final String s, final Integer i) {
    return i == null ? UNKNOWN + " " + s + "s" : i.intValue() != 1 ? i + " " + s + "s" : "one " + s;
  }

  /** Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  static String plurales(final String s, final Integer i) {
    return i == null ? UNKNOWN + " " + s + "es" : i.intValue() != 1 ? i + " " + s + "es" : "one " + s;
  }

  /** Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  static String plurals(final String s, final AtomicInteger i) {
    return i == null ? UNKNOWN + " " + s + "s" : i.intValue() != 1 ? i + " " + s + "s" : "one " + s;
  }

  /** Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  static String plurales(final String s, final AtomicInteger i) {
    return i == null ? UNKNOWN + " " + s + "es" : i.intValue() != 1 ? i + " " + s + "es" : "one " + s;
  }

  /** Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  static String plurals(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "s";
  }

  /** Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  static String plurales(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "es";
  }

  /** @param ¢ something
   * @return printable {@link String} for it */
  static <X> String unknownIfNull(final X ¢) {
    return ¢ != null ? ¢ + "" : UNKNOWN;
  }

  /** @param x something
   * @param f function to be conducted on x in case it is not null
   * @return printable {@link String} for f(x) */
  static <X> String unknownIfNull(final X x, final Function<X, ?> f) {
    return x == null ? UNKNOWN : f.apply(x) + "";
  }
}
