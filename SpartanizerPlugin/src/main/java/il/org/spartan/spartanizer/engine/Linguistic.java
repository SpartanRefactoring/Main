package il.org.spartan.spartanizer.engine;
import static java.text.DecimalFormat.*;
import java.text.*;
import java.text.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import static java.text.DecimalFormat.*;
import il.org.spartan.*;
import il.org.spartan.plugin.*;

/** Utility class for linguistic issues. Used by GUI dialogs.
 * @author Ori Roth
 * @since 2.6 */
public interface Linguistic {
  /**
   * Error string, replacing null/error value. 
   */
  final String UNKNOWN = "???";
  final String SEPARATOR = ", ";

  /**
   * Cut string's suffix to maximal length.
   * @param s  JD
   * @param l  JD
   * @return  cut string 
   */
  static String trim(final String s, final int l) {
    return s == null || s.length() < l ? s : s.substring(0, l);
  }
  static String time(long t) {
    try {
      return (new DecimalFormat(DOUBLE_FORMAT)).format(t / 1000000000.0);
    } catch (final ArithmeticException x) {
      monitor.log(x);
      return UNKNOWN;
    }
  }
  /**
   * Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s  string to be pluralize
   * @param i  count
   * @return  fixed string 
   */
  static String plurals(final String s, final Integer i) {
    return i == null ? UNKNOWN + " " + s + "s" : i.intValue() != 1 ? i + " " + s + "s" : "one " + s;
  }

  /**
   * Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s  string to be pluralize
   * @param i  count
   * @return  fixed string 
   */
  static String plurales(final String s, final Integer i) {
    return i == null ? UNKNOWN + " " + s + "es" : i.intValue() != 1 ? i + " " + s + "es" : "one " + s;
  }

  /**
   * Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s  string to be pluralize
   * @param i  count
   * @return  fixed string 
   */
  static String plurals(final String s, final AtomicInteger i) {
    return i == null ? UNKNOWN + " " + s + "s" : i.intValue() != 1 ? i + " " + s + "s" : "one " + s;
  }

  /**
   * Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s  string to be pluralize
   * @param i  count
   * @return  fixed string 
   */
  static String plurales(final String s, final AtomicInteger i) {
    return i == null ? UNKNOWN + " " + s + "es" : i.intValue() != 1 ? i + " " + s + "es" : "one " + s;
  }

  /**
   * Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s  string to be pluralize
   * @param i  count
   * @return  fixed string 
   */
  static String plurals(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "s";
  }

  /**
   * Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s  string to be pluralize
   * @param i  count
   * @return  fixed string 
   */
  static String plurales(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "es";
  }

  /**
   * @param ¢  something
   * @return  printable  {@link String}  for it 
   */
  static <X> String nanable(final X ¢) {
    return ¢ != null ? ¢ + "" : UNKNOWN;
  }

  /**
   * @param x  something
   * @param f  function to be conducted on x in case it is not null
   * @return  printable  {@link String}  for f(x) 
   */
  static <X> String nanable(final X x, final Function<X, ?> f) {
    return x == null ? UNKNOWN : f.apply(x) + "";
  }

  static String merge(final Object[] ¢) {
    return separate.these(¢).by(SEPARATOR);
  }

  static String merge(final Object[] os, final String separator) {
    return separate.these(os).by(separator);
  }
}
