// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.strings;

import java.util.*;

import il.org.spartan.streotypes.*;

/** A utility class with functions to escape special HTML characters in a
 * {@link String}, before including it in an HTML page.
 * <p>
 * The escaped characters are {@link #LESS_THAN}, {@link #GREATER_THAN},
 * {@link #QUOTE} and {@link #AMPERSAND}.
 * @author Yossi Gil, the Technion. */
@Utility public enum HTML {
  ;
  /** The '<' character. */
  public static final char LESS_THAN = '<';
  /** The '>' character. */
  public static final char GREATER_THAN = '>';
  /** The '"' character. */
  public static final char QUOTE = '"';
  /** The '&' character. */
  public static final char AMPERSAND = '&';

  public static String beginTag(final String tag) {
    return "<" + tag + ">";
  }

  public static String endTag(final String tag) {
    return beginTag("/" + tag);
  }

  /** Escape a single <code><b>char</b></code> for inclusion in an HTML page.
   * @param c a <code><b>char</b></code> to escape.
   * @return a string representing the escaped form of the parameter (if it is a
   *         special character). Otherwise, the string containing the
   *         parameter. */
  public static String esc(final char c) {
    switch (c) {
      case ' ':
        return "&nbsp;";
      case '\t':
        return "&09;";
      case LESS_THAN:
        return "&lt;";
      case GREATER_THAN:
        return "&gt;";
      case QUOTE:
        return "&quot;";
      case AMPERSAND:
        return "&amp;";
      default:
        return "" + c;
    }
  }

  /** Escape a {@link Collection} of {@link String}s for inclusion in an HTML
   * page.
   * @param ss {@link Collection} of {@link String}s to be escaped.
   * @return an array containing the escaped version of the elements of the
   *         parameter. */
  public static String[] esc(final Collection<String> ss) {
    return esc(ss.toArray(new String[ss.size()]));
  }

  /** Escape a {@link String} for inclusion in an HTML page.
   * @param s a string to escape.
   * @return a string representing the escaped form of the parameter, where all
   *         special HTML characters are escaped. */
  public static String esc(final String s) {
    final StringBuilder $ = new StringBuilder();
    for (int i = 0; i < s.length(); i++)
      $.append(esc(s.charAt(i)));
    return $.toString();
  }

  /** Escape an array of {@link String}s for inclusion in an HTML page.
   * @param ss an array of strings escape.
   * @return the same array, where each entry is replaced by its escaped
   *         form. */
  public static String[] esc(final String[] ss) {
    for (int i = 0; i < ss.length; i++)
      ss[i] = esc(ss[i]);
    return ss;
  }

  public static char first(final String s) {
    return s.charAt(0);
  }

  public static char last(final String s) {
    return s.charAt(s.length() - 1);
  }

  public static String peel(final String s) {
    return s.substring(1, s.length() - 1);
  }

  public static String tag(final String tag, final String text) {
    return beginTag(tag) + text + endTag(tag);
  }

  public static String tagContents(final String tag, final String s) {
    return new StringBuilder(first(s)).append(tag(tag, peel(s))).append(last(s)).toString();
  }
}
