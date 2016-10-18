package il.org.spartan.plugin;

import java.util.concurrent.atomic.*;
import java.util.function.*;

/** Utility class for linguistic issues. Used by GUI dialogs.
 * @author Ori Roth
 * @since 2.6 */
public class Linguistic {
  /** Error string, replacing null/errored value. */
  public static final String NAN = "???";

  /** Cut string's suffix to maximal length.
   * @param s JD
   * @param l JD
   * @return cut string */
  public static String trim(final String s, final int l) {
    return s == null || s.length() < l ? s : s.substring(0, l);
  }

  /** Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  public static String plurals(final String s, final Integer i) {
    return i == null ? NAN + " " + s + "s" : i.intValue() != 1 ? i + " " + s + "s" : "one " + s;
  }

  /** Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  public static String plurales(final String s, final Integer i) {
    return i == null ? NAN + " " + s + "es" : i.intValue() != 1 ? i + " " + s + "es" : "one " + s;
  }

  /** Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  public static String plurals(final String s, final AtomicInteger i) {
    return i == null ? NAN + " " + s + "s" : i.intValue() != 1 ? i + " " + s + "s" : "one " + s;
  }

  /** Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  public static String plurales(final String s, final AtomicInteger i) {
    return i == null ? NAN + " " + s + "es" : i.intValue() != 1 ? i + " " + s + "es" : "one " + s;
  }

  /** Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  public static String plurals(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "s";
  }

  /** Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  public static String plurales(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "es";
  }

  /** @param ¢ something
   * @return printable {@link String} for it */
  public static <X> String nanable(final X ¢) {
    return ¢ == null ? NAN : ¢ + "";
  }

  /** @param x something
   * @param f function to be conducted on x in case it is not null
   * @return printable {@link String} for f(x) */
  public static <X> String nanable(final X x, final Function<X, ?> f) {
    return x == null ? NAN : f.apply(x) + "";
  }
}
