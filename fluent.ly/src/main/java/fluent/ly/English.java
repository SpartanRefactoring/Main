package fluent.ly;

import java.text.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import il.org.spartan.utils.*;

/** Utility class for linguistic issues. Used by GUI dialogs.
 * @author Ori Roth
 * @since 2.6 */
public interface English {
  interface Inflection {
    static Inflection stem(final String base) {
      return new Inflection() {
        @Override public String get() {
          return base;
        }
        @Override public String getEd() {
          return base + "ed";
        }
        @Override public String getIng() {
          return base + "ing";
        }
      };
    }
    String get();
    String getEd();
    String getIng();
  }

  String DOUBLE_FORMAT = "0.00";
  String SEPARATOR = ", ";
  String TRIM_SUFFIX = "...";
  int TRIM_THRESHOLD = 50;
  /** Error string, replacing null/error value. */
  String UNKNOWN = "???";

  static String indefinite(final Object ¢) {
    return is.indefinite(English.name(¢));
  }
  static boolean isAcronym(final String $) {
    return $.toUpperCase().equals($);
  }
  /** Constructs linguistic list of items: [i1, i2, i3] --> "i1, i2 and i3"
   * @param ¢ list of items
   * @return a linguistic list of the items */
  static String list(final List<String> ¢) {
    return ¢ == null || ¢.isEmpty() ? "nothing"
        : ¢.size() == 1 ? the.headOf(¢) : separate.these(¢.subList(0, ¢.size() - 1)).by(SEPARATOR) + " and " + the.lastOf(¢);
  }
  static String lowerFirstLetter(final String input) {
    return input.isEmpty() ? "genererated" + new Random().nextInt(100) : input.substring(0, 1).toLowerCase() + input.substring(1);
  }
  static String name(final Class<?> ¢) {
    return ¢.getEnclosingClass() == null ? English.selfName(¢) : English.selfName(¢) + "." + name(¢.getEnclosingClass());
  }
  static String name(final Object ¢) {
    return English.name(¢.getClass());
  }
  /** Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  static String plurales(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "es";
  }
  /** Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  static String plurales(final String s, final Int i) {
    return i == null ? UNKNOWN + " " + s + "es" : i.get() != 1 ? i + " " + s + "es" : "one " + s;
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
  static String plurals(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "s";
  }
  /** Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  static String plurals(final String s, final Int i) {
    return i == null ? UNKNOWN + " " + s + "s" : i.get() != 1 ? i + " " + s + "s" : "one " + s;
  }
  /** Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  static String plurals(final String s, final Integer i) {
    return i == null ? UNKNOWN + " " + s + "s" : i.intValue() != 1 ? i + " " + s + "s" : "one " + s;
  }
  static String pronounce(final char ¢) {
    if (Character.isUpperCase(¢))
      return pronounce(Character.toLowerCase(¢));
    switch (¢) {
      case 'a':
        return "aey";
      case 'b':
        return "bee";
      case 'c':
        return "see";
      case 'd':
        return "dee";
      case 'e':
        return "eae";
      case 'f':
        return "eff";
      case 'g':
        return "gee";
      case 'h':
        return "eitch";
      case 'i':
        return "eye";
      case 'j':
        return "jay";
      case 'k':
        return "kay";
      case 'l':
        return "ell";
      case 'm':
        return "em";
      case 'n':
        return "en";
      case 'o':
        return "oh";
      case 'p':
        return "pee";
      case 'q':
        return "queue";
      case 'r':
        return "ar";
      case 's':
        return "ess";
      case 't':
        return "tee";
      case 'u':
        return "you";
      case 'v':
        return "vee";
      case 'x':
        return "exx";
      case 'y':
        return "why";
      case 'z':
        return "zee";
      default:
        return "some character";
    }
  }
  static String repeat(final int i, final char c) {
    return String.valueOf(new char[i]).replace('\0', c);
  }
  static String selfName(final Class<?> ¢) {
    return ¢.isAnonymousClass() ? "{}"
        : ¢.isAnnotation() ? "@" + ¢.getSimpleName() : !¢.getSimpleName().isEmpty() ? ¢.getSimpleName() : ¢.getCanonicalName();
  }
  static String time(final long $) {
    return new DecimalFormat(DOUBLE_FORMAT).format($ / 1000000000.0);
  }
  /** Cut string's suffix to maximal length for every row.
   * @param s JD
   * @return cut string */
  static String trim(final String s) {
    if (s == null)
      return null;
    final String[] $ = s.split("\n");
    IntStream.range(0, $.length).forEach(λ -> $[λ] = trimAbsolute($[λ], TRIM_THRESHOLD, TRIM_SUFFIX));
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
  static String upperFirstLetter(final String input) {
    return input.isEmpty() ? "genererated" + new Random().nextInt(100) : input.substring(0, 1).toUpperCase() + input.substring(1);
  }
}
