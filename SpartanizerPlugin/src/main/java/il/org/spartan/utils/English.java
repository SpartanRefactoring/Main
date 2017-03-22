package il.org.spartan.utils;

import static il.org.spartan.utils.system.*;

import static il.org.spartan.lisp.*;

import java.text.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** Utility class for linguistic issues. Used by GUI dialogs.
 * @author Ori Roth
 * @since 2.6 */
public interface English {
  /** Error string, replacing null/error value. */
  String UNKNOWN = "???";
  String SEPARATOR = ", ";
  String DOUBLE_FORMAT = "0.00";
  String TRIM_SUFFIX = "...";
  int TRIM_THRESHOLD = 50;

  @NotNull static String indefinite(@NotNull final Object ¢) {
    return indefinite(className(¢));
  }

  @NotNull static String indefinite(@NotNull final String className) {
    final String $ = namer.components(className)[0];
    final char openingLetter = first($);
    return isAcronym($) ? indefinite(pronounce(openingLetter)) : //
        (Utils.intIsIn(openingLetter, 'i', 'e', 'o', 'u', 'y') ? "an" : "a") + " " + className;
  }

  static boolean isAcronym(@NotNull final String $) {
    return $.toUpperCase().equals($);
  }

  /** Constructs linguistic list of items: [i1, i2, i3] --> "i1, i2 and i3"
   * @param ¢ list of items
   * @return a linguistic list of the items */
  @NotNull static String list(@Nullable final List<String> ¢) {
    return ¢ == null || ¢.isEmpty() ? "nothing"
        : ¢.size() == 1 ? first(¢) : separate.these(¢.subList(0, ¢.size() - 1)).by(SEPARATOR) + " and " + last(¢);
  }

  @NotNull static String upperFirstLetter(@NotNull final String input) {
    return input.isEmpty() ? "genererated" + new Random().nextInt(100) : input.substring(0, 1).toUpperCase() + input.substring(1);
  }

  @NotNull static String lowerFirstLetter(@NotNull final String input) {
    return input.isEmpty() ? "genererated" + new Random().nextInt(100) : input.substring(0, 1).toLowerCase() + input.substring(1);
  }

  /** Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  @NotNull static String plurales(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "es";
  }

  /** Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  @NotNull static String plurales(final String s, @Nullable final Int i) {
    return i == null ? UNKNOWN + " " + s + "es" : i.get() != 1 ? i + " " + s + "es" : "one " + s;
  }

  /** Get the plural form of the word if needed, by adding an 'es' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  @NotNull static String plurales(final String s, @Nullable final Integer i) {
    return i == null ? UNKNOWN + " " + s + "es" : i.intValue() != 1 ? i + " " + s + "es" : "one " + s;
  }

  /** Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  @NotNull static String plurals(final String s, final int i) {
    return i == 1 ? "one " + s : i + " " + s + "s";
  }

  /** Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  @NotNull static String plurals(final String s, @Nullable final Int i) {
    return i == null ? UNKNOWN + " " + s + "s" : i.get() != 1 ? i + " " + s + "s" : "one " + s;
  }

  /** Get the plural form of the word if needed, by adding an 's' to its end.
   * @param s string to be pluralize
   * @param i count
   * @return fixed string */
  @NotNull static String plurals(final String s, @Nullable final Integer i) {
    return i == null ? UNKNOWN + " " + s + "s" : i.intValue() != 1 ? i + " " + s + "s" : "one " + s;
  }

  @NotNull static String pronounce(final char ¢) {
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

  static String time(final long $) {
    return new DecimalFormat(DOUBLE_FORMAT).format($ / 1000000000.0);
  }

  /** Cut string's suffix to maximal length for every row.
   * @param s JD
   * @return cut string */
  static String trim(@NotNull final String s) {
    final String[] $ = s.split("\n");
    IntStream.range(0, $.length).forEach(λ -> $[λ] = trimAbsolute($[λ], TRIM_THRESHOLD, TRIM_SUFFIX));
    return String.join("\n", $);
  }

  /** Cut string's suffix to maximal length.
   * @param s JD
   * @param l JD
   * @param x replacement suffix string
   * @return cut string */
  @NotNull static String trimAbsolute(@Nullable final String s, final int l, @NotNull final String x) {
    assert l - x.length() >= 0;
    return s == null || s.length() <= l ? s : s.substring(0, l - x.length()) + x;
  }

  /** @param ¢ something
   * @return printable {@link String} for it */
  @NotNull static <X> String unknownIfNull(@Nullable final X ¢) {
    return ¢ != null ? ¢ + "" : UNKNOWN;
  }

  /** @param x something
   * @param f function to be conducted on x in case it is not null
   * @return printable {@link String} for f(x) */
  @NotNull static <X> String unknownIfNull(@Nullable final X x, @NotNull final Function<X, ?> f) {
    return x == null ? UNKNOWN : f.apply(x) + "";
  }

  interface Activity {
    @NotNull static Activity simple(@NotNull final String base) {
      return new Activity() {
        @Override @NotNull public String get() {
          return base;
        }

        @Override @NotNull public String getEd() {
          return base + "ed";
        }

        @Override @NotNull public String getIng() {
          return base + "ing";
        }
      };
    }

    @NotNull String get();

    @NotNull String getEd();

    @NotNull String getIng();
  }
}
