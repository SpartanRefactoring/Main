// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;

/** A class representing a separator string, which can be used for separating
 * elements of a sequence while printing it, without special case treatment of
 * the first or last element. For example, the following program prints a list
 * of its arguments separated by commas, without using any conditionals.
 *
 * <pre>
 * static void main(String[] args) {
 *   Separator s = new Separator(&quot;, &quot;);
 *   for (String a : args)
 *     System.out.print(s + a);
 * }
 * </pre>
 *
 * @author Yossi Gil (
 * @since 12/02/2006) */
@Instantiable public final class Separator {
  public static <T> boolean isEmpty(@NotNull final Iterable<T> items) {
    return !items.iterator().hasNext();
  }

  @NotNull public static String separateBy(@NotNull final int[] is, final String between) {
    if (is.length == 0)
      return "";
    @NotNull String $ = "";
    @NotNull final Separator s = new Separator(between);
    for (final int ¢ : is)
      $ += s + "" + Integer.valueOf(¢);
    return $;
  }

  @NotNull public static <T> String separateBy(final String between, @NotNull final T[] items) {
    return wrap("", "", items, between);
  }

  @NotNull public static <T> String wrap(final String wrap, @NotNull final Iterable<T> items, final String between) {
    return wrap(wrap, wrap, items, between);
  }

  @NotNull public static <T> String wrap(final String begin, final String end, @NotNull final Iterable<T> items, final String between) {
    if (isEmpty(items))
      return "";
    String $ = begin;
    @NotNull final Separator s = new Separator(between);
    for (final T ¢ : items)
      $ += s + "" + ¢;
    return $ + end;
  }

  @NotNull public static <T> String wrap(final String begin, final String end, @NotNull final T[] items, final String between) {
    if (items.length == 0)
      return "";
    String $ = begin;
    @NotNull final Separator s = new Separator(between);
    for (final T ¢ : items)
      $ += s + "" + ¢;
    return $ + end;
  }

  static void main(@NotNull final String[] args) {
    for (final String a : args)
      System.out.print(new Separator(", ") + a);
  }

  private final String s;
  boolean first = true;

  public Separator(final char c) {
    s = c + "";
  }

  public Separator(final String s) {
    this.s = s;
  }

  @Override public String toString() {
    if (!first)
      return s;
    first = false;
    return "";
  }
}
