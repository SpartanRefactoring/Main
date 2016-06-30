/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import il.org.spartan.iterables.Iterables;

/**
 * A class representing a separator string, which can be used for separating
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
 * @since 12/02/2006)
 */
public final class Separator {
  private final String s;
  boolean first = true;

  public Separator(final char c) {
    this("" + c);
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
  public static <T> String wrap(final String wrap, final Iterable<T> ts, final String between) {
    return wrap(wrap, wrap, ts, between);
  }
  public static <T> String wrap(final String begin, final String end, final Iterable<T> ts, final String between) {
    if (Iterables.isEmpty(ts))
      return "";
    final StringBuilder $ = new StringBuilder(begin);
    final Separator s = new Separator(between);
    for (final T t : ts)
      $.append(s).append(t);
    return as.string($.append(end));
  }
  public static <T> String wrap(final String begin, final String end, final T[] ts, final String between) {
    if (ts.length == 0)
      return "";
    final StringBuilder $ = new StringBuilder(begin);
    final Separator s = new Separator(between);
    for (final T t : ts)
      $.append(s).append(t);
    return as.string($.append(end));
  }
  public static void main(final String[] args) {
    final Separator s = new Separator(", ");
    for (final String a : args)
      System.out.print(s + a);
  }
  public static <T> String separateBy(final String between, final T[] ts) {
    return wrap("", "", ts, between);
  }
  public static String separateBy(final int[] is, final String between) {
    if (is.length == 0)
      return "";
    String $ = "";
    final Separator s = new Separator(between);
    for (final int i : is)
      $ += s + new Integer(i).toString();
    return $;
  }
}
