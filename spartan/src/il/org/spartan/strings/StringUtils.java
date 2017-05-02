package il.org.spartan.strings;

import static fluent.ly.___.*;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;

/** A bunch of string functions.
 * @author Yossi Gil */
@Antiexample @Utility public enum StringUtils {
  // No elements in this name space
  ;
  static final int MAX_FIRST = 20;
  static final int MAX_LAST = 10;

  public static double atod(@NotNull final String ¢) {
    return Double.valueOf(¢).doubleValue();
  }

  public static float atof(@NotNull final String ¢) {
    return Float.valueOf(¢).floatValue();
  }

  public static int atoi(@NotNull final String ¢) {
    return Integer.valueOf(¢).intValue();
  }

  public static long atol(@NotNull final String ¢) {
    return Long.valueOf(¢).longValue();
  }

  @NotNull public static String capitalize(@NotNull final String ¢) {
    return ¢.length() == 0 ? ¢ : (first(¢) + "").toUpperCase() + rest(¢).toLowerCase();
  }

  /** Concatenate any number of strings.
   * @param ss a variable number of strings
   * @return the concatenation of the strings in <code>ss</code> */
  @NotNull public static String cat(@NotNull final String... ss) {
    @NotNull final StringBuilder $ = new StringBuilder("");
    for (final String ¢ : ss)
      $.append(¢);
    return $ + "";
  }

  @NotNull public static String cat(@NotNull final String[]... sss) {
    @NotNull final StringBuilder $ = new StringBuilder("");
    for (final String[] ¢ : sss)
      $.append(cat(¢));
    return $ + "";
  }

  public static double delta(final double a, final double d) {
    return a == d ? 0 : signum(a) != signum(d) ? Double.NaN : 2 * Math.abs(a - d) / Math.abs(a + d);
  }

  @NotNull public static String dtoa(final double ¢) {
    return ¢ + "";
  }

  public static <T> boolean eq(@Nullable final T a, @Nullable final T b) {
    return a == null ? b == null : a.equals(b);
  }

  @NotNull public static String esc(final char ¢) {
    switch (¢) {
      case '\n':
        return "\\n";
      case '\r':
        return "\\r";
      case '\t':
        return "\\t";
      case '\f':
        return "\\f";
      case '\b':
        return "\\b";
      case '\\':
        return "\\\\";
      default:
        return ¢ + "";
    }
  }

  @NotNull public static String esc(@Nullable final String s) {
    if (s == null)
      return "(null)";
    @NotNull final StringBuilder $ = new StringBuilder(s.length());
    for (int ¢ = 0; ¢ < s.length(); ++¢)
      $.append(esc(s.charAt(¢)));
    return $ + "";
  }

  public static String expandLeadingTabs(final String s) {
    nonnull(s);
    for (String $ = s;;) {
      final String newValue = $.replaceAll("(?m)^([\t]*)\t", "$1    ");
      if ($.equals(newValue))
        return $;
      $ = newValue;
    }
  }

  @NotNull public static String fill(final int i, final char c) {
    return fill(i, c + "");
  }

  @NotNull public static String fill(final int i, final String s) {
    @NotNull final StringBuilder $ = new StringBuilder();
    for (int ¢ = 0; ¢ < i; ++¢)
      $.append(s);
    return $ + "";
  }

  public static char first(@NotNull final String ¢) {
    nonnull(¢);
    positive(¢.length());
    return ¢.charAt(0);
  }

  @NotNull public static String ftoa(final float ¢) {
    return ¢ + "";
  }

  public static boolean isDouble(@NotNull final String s) {
    try {
      Double.valueOf(s);
      return true;
    } catch (@NotNull final NumberFormatException __) {
      return false;
    }
  }

  public static boolean isFloat(@NotNull final String s) {
    try {
      Float.valueOf(s);
      return true;
    } catch (@NotNull final NumberFormatException __) {
      return false;
    }
  }

  public static boolean isInt(@NotNull final String s) {
    try {
      Integer.valueOf(s);
      return true;
    } catch (@NotNull final NumberFormatException __) {
      return false;
    }
  }

  public static boolean isLong(@NotNull final String s) {
    try {
      Long.valueOf(s);
      return true;
    } catch (@NotNull final NumberFormatException __) {
      return false;
    }
  }

  @NotNull public static String itoa(final int ¢) {
    return ¢ + "";
  }

  @NotNull public static String javaCase(@NotNull final String ¢) {
    return ¢.length() == 0 ? ¢ : (first(¢) + "").toLowerCase() + rest(¢);
  }

  public static char last(@NotNull final String ¢) {
    nonnull(¢);
    positive(¢.length());
    return ¢.charAt(¢.length() - 1);
  }

  @NotNull public static String lowCounter(final int ¢) {
    switch (¢) {
      case -1:
        return "";
      case 0:
        return "a";
      default:
        return tolow(¢);
    }
  }

  @NotNull public static String ltoa(final long ¢) {
    return ¢ + "";
  }

  /** Compute the string equivalent ordinal of a positive integer, e.g., for 1
   * return "1st", for 22, the "22nd", etc.
   * @param ¢ a non-negative integer to convert
   * @return the ordinal string representation of <code>n</code> */
  @NotNull public static String ordinal(final int ¢) {
    nonnegative(¢);
    switch (¢ % 10) {
      case 1:
        return ¢ + (¢ == 11 ? "th" : "st");
      case 2:
        return ¢ + (¢ == 12 ? "th" : "nd");
      default:
        return ¢ + "th";
    }
  }

  /** Wrap an object in parenthesis
   * @param ¢ a non-<code><b>null</b></code> object for wrapping in parenthesis
   * @return the result of <code>o.toString()</code> wrapped parenthesis */
  @NotNull public static String paren(final Object ¢) {
    return "(" + ¢ + ")";
  }

  @NotNull public static String pluralize(final int i, final String singular) {
    return pluralize(i, singular, singular + "s");
  }

  @NotNull public static String pluralize(final int i, final String singular, final String plural) {
    switch (i) {
      case 0:
        return "no " + plural;
      case 1:
        return singular + "";
      case 2:
        return "two " + plural;
      case 3:
        return "three " + plural;
      case 4:
        return "four " + plural;
      case 5:
        return "five " + plural;
      case 6:
        return "six " + plural;
      case 7:
        return "seven " + plural;
      case 8:
        return "eight " + plural;
      case 9:
        return "nine " + plural;
      default:
        return i + " " + plural;
    }
  }

  @NotNull public static String pretty(final String singular, final Collection<?> a) {
    return pretty(singular, singular + "s", a);
  }

  @NotNull public static String pretty(final String singular, final String plural, @Nullable final Collection<?> a) {
    if (a == null || a.isEmpty())
      return "";
    if (a.size() == 1)
      return "1 " + singular + ": " + a.iterator().next() + "\n";
    @NotNull String $ = a.size() + " " + plural + ":\n";
    int n = 0;
    @NotNull final Once ellipsis = new Once("\t...\n");
    for (final Object ¢ : a) {
      ++n;
      $ += n > MAX_FIRST && n <= a.size() - MAX_LAST ? ellipsis : "\t" + n + ") " + ¢ + "\n";
    }
    return $;
  }

  /** Quote an object
   * @param ¢ a non-<code><b>null</b></code> object for quoting
   * @return the result of <code>o.toString()</code> wrapped with single
   *         quotes */
  @NotNull public static String quote(final Object ¢) {
    return wrap('\'', ¢ + "");
  }

  @NotNull public static String repeat(final int i, final char c) {
    return repeat(i, c + "");
  }

  /** Repeat a string a fixed number of times
   * @param i a non-negative integer
   * @param s a string to repeat
   * @return a {@link String} containing <code>s</code> concatenated
   *         <code>n</code> times */
  @NotNull public static String repeat(final int i, final String s) {
    @NotNull final StringBuffer $ = new StringBuffer();
    for (int ¢ = 0; ¢ < i; ++¢)
      $.append(s);
    return $ + "";
  }

  /** Chop the first character of a string.
   * @param ¢ a non-<code><b>null</b></code> string of length at least one
   * @return <code>s</code> but without its first character. */
  public static String rest(@NotNull final String ¢) {
    nonnull(¢);
    positive(¢.length());
    return ¢.substring(1);
  }

  public static int signum(final double ¢) {
    return ¢ == 0 ? 0 : ¢ > 0 ? 1 : -1;
  }

  @NotNull public static String sprintf(@NotNull final String format, final Object... args) {
    return new Formatter().format(format, args) + "";
  }

  public static String sprintf(@NotNull final String[] args) {
    switch (args.length) {
      case 0:
        return "";
      case 1:
        return args[0];
      default:
        @NotNull final Object $[] = new Object[args.length - 1];
        for (int ¢ = 1; ¢ < args.length; ++¢)
          $[¢ - 1] = args[¢];
        return new Formatter().format(args[0], $) + "";
    }
  }

  /** Strip the first and last character of a string.
   * @param ¢ a non-<code><b>null</b></code> string of length at least two to
   *        strip
   * @return <code>s</code> but without its first and last character. */
  public static String strip(@NotNull final String ¢) {
    nonnull(¢);
    require(¢.length() >= 2);
    return ¢.substring(1, ¢.length() - 1);
  }

  @NotNull public static List<String> toLines(@NotNull final String s) throws IOException {
    @NotNull final List<String> $ = new ArrayList<>();
    for (@NotNull final BufferedReader br = new BufferedReader(new StringReader(s));;) {
      final String line = br.readLine();
      if (line == null)
        return $;
      $.add(line);
    }
  }

  @NotNull public static String upCounter(final int ¢) {
    switch (¢) {
      case -1:
        return "";
      case 0:
        return "A";
      default:
        return toup(¢);
    }
  }

  public static String visualize(final String ¢) {
    return esc(¢).replaceAll(" ", "\\s");
  }

  @NotNull public static String wrap(final char with, final String s) {
    return with + s + with;
  }

  @NotNull public static String wrap(final String with, final String s) {
    return with + s + with;
  }

  @NotNull private static String tolow(final int ¢) {
    return ¢ == 0 ? "" : tolow(¢ / 26) + (char) (¢ % 26 + 'a');
  }

  @NotNull private static String toup(final int ¢) {
    return ¢ == 0 ? "" : toup(¢ / 26) + (char) (¢ % 26 + 'A');
  }
}
