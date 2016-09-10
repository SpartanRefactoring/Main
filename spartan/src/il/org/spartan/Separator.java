/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import il.org.spartan.iterables.*;

/** A class representing a separator string, which can be used for separating
 * elements of a sequence while printing it, without special case treatment of
 * the first and last element. For example, the following program prints a list
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
 * @author Yossi Gil
 * @since 12/02/2006) */
public final class Separator {
  /** @param args JD */
  public static void main(final String[] args) {
    final Separator s = new Separator(", ");
    for (final String a : args)
      System.out.print(s + a);
  }

  /** <code>separateBy</code>
   * @param is JD
   * @param between what to put between the items
   * @return String TODO Javadoc(2016) automatically generated for returned
   *         value of method <code>separateBy</code> */
  public static String separateBy(final int[] is, final String between) {
    if (is.length == 0)
      return "";
    String $ = "";
    final Separator s = new Separator(between);
    for (final int i : is)
      $ += s + (new Integer(i) + "");
    return $;
  }

  /** <code>separateBy</code> returning String
   * @param <T> JD
   * @param between what to put between the items
   * @param ts JD
   * @return the parameters separated */
  public static <T> String separateBy(final String between, final T[] ts) {
    return wrap("", "", ts, between);
  }

  /** TODO Javadoc(2016): automatically generated for method <code>wrap</code>
   * @param <T> JD
   * @param wrap TODO
   * @param ts JD
   * @param between what to put between the items
   * @return String TODO Javadoc(2016) automatically generated for returned
   *         value of method <code>wrap</code> */
  public static <T> String wrap(final String wrap, final Iterable<T> ts, final String between) {
    return wrap(wrap, wrap, ts, between);
  }

  /** @param <T> JD
   * @param begin what to place before the items
   * @param end what to place after the items
   * @param ts JD
   * @param between what to put between the items
   * @return String // TODO: automatically generated for return method
   *         <code>wrap</code> */
  public static <T> String wrap(final String begin, final String end, final Iterable<T> ts, final String between) {
    if (iterables.isEmpty(ts))
      return "";
    final StringBuilder $ = new StringBuilder(begin);
    final Separator s = new Separator(between);
    for (final T t : ts)
      $.append(s).append(t);
    return as.string($.append(end));
  }

  /** @param <T> JD
   * @param begin what to place before the items
   * @param end what to place after the items
   * @param ts JD
   * @param between what to put between the items
   * @return TODO document return type */
  public static <T> String wrap(final String begin, final String end, final T[] ts, final String between) {
    if (ts.length == 0)
      return "";
    final StringBuilder $ = new StringBuilder(begin);
    final Separator s = new Separator(between);
    for (final T t : ts)
      $.append(s).append(t);
    return as.string($.append(end));
  }

  private boolean first = true;
  private final String s;

  /** Instantiates this class.
   * @param c JD */
  public Separator(final char c) {
    this(c + "");
  }

  /** Instantiates this class.
   * @param s JD */
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