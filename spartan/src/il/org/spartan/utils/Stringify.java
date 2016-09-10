// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import static il.org.spartan.utils.___.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.Separate.*;
import il.org.spatan.iteration.*;

/** A <b>Utility Class</b> providing services of converting a collection or an
 * array into a {@link String}, with means for omitting <code><b>null</b></code>
 * elements, replacing these with a designated filler, adding a separator string
 * between elements, and a beginning and ending texts which are printed only if
 * the collection/array is not empty. For example, the following function
 *
 * <pre>
 *  * void g(String... ss) {
 * 	System.out.println(//
 * 	        Stringify.it(ss, begin(&quot;&lt;&lt;&quot;).end(&quot;&gt;&gt;&quot;).separator(&quot;|&quot;).omitNulls()) //
 * 	        );
 * }
 * </pre>
 *
 * prints a list of its non-<code><b>null</b></code> arguments, wrapped with
 * double angular brackets and separated by a vertical bar. Nothing is printed
 * though if no non-<code><b>null</b></code> are passed to the program.
 * <p>
 * Observe that the formatting style in the above example is specified with an
 * <b>Options Object</b> of type {@link Stringify.Option}, created by the
 * expression
 *
 * <pre>
 * begin(&quot;&lt;&lt;&quot;).end(&quot;&gt;&gt;&quot;).separator(&quot;|&quot;).omitNulls()
 * </pre>
 *
 * In this expression, <code>begin(&quot;&lt;&lt;&quot;)</code> is a
 * <code><b>static</b></code> function defined in {@link Stringify} which
 * creates this object. The remaining functions in this call chain are defined
 * in {@link Stringify.Option} and modify the options object.
 * @author Yossi Gil, the Technion.
 * @since 28/08/2008 */
@Utility public enum Stringify {
  ; // No values in this namespace.
  public static <T> ArrayList<String> apply(final F<T> f, final Iterable<? extends T> ts) {
    final ArrayList<String> $ = new ArrayList<>();
    for (final T t : ts)
      $.add(f.__(t));
    return $;
  }

  /** Create an option object with default setting, except for a specified
   * beginning string.
   * @param begin a non-<code><b>null</b></code> specifying the text to place at
   *        the beginning of a non-empty list.
   * @return a newly created options object with the default settings, except
   *         for the specified beginning string. */
  public static Option begin(final String begin) {
    nonnull(begin);
    return new Option().begin(begin);
  }

  /** Create an option object with default setting, except for a specified
   * ending string.
   * @param end a non-<code><b>null</b></code> specifying the text to add at the
   *        end of a non-empty list.
   * @return a newly created options object with the default settings, except
   *         for the specified ending string. */
  public static Option end(final String end) {
    nonnull(end);
    return new Option().end(end);
  }

  /** Create an option object with default setting, except for a specified
   * string to replace <code><b>null</b></code> values.
   * @param filler string to replace <code><b>null</b></code> values found in
   *        the list. If <code><b>null</b></code>, then empty elements are not .
   * @return a newly created options object with the default settings, except
   *         for the specified filler string. */
  public static Option filler(final String filler) {
    return new Option().filler(filler);
  }

  /** Create a textual representation of an {@link Iterable} collection of
   * objects using a supplied formatting style, after applying a user supplied
   * conversion to each object.
   * @param ts an array of objects of type <code>T</code>.
   * @param f a function to apply on each object prior to conversion.
   * @param o formatting style for this list.
   * @param <T> type of arguments to be converted.
   * @return a {@link String} representation of the parameter, prepared with the
   *         supplied formatting style. */
  public static <T> String it(final Iterable<? extends T> ts, final F<T> f, final Option o) {
    return it(apply(f, ts), o);
  }

  /** Convert an {@link Iterable} collection of objects to a {@link String}
   * using the default formatting style.
   * @param <T> type of arguments to be converted.
   * @param ts a collection of objects of type <code>T</code>.
   * @return a {@link String} representation of the parameter, prepared with the
   *         default list formatting style as represented by
   *         {@link Option#defaultStyle}. */
  public static <T> String it(final Iterable<T> ts) {
    return it(ts, Option.defaultStyle);
  }

  /** Create a textual representation of an {@link Iterable} collection of
   * objects using a supplied formatting style.
   * @param <T> type of arguments to be converted.
   * @param ts an array of objects of type <code>T</code>.
   * @param o formatting style for this list.
   * @return a {@link String} representation of the parameter, prepared with the
   *         supplied formatting style. */
  public static <T> String it(final Iterable<T> ts, final Option o) {
    return makeList(asStringArray(prune(ts, o), o.filler()), o);
  }

  /** Convert an array of objects to a {@link String} using the default
   * formatting style.
   * @param <T> type of arguments to be converted.
   * @param ts an array of objects of type <code>T</code>.
   * @return a {@link String} representation of the parameter, prepared with the
   *         default list formatting style as represented by
   *         {@link Option#defaultStyle}. */
  public static <T> String it(final T[] ts) {
    return it(ts, Option.defaultStyle);
  }

  /** Convert an {@link Iterable} collection of objects to a {@link String}
   * using a supplied style.
   * @param <T> type of arguments to be converted.
   * @param ts an array of objects of type <code>T</code>.
   * @param o formatting style for this list.
   * @return a {@link String} representation of the parameter, prepared with the
   *         supplied formatting style. */
  public static <T> String it(final T[] ts, final Option o) {
    return makeList(asStringArray(prune(ts, o), o.filler()), o);
  }

  /** Create an option object with default setting, except for the specification
   * <code><b>null</b></code> are to be omitted.
   * @return a newly created options object with the default settings, except
   *         for except for the specification <code><b>null</b></code> are to be
   *         omitted. */
  public static Option omitNulls() {
    return new Option().omitNulls();
  }

  /** Create an option object with default setting, except for a specified
   * separator string.
   * @param separator a non-<code><b>null</b></code> specifying the text used to
   *        separate the list items.
   * @return a newly created options object with the default settings, except
   *         for the specified filler string. */
  public static Option separator(final String separator) {
    nonnull(separator);
    return new Option().separator(separator);
  }

  private static <T> String[] asStringArray(final Iterable<T> ts, final String nullFiller) {
    return asStringCollection(ts, nullFiller).toArray(new String[0]);
  }

  private static <T> String[] asStringArray(final T[] ts, final String nullFiller) {
    final String[] $ = new String[ts.length];
    int i = 0;
    for (final T t : ts)
      $[i++] = t == null ? nullFiller : t.toString();
    return $;
  }

  private static <T> Collection<String> asStringCollection(final Iterable<T> ts, final String nullFiller) {
    final ArrayList<String> $ = new ArrayList<>();
    for (final T t : ts)
      if (t != null)
        $.add(t.toString());
      else if (nullFiller != null)
        $.add(nullFiller);
    return $;
  }

  private static String makeList(final String[] ss, final Option o) {
    return ss.length == 0 ? "" : o.begin() + Separate.by(ss, o.separator()) + o.end();
  }

  private static <T> Iterable<T> prune(final Iterable<T> ts, final Option o) {
    return o.isOmittingNulls() ? Prune.nulls(ts) : ts;
  }

  private static <T> T[] prune(final T[] ts, final Option o) {
    return !o.isOmittingNulls() ? ts : Prune.nulls(ts);
  }

  /** An options object for the containing class.
   * @author Yossi Gil, the Technion.
   * @since 28/08/2008 */
  public static class Option {
    public static final String COMMA = ", ";
    public static final String NL = "\n";
    public static final String NULL__TEXT = "(null)";
    public static final Option functionStyle = new Option("(", ")", "__", COMMA);
    public static final Option statementStyle = new Option("{", "}", "__", NL);
    public static final Option defaultStyle = new Option("[", "]", NULL__TEXT, ",");
    private String __begin;
    private String __end;
    private String __filler;
    private String __separator;

    /** Create a new instance, initialized with the default formatting style. */
    public Option() {
      this(defaultStyle);
    }

    private Option(final Option o) {
      __begin = o.__begin;
      __end = o.__end;
      __filler = o.__filler;
      __separator = o.__separator;
    }

    private Option(final String __begin, final String __end, final String __filler, final String __separator) {
      this.__begin = __begin;
      this.__end = __end;
      this.__filler = __filler;
      this.__separator = __separator;
    }

    public String begin() {
      return __begin;
    }

    public Option begin(final String begin) {
      __begin = begin;
      return this;
    }

    public String end() {
      return __end;
    }

    public Option end(final String end) {
      __end = end;
      return this;
    }

    public String filler() {
      return __filler;
    }

    public Option filler(final String filler) {
      __filler = filler;
      return this;
    }

    public boolean isOmittingNulls() {
      return __filler == null;
    }

    public Option omitNulls() {
      __filler = null;
      return this;
    }

    public String separator() {
      return __separator;
    }

    public Option separator(final String separator) {
      __separator = separator;
      return this;
    }
  }

  @SuppressWarnings("static-method") public static class TEST {
    private static String[] makeArray(final String... ss) {
      return ss;
    }

    private static Collection<String> makeCollection(final String... ss) {
      final ArrayList<String> $ = new ArrayList<>();
      for (final String s : ss)
        $.add(s);
      return $;
    }

    public Collection<String> makeCollectionABC() {
      return makeCollection("A", "B", "C");
    }

    @Test public void testArrayFillerSeparatorBeginEnd() {
      assertEquals("[[__:A:__:B:C:__]]", //
          it(//
              makeArray(null, "A", null, "B", "C", null), //
              filler("__").begin("[[").end("]]").separator(":")//
          )//
      );
    }

    @Test public void testArrayNoBegin() {
      assertEquals("A,B,C]", it(makeArray("A", "B", "C"), begin("")));
    }

    @Test public void testArrayNoEnd() {
      assertEquals("[A,B,C", it(makeArray("A", "B", "C"), end("")));
    }

    @Test public void testDefaultsArray() {
      assertEquals("[A,B,C]", it(makeArray("A", "B", "C")));
    }

    @Test public void testMakeBeginOption() {
      assert null != begin("(");
    }

    @Test public void testMakeEndOption() {
      assert null != end(")");
    }

    @Test public void testMakeFillerOption() {
      assert null != filler(")");
    }

    @Test public void testMakeIgnoreNulls() {
      assert null != filler(")");
    }

    @Test public void testMakeSeparatorOption() {
      assert null != separator(")");
    }

    @Test public void testNulledList() {
      assertEquals("[(null),A,(null),B,(null),C,(null)]", it(makeCollection(null, "A", null, "B", null, "C", null)));
    }

    @Test public void testNulledListPruned() {
      assertEquals("[A,B,C]", it(makeCollection(null, "A", null, "B", null, "C", null), filler(null)));
    }

    @Test public void testNulledListPrunedWithOmitNulls() {
      assertEquals("[A,B,C]", it(makeCollection(null, "A", null, "B", null, "C", null), omitNulls()));
    }

    @Test public void testPrunedEmptyCollectionBeginEnd() {
      assertEquals("",
          it(//
              makeCollection(), //
              omitNulls().separator(",").begin("(").end(")")));
    }

    @Test public void testPrunedEmptyCollectionOmittingNullsBeginEnd() {
      assertEquals("",
          it(//
              makeCollection(null, null, null), //
              omitNulls().separator(",").begin("(").end(")")));
    }

    @Test public void testPrunedNulledListCommas() {
      assertEquals("[A,B,C]", it(makeCollection(null, "A", null, "B", null, "C", null), omitNulls().separator(",")));
    }

    @Test public void testPrunedNulledListCommasCallsBeginEnd() {
      assertEquals("(A,B,C)",
          it(//
              makeCollection(null, "A", null, "B", null, "C", null), //
              omitNulls().separator(",").begin("(").end(")")));
    }

    @Test public void testPrunedNulledListSemiColons() {
      assertEquals("[A;B;C]", it(makeCollection(null, "A", null, "B", null, "C", null), omitNulls().separator(";")));
    }

    @Test public void testSimpleList() {
      assertEquals("[A,B,C]", it(makeCollection("A", "B", "C")));
    }
  }
}
