// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.strings;

import java.util.*;

import org.jetbrains.annotations.*;

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

  @NotNull public static String beginTag(final String tag) {
    return "<" + tag + ">";
  }

  @NotNull public static String endTag(final String tag) {
    return beginTag("/" + tag);
  }

  /** Escape a single <code><b>char</b></code> for inclusion in an HTML page.
   * @param ¢ a <code><b>char</b></code> to escape.
   * @return a string representing the escaped form of the parameter (if it is a
   *         special character). Otherwise, the string containing the
   *         parameter. */
  @NotNull public static String esc(final char ¢) {
    switch (¢) {
      case ' ':
        return "&nbsp;";
      case '\t':
        return "&09;";
      case AMPERSAND:
        return "&amp;";
      case GREATER_THAN:
        return "&gt;";
      case LESS_THAN:
        return "&lt;";
      case QUOTE:
        return "&quot;";
      default:
        return ¢ + "";
    }
  }

  /** Escape a {@link Collection} of {@link String}s for inclusion in an HTML
   * page.
   * @param ¢ {@link Collection} of {@link String}s to be escaped.
   * @return an array containing the escaped version of the elements of the
   *         parameter. */
  @NotNull public static String[] esc(@NotNull final Collection<String> ¢) {
    return esc(¢.toArray(new String[¢.size()]));
  }

  /** Escape a {@link String} for inclusion in an HTML page.
   * @param s a string to escape.
   * @return a string representing the escaped form of the parameter, where all
   *         special HTML characters are escaped. */
  @NotNull public static String esc(@NotNull final String s) {
    @NotNull final StringBuilder $ = new StringBuilder();
    for (int ¢ = 0; ¢ < s.length(); ++¢)
      $.append(esc(s.charAt(¢)));
    return $ + "";
  }

  /** Escape an array of {@link String}s for inclusion in an HTML page.
   * @param ss an array of strings escape.
   * @return the same array, where each entry is replaced by its escaped
   *         form. */
  @NotNull public static String[] esc(@NotNull final String[] $) {
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = esc($[¢]);
    return $;
  }

  public static char first(@NotNull final String ¢) {
    return ¢.charAt(0);
  }

  public static char last(@NotNull final String ¢) {
    return ¢.charAt(¢.length() - 1);
  }

  public static String peel(@NotNull final String ¢) {
    return ¢.substring(1, ¢.length() - 1);
  }

  @NotNull public static String tag(final String tag, final String text) {
    return beginTag(tag) + text + endTag(tag);
  }

  @NotNull public static String tagContents(final String tag, @NotNull final String s) {
    return first(s) + "" + tag(tag, peel(s)) + last(s);
  }
}
