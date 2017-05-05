package il.org.spartan.strings;

import java.lang.reflect.*;
import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;

/** An immutable class, whose various constructors concatenate the string value
 * of a list of items, optionally separated by a separator.
 * @author Yossi Gil (
 * @since 22/02/2006) */
@Antiexample
public class Stringer {
  @Nullable public static String compaq(@Nullable final String s) {
    if (s == null)
      return null;
    @NotNull String $ = "";
    for (final char ¢ : s.toCharArray())
      $ += Character.isSpaceChar(¢) ? "" : ¢ + "";
    return $;
  }
  /** Concatenate a prefix of a string with another string. The prefix is
   * determined by the value of <code>pos</code> parameter:
   * <ul>
   * <li>If non-negative the prefix is the first <code>pos</code> characters of
   * <code>lhs</code>
   * <li>Otherwise, the prefix is all the characters of <code>lhs</code> but the
   * last <code>pos</code> character.
   * </ul>
   * @param lhs Left hand side string
   * @param pos Position in the <code>lhs</code> string
   * @param rhs Right hand side string
   * @return Concatenated String */
  @NotNull public static String concatAt(@NotNull final String lhs, final int pos, final String rhs) {
    return lhs.substring(0, pos >= 0 ? pos : lhs.length() + pos) + rhs;
  }
  /** Add leading zeros to a sequence of consecutive digits appearing at the
   * suffix of a String. This allows sorting to follow the natural order (i.e.:
   * abc2 should come before abc21).
   * @param s Input string
   * @return Fixed string derived from s */
  @Nullable public static String fixNumericalSuffix(@Nullable final String s) {
    if (s == null || s.length() == 0)
      return s;
    int $ = 0;
    for (int ¢ = 0, len = s.length(); ¢ < len; ++¢) {
      $ = ¢;
      if (!Character.isDigit(s.charAt(len - ¢ - 1)))
        break;
    }
    if ($ == 0)
      return s;
    final int firstDigitIndex = s.length() - $;
    @NotNull final String ZEROS = "0000000";
    return $ >= ZEROS.length() ? s : s.substring(0, firstDigitIndex) + ZEROS.substring(0, ZEROS.length() - $) + s.substring(firstDigitIndex);
  }
  /** Return the largest prefix of a String the does not contain a certain
   * character.
   * @param s String whose prefix is to be taken
   * @param c The character that should not appear in the prefix
   * @return Prefix of s. If s does not contain the character c then s is
   *         returned. */
  public static String prefixUntil(@NotNull final String s, final char c) {
    final int $ = s.indexOf(c);
    return s.substring(0, $ >= 0 ? $ : s.length());
  }
  /** @param <T> type of items in the list
   * @param begin the string starting the string representation.
   * @param ts the actual items in the list, method <code>toString()</code> is
   *        used to compute obtain each item string represntation.
   * @param sep a string so separate these items
   * @param end a string terminating the string representation
   * @return the string equivalent of the <code>ts</code> in the following
   *         structure: <code> begin item1 sep item2 sep ... item2 end</code> */
  @NotNull public static <T> String sequence(@NotNull final String begin, @NotNull final Iterable<T> ts, final String sep, final String end) {
    @NotNull final StringBuilder $ = new StringBuilder(begin);
    @NotNull final Separator s = new Separator(sep);
    for (final T ¢ : ts)
      $.append(s).append(¢);
    $.append(end);
    return $ + "";
  }
  /** @param <T> type of items in the list
   * @param begin the string starting the string representation.
   * @param ts the actual items in the list, method <code>toString()</code> is
   *        used to compute obtain each item string represntation.
   * @param sep a string so separate these items
   * @param end a string terminating the string representation
   * @return the string equivalent of the <code>ts</code> in the following
   *         structure: <code> begin item1 sep item2 sep ... item2 end</code> */
  @NotNull public static <T> String sequence(@NotNull final String begin, @NotNull final T[] ts, final String sep, final String end) {
    @NotNull final StringBuilder $ = new StringBuilder(begin);
    @NotNull final Separator s = new Separator(sep);
    for (final T ¢ : ts)
      $.append(s).append(¢);
    $.append(end);
    return $ + "";
  }
  /** @author Oren Rubin
   * @param <T> type of items in the list
   * @param begin the string starting the string representation.
   * @param ts the actual items in the list, method <code>toString()</code> is
   *        used to compute obtain each item string represntation.
   * @param sep a string so separate these items
   * @param end a string terminating the string representation
   * @param c class to customize conversions.
   * @return the string equivalent of the <code>ts</code> in the following
   *         structure: <code> begin item1 sep item2 sep ... item2 end</code> */
  @NotNull public static <T> String sequence(@NotNull final String begin, @NotNull final T[] ts, final String sep, final String end,
      @NotNull final Converter<T> c) {
    @NotNull final StringBuilder $ = new StringBuilder(begin);
    @NotNull final Separator s = new Separator(sep);
    for (final T ¢ : ts)
      $.append(s).append(c.convert(¢));
    $.append(end);
    return $ + "";
  }
  /** Return the longest suffix of a String the starts with a certain character.
   * @param s String whose suffix is to be taken
   * @param c First character of the suffix.
   * @return Suffix of s. If s does not contain the character c then the empty
   *         string is returned. */
  @NotNull public static String suffixFrom(@NotNull final String s, final char c) {
    final int $ = s.indexOf(c);
    return $ < 0 ? "" : s.substring($);
  }
  /** Generate a string specifying the values of all declared fields of the
   * given object.
   * @param o Object to inspect
   * @return String representation of o */
  @NotNull public static String toString(@NotNull final Object o) {
    @NotNull final List<String> $ = new ArrayList<>();
    for (@NotNull final Field f : o.getClass().getDeclaredFields()) {
      f.setAccessible(true);
      try {
        $.add(f.getName() + "=" + f.get(o));
      } catch (@NotNull final IllegalArgumentException ¢) {
        $.add(f.getName() + "= (illegal argument) " + ¢.getMessage());
      } catch (@NotNull final IllegalAccessException ¢) {
        $.add(f.getName() + "= (illegal access) " + ¢.getMessage());
      }
    }
    return Separate.byCommas($);
  }

  @NotNull private final String value;

  public Stringer(final String between, @NotNull final int... ts) {
    @NotNull final StringBuilder b = new StringBuilder();
    @NotNull final Separator s = new Separator(between);
    for (final int ¢ : ts)
      b.append(s).append(¢);
    value = b + "";
  }
  public <T> Stringer(final String separator, final String nullStr, @NotNull final T... ts) {
    @NotNull final StringBuilder b = new StringBuilder();
    @NotNull final Separator s = new Separator(separator);
    for (@Nullable final T ¢ : ts) {
      b.append(s);
      b.append(¢ != null ? ¢ : nullStr);
    }
    value = b + "";
  }
  public <T> Stringer(final String between, final T... ts) {
    this(between, "null", ts);
  }
  @Override @NotNull public String toString() {
    return value;
  }

  public interface Converter<T> {
    @NotNull String convert(T toBeConverted);
  }
}
