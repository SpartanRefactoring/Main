package il.org.spartan.strings;

import java.lang.reflect.*;
import java.util.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.*;

/** An immutable class, whose various constructors concatenate the string value
 * of a list of items, optionally separated by a separator.
 * @author Yossi Gil (
 * @since 22/02/2006) */
@Antiexample public class Stringer {
  public static String compaq(final String s) {
    if (s == null)
      return null;
    String $ = "";
    for (final char c : s.toCharArray())
      $ += Character.isSpaceChar(c) ? "" : "" + c;
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
  public static String concatAt(final String lhs, final int pos, final String rhs) {
    return lhs.substring(0, pos >= 0 ? pos : lhs.length() + pos) + rhs;
  }

  /** Add leading zeros to a sequence of consecutive digits appearing at the
   * suffix of a String. This allows sorting to follow the natural order (i.e.:
   * abc2 should come before abc21).
   * @param s Input string
   * @return Fixed string derived from s */
  public static String fixNumericalSuffix(final String s) {
    if (s == null || s.length() == 0)
      return s;
    int numDigits = 0;
    for (int i = 0, len = s.length(); i < len; ++i) {
      numDigits = i;
      if (!Character.isDigit(s.charAt(len - i - 1)))
        break;
    }
    if (numDigits == 0)
      return s;
    final int firstDigitIndex = s.length() - numDigits;
    final String ZEROS = "0000000";
    if (numDigits >= ZEROS.length())
      return s;
    final String z = ZEROS.substring(0, ZEROS.length() - numDigits);
    return s.substring(0, firstDigitIndex) + z + s.substring(firstDigitIndex);
  }

  /** Return the largest prefix of a String the does not contain a certain
   * character.
   * @param s String whose prefix is to be taken
   * @param c The character that should not appear in the prefix
   * @return Prefix of s. If s does not contain the character c then s is
   *         returned. */
  public static String prefixUntil(final String s, final char c) {
    final int p = s.indexOf(c);
    return s.substring(0, p >= 0 ? p : s.length());
  }

  /** @param <T> type of items in the list
   * @param begin the string starting the string representation.
   * @param ts the actual items in the list, method <code>toString()</code> is
   *        used to compute obtain each item string represntation.
   * @param sep a string so separate these items
   * @param end a string terminating the string representation
   * @return the string equivalent of the <code>ts</code> in the following
   *         structure: <code> begin item1 sep item2 sep ... item2 end</code> */
  public static final <T> String sequence(final String begin, final Iterable<T> ts, final String sep, final String end) {
    final StringBuilder b = new StringBuilder(begin);
    final Separator s = new Separator(sep);
    for (final T t : ts)
      b.append(s).append(t);
    b.append(end);
    return b.toString();
  }

  /** @param <T> type of items in the list
   * @param begin the string starting the string representation.
   * @param ts the actual items in the list, method <code>toString()</code> is
   *        used to compute obtain each item string represntation.
   * @param sep a string so separate these items
   * @param end a string terminating the string representation
   * @return the string equivalent of the <code>ts</code> in the following
   *         structure: <code> begin item1 sep item2 sep ... item2 end</code> */
  public static final <T> String sequence(final String begin, final T[] ts, final String sep, final String end) {
    final StringBuilder b = new StringBuilder(begin);
    final Separator s = new Separator(sep);
    for (final T t : ts)
      b.append(s).append(t);
    b.append(end);
    return b.toString();
  }

  /** @author Oren Rubin
   * @param <T> type of items in the list
   * @param begin the string starting the string representation.
   * @param ts the actual items in the list, method <code>toString()</code> is
   *        used to compute obtain each item string represntation.
   * @param sep a string so separate these items
   * @param end a string terminating the string representation
   * @param converter class to customize conversions.
   * @return the string equivalent of the <code>ts</code> in the following
   *         structure: <code> begin item1 sep item2 sep ... item2 end</code> */
  public static final <T> String sequence(final String begin, final T[] ts, final String sep, final String end, final Converter<T> converter) {
    final StringBuilder b = new StringBuilder(begin);
    final Separator s = new Separator(sep);
    for (final T t : ts)
      b.append(s).append(converter.convert(t));
    b.append(end);
    return b.toString();
  }

  /** Return the longest suffix of a String the starts with a certain character.
   * @param s String whose suffix is to be taken
   * @param c First character of the suffix.
   * @return Suffix of s. If s does not contain the character c then the empty
   *         string is returned. */
  public static String suffixFrom(final String s, final char c) {
    final int p = s.indexOf(c);
    return p < 0 ? "" : s.substring(p);
  }

  /** Generate a string specifying the values of all declared fields of the
   * given object.
   * @param o Object to inspect
   * @return String representation of o */
  public static String toString(final Object o) {
    final List<String> list = new ArrayList<>();
    for (final Field f : o.getClass().getDeclaredFields()) {
      f.setAccessible(true);
      try {
        list.add(f.getName() + "=" + f.get(o));
      } catch (final IllegalArgumentException e) {
        list.add(f.getName() + "= (illegal argument) " + e.getMessage());
      } catch (final IllegalAccessException e) {
        list.add(f.getName() + "= (illegal access) " + e.getMessage());
      }
    }
    return Separate.byCommas(list);
  }

  private final String value;

  public <T> Stringer(final String between, final int... ts) {
    final StringBuilder b = new StringBuilder();
    final Separator s = new Separator(between);
    for (final int i : ts)
      b.append(s).append(i);
    value = b.toString();
  }

  public <T> Stringer(final String separator, final String nullStr, final T... ts) {
    final StringBuilder b = new StringBuilder();
    final Separator s = new Separator(separator);
    for (final T t : ts) {
      b.append(s);
      b.append(t != null ? t : nullStr);
    }
    value = b.toString();
  }

  public <T> Stringer(final String between, final T... ts) {
    this(between, "null", ts);
  }

  @Override public String toString() {
    return value;
  }

  public static interface Converter<T> {
    public String convert(T toBeConverted);
  }
}
