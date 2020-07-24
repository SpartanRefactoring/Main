// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import il.org.spartan.streotypes.Instantiable;

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
@Instantiable
public final class Separator {
  public static <T> boolean isEmpty(final Iterable<T> items) {
    return !items.iterator().hasNext();
  }
  public static String separateBy(final int[] is, final String between) {
    if (is.length == 0)
      return "";
    String $ = "";
    final Separator s = new Separator(between);
    for (final int ¢ : is)
      $ += s + "" + Integer.valueOf(¢);
    return $;
  }
  public static <T> String separateBy(final String between, final T[] items) {
    return wrap("", "", items, between);
  }
  public static <T> String wrap(final String wrap, final Iterable<T> items, final String between) {
    return wrap(wrap, wrap, items, between);
  }
  public static <T> String wrap(final String begin, final String end, final Iterable<T> items, final String between) {
    if (isEmpty(items))
      return "";
    String $ = begin;
    final Separator s = new Separator(between);
    for (final T ¢ : items)
      $ += s + "" + ¢;
    return $ + end;
  }
  public static <T> String wrap(final String begin, final String end, final T[] items, final String between) {
    if (items.length == 0)
      return "";
    String $ = begin;
    final Separator s = new Separator(between);
    for (final T ¢ : items)
      $ += s + "" + ¢;
    return $ + end;
  }
  static void main(final String[] args) {
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
