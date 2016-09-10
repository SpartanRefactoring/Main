package il.org.spartan.strings;

import static il.org.spartan.utils.___.*;

import java.io.*;
import java.util.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;

/** A bunch of string functions.
 * @author Yossi Gil */
@Antiexample @Utility public enum StringUtils {
  // No elements in this name space
  ;
  static final int MAX_FIRST = 20;
  static final int MAX_LAST = 10;

  public static double atod(final String s) {
    return new Double(s).doubleValue();
  }

  public static float atof(final String s) {
    return new Float(s).floatValue();
  }

  public static int atoi(final String s) {
    return new Integer(s).intValue();
  }

  public static long atol(final String s) {
    return new Long(s).longValue();
  }

  public static String capitalize(final String s) {
    return s.length() == 0 ? s : ("" + first(s)).toUpperCase() + rest(s).toLowerCase();
  }

  /** Concatenate any number of strings.
   * @param ss a variable number of strings
   * @return the concatenation of the strings in <code>ss</code> */
  public static String cat(final String... ss) {
    final StringBuilder $ = new StringBuilder("");
    for (final String s : ss)
      $.append(s);
    return $.toString();
  }

  public static String cat(final String[]... ss) {
    final StringBuilder $ = new StringBuilder("");
    for (final String[] s : ss)
      $.append(cat(s));
    return $.toString();
  }

  public static double delta(final double a, final double b) {
    return a == b ? 0 : signum(a) != signum(b) ? Double.NaN : 2 * Math.abs(a - b) / Math.abs(a + b);
  }

  public static String dtoa(final double d) {
    return "" + d;
  }

  public static <T> boolean eq(final T a, final T b) {
    return a == null ? b == null : a.equals(b);
  }

  public static String esc(final char c) {
    switch (c) {
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
        return "" + c;
    }
  }

  public static String esc(final String s) {
    if (s == null)
      return "(null)";
    final StringBuilder $ = new StringBuilder(s.length());
    for (int i = 0; i < s.length(); i++)
      $.append(esc(s.charAt(i)));
    return $.toString();
  }

  public static String expandLeadingTabs(final String s) {
    nonnull(s);
    String $ = s;
    for (;;) {
      final String newValue = $.replaceAll("(?m)^([\t]*)\t", "$1    ");
      if ($.equals(newValue))
        return $;
      $ = newValue;
    }
  }

  public static String fill(final int n, final char c) {
    return fill(n, "" + c);
  }

  public static String fill(final int n, final String s) {
    final StringBuilder $ = new StringBuilder();
    for (int i = 0; i < n; ++i)
      $.append(s);
    return $.toString();
  }

  public static char first(final String s) {
    nonnull(s);
    positive(s.length());
    return s.charAt(0);
  }

  public static String ftoa(final float f) {
    return "" + f;
  }

  public static boolean isDouble(final String s) {
    try {
      @SuppressWarnings("unused") final Object __ = new Double(s);
      return true;
    } catch (final NumberFormatException __) {
      return false;
    }
  }

  public static boolean isFloat(final String s) {
    try {
      @SuppressWarnings("unused") final Object __ = new Float(s);
      return true;
    } catch (final NumberFormatException __) {
      return false;
    }
  }

  public static boolean isInt(final String s) {
    try {
      @SuppressWarnings("unused") final Object __ = new Integer(s);
      return true;
    } catch (final NumberFormatException __) {
      return false;
    }
  }

  public static boolean isLong(final String s) {
    try {
      @SuppressWarnings("unused") final Object __ = new Long(s);
      return true;
    } catch (final NumberFormatException __) {
      return false;
    }
  }

  public static String itoa(final int i) {
    return "" + i;
  }

  public static String javaCase(final String s) {
    return s.length() == 0 ? s : ("" + first(s)).toLowerCase() + rest(s);
  }

  public static char last(final String s) {
    nonnull(s);
    positive(s.length());
    return s.charAt(s.length() - 1);
  }

  public static String lowCounter(final int n) {
    switch (n) {
      case -1:
        return "";
      case 0:
        return "a";
      default:
        return tolow(n);
    }
  }

  public static String ltoa(final long l) {
    return "" + l;
  }

  /** Compute the string equivalent ordinal of a positive integer, e.g., for 1
   * return "1st", for 22, the "22nd", etc.
   * @param n a non-negative integer to convert
   * @return the ordinal string representation of <code>n</code> */
  public static String ordinal(final int n) {
    nonnegative(n);
    final String th = "th";
    switch (n % 10) {
      case 1:
        return n + (n != 11 ? "st" : th);
      case 2:
        return n + (n != 12 ? "nd" : th);
      default:
        return n + th;
    }
  }

  /** Wrap an object in parenthesis
   * @param o a non-<code><b>null</b></code> object for wrapping in parenthesis
   * @return the result of <code>o.toString()</code> wrapped parenthesis */
  public static String paren(final Object o) {
    return "(" + o + ")";
  }

  public static String pluralize(final int n, final String singular) {
    return pluralize(n, singular, singular + "s");
  }

  public static String pluralize(final int n, final String singular, final String plural) {
    switch (n) {
      case 0:
        return "no " + plural;
      case 1:
        return "" + singular;
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
        return n + " " + plural;
    }
  }

  public static String pretty(final String singular, final Collection<? extends Object> a) {
    return pretty(singular, singular + "s", a);
  }

  public static String pretty(final String singular, final String plural, final Collection<? extends Object> a) {
    if (a == null || a.size() <= 0)
      return "";
    if (a.size() == 1)
      return "1 " + singular + ": " + a.iterator().next() + "\n";
    String $ = a.size() + " " + plural + ":\n";
    int n = 0;
    final Once ellipsis = new Once("\t...\n");
    for (final Object o : a) {
      ++n;
      $ += n > MAX_FIRST && n <= a.size() - MAX_LAST ? ellipsis : "\t" + n + ") " + o + "\n";
    }
    return $;
  }

  /** Quote an object
   * @param o a non-<code><b>null</b></code> object for quoting
   * @return the result of <code>o.toString()</code> wrapped with single
   *         quotes */
  public static String quote(final Object o) {
    return wrap('\'', o.toString());
  }

  public static String repeat(final int n, final char c) {
    return repeat(n, "" + c);
  }

  /** Repeat a string a fixed number of times
   * @param n a non-negative integer
   * @param s a string to repeat
   * @return a {@link String} containing <code>s</code> concatenated
   *         <code>n</code> times */
  public static String repeat(final int n, final String s) {
    final StringBuffer $ = new StringBuffer();
    for (int i = 0; i < n; i++)
      $.append(s);
    return $.toString();
  }

  /** Chop the first character of a string.
   * @param s a non-<code><b>null</b></code> string of length at least one
   * @return <code>s</code> but without its first character. */
  public static String rest(final String s) {
    nonnull(s);
    positive(s.length());
    return s.substring(1);
  }

  public static int signum(final double d) {
    return d == 0 ? 0 : d > 0 ? 1 : -1;
  }

  public static String sprintf(final String format, final Object... args) {
    return new Formatter().format(format, args).toString();
  }

  public static String sprintf(final String[] args) {
    switch (args.length) {
      case 0:
        return "";
      case 1:
        return args[0];
      default:
        final Object os[] = new Object[args.length - 1];
        for (int i = 1; i < args.length; i++)
          os[i - 1] = args[i];
        return new Formatter().format(args[0], os).toString();
    }
  }

  /** Strip the first and last character of a string.
   * @param s a non-<code><b>null</b></code> string of length at least two to
   *        strip
   * @return <code>s</code> but without its first and last character. */
  public static String strip(final String s) {
    nonnull(s);
    require(s.length() >= 2);
    return s.substring(1, s.length() - 1);
  }

  public static List<String> toLines(final String s) throws IOException {
    final List<String> $ = new ArrayList<>();
    final BufferedReader br = new BufferedReader(new StringReader(s));
    while (true) {
      final String line = br.readLine();
      if (line == null)
        return $;
      $.add(line);
    }
  }

  public static String upCounter(final int n) {
    switch (n) {
      case -1:
        return "";
      case 0:
        return "A";
      default:
        return toup(n);
    }
  }

  public static String visualize(final String s) {
    return esc(s).replaceAll(" ", "\\s");
  }

  public static String wrap(final char with, final String s) {
    return with + s + with;
  }

  public static String wrap(final String with, final String s) {
    return with + s + with;
  }

  private static String tolow(final int n) {
    return n == 0 ? "" : tolow(n / 26) + (char) ('a' + n % 26);
  }

  private static String toup(final int n) {
    return n == 0 ? "" : toup(n / 26) + (char) ('A' + n % 26);
  }
}
