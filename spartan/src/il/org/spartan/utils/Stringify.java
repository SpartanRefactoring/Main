// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.utils;

import static fluent.ly.azzert.*;
import static fluent.ly.___.*;

import java.util.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.streotypes.*;
import il.org.spartan.utils.Separate.*;
import fluent.ly.*;

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
@Utility
public enum Stringify {
  ; // No values in this namespace.
  @NotNull public static <T> ArrayList<String> apply(@NotNull final F<T> f, @NotNull final Iterable<? extends T> ts) {
    @NotNull final ArrayList<String> $ = new ArrayList<>();
    for (final T ¢ : ts)
      $.add(f.__(¢));
    return $;
  }
  /** Create an option object with default setting, except for a specified
   * beginning string.
   * @param begin a non-<code><b>null</b></code> specifying the text to place at
   *        the beginning of a non-empty list.
   * @return a newly created options object with the default settings, except
   *         for the specified beginning string. */
  @NotNull public static Option begin(final String begin) {
    nonnull(begin);
    return new Option().begin(begin);
  }
  /** Create an option object with default setting, except for a specified
   * ending string.
   * @param end a non-<code><b>null</b></code> specifying the text to add at the
   *        end of a non-empty list.
   * @return a newly created options object with the default settings, except
   *         for the specified ending string. */
  @NotNull public static Option end(final String end) {
    nonnull(end);
    return new Option().end(end);
  }
  /** Create an option object with default setting, except for a specified
   * string to replace <code><b>null</b></code> values.
   * @param filler string to replace <code><b>null</b></code> values found in
   *        the list. If <code><b>null</b></code>, then empty elements are not .
   * @return a newly created options object with the default settings, except
   *         for the specified filler string. */
  @NotNull public static Option filler(final String filler) {
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
  @NotNull public static <T> String it(@NotNull final Iterable<? extends T> ts, @NotNull final F<T> f, @NotNull final Option o) {
    return it(apply(f, ts), o);
  }
  /** Convert an {@link Iterable} collection of objects to a {@link String}
   * using the default formatting style.
   * @param <T> type of arguments to be converted.
   * @param ¢ a collection of objects of type <code>T</code>.
   * @return a {@link String} representation of the parameter, prepared with the
   *         default list formatting style as represented by
   *         {@link Option#defaultStyle}. */
  @NotNull public static <T> String it(@NotNull final Iterable<T> ¢) {
    return it(¢, Option.defaultStyle);
  }
  /** Create a textual representation of an {@link Iterable} collection of
   * objects using a supplied formatting style.
   * @param <T> type of arguments to be converted.
   * @param ts an array of objects of type <code>T</code>.
   * @param o formatting style for this list.
   * @return a {@link String} representation of the parameter, prepared with the
   *         supplied formatting style. */
  @NotNull public static <T> String it(@NotNull final Iterable<T> ts, @NotNull final Option o) {
    return makeList(asStringArray(prune(ts, o), o.filler()), o);
  }
  /** Convert an array of objects to a {@link String} using the default
   * formatting style.
   * @param <T> type of arguments to be converted.
   * @param ¢ an array of objects of type <code>T</code>.
   * @return a {@link String} representation of the parameter, prepared with the
   *         default list formatting style as represented by
   *         {@link Option#defaultStyle}. */
  @NotNull public static <T> String it(@NotNull final T[] ¢) {
    return it(¢, Option.defaultStyle);
  }
  /** Convert an {@link Iterable} collection of objects to a {@link String}
   * using a supplied style.
   * @param <T> type of arguments to be converted.
   * @param ts an array of objects of type <code>T</code>.
   * @param o formatting style for this list.
   * @return a {@link String} representation of the parameter, prepared with the
   *         supplied formatting style. */
  @NotNull public static <T> String it(@NotNull final T[] ts, @NotNull final Option o) {
    return makeList(asStringArray(prune(ts, o), o.filler()), o);
  }
  /** Create an option object with default setting, except for the specification
   * <code><b>null</b></code> are to be omitted.
   * @return a newly created options object with the default settings, except
   *         for except for the specification <code><b>null</b></code> are to be
   *         omitted. */
  @NotNull public static Option omitNulls() {
    return new Option().omitNulls();
  }
  /** Create an option object with default setting, except for a specified
   * separator string.
   * @param separator a non-<code><b>null</b></code> specifying the text used to
   *        separate the list items.
   * @return a newly created options object with the default settings, except
   *         for the specified filler string. */
  @NotNull public static Option separator(final String separator) {
    nonnull(separator);
    return new Option().separator(separator);
  }
  private static <T> String[] asStringArray(@NotNull final Iterable<T> ts, final String nullFiller) {
    return asStringCollection(ts, nullFiller).toArray(new String[0]);
  }
  @NotNull private static <T> String[] asStringArray(@NotNull final T[] ts, final String nullFiller) {
    @NotNull final String[] $ = new String[ts.length];
    int i = 0;
    for (@Nullable final T ¢ : ts)
      $[i++] = ¢ != null ? ¢ + "" : nullFiller;
    return $;
  }
  @NotNull private static <T> Collection<String> asStringCollection(@NotNull final Iterable<T> ts, @Nullable final String nullFiller) {
    @NotNull final ArrayList<String> $ = new ArrayList<>();
    for (@Nullable final T ¢ : ts)
      if (¢ != null)
        $.add(¢ + "");
      else if (nullFiller != null)
        $.add(nullFiller);
    return $;
  }
  @NotNull private static String makeList(@NotNull final String[] ss, @NotNull final Option o) {
    return ss.length == 0 ? "" : o.begin() + Separate.by(ss, o.separator()) + o.end();
  }
  @NotNull private static <T> Iterable<T> prune(@NotNull final Iterable<T> ts, @NotNull final Option o) {
    return o.notOmittingNulls() ? ts : prune.nulls(ts);
  }
  @NotNull private static <T> T[] prune(@NotNull final T[] ts, @NotNull final Option o) {
    return o.notOmittingNulls() ? ts : prune.nulls(ts);
  }

  /** An options object for the containing class.
   * @author Yossi Gil, the Technion.
   * @since 28/08/2008 */
  public static class Option {
    public static final String COMMA = ", ";
    public static final String NL = "\n";
    public static final String NULL_TEXT = "(null)";
    public static final Option functionStyle = new Option("(", ")", "__", COMMA);
    public static final Option statementStyle = new Option("{", "}", "__", NL);
    public static final Option defaultStyle = new Option("[", "]", NULL_TEXT, ",");
    private String __begin;
    private String __end;
    @Nullable private String __filler;
    private String __separator;

    /** Create a new instance, initialized with the default formatting style. */
    public Option() {
      this(defaultStyle);
    }
    private Option(@NotNull final Option o) {
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
    @NotNull public Option begin(final String begin) {
      __begin = begin;
      return this;
    }
    public String end() {
      return __end;
    }
    @NotNull public Option end(final String end) {
      __end = end;
      return this;
    }
    @Nullable public String filler() {
      return __filler;
    }
    @NotNull public Option filler(final String filler) {
      __filler = filler;
      return this;
    }
    public boolean notOmittingNulls() {
      return __filler != null;
    }
    @NotNull public Option omitNulls() {
      __filler = null;
      return this;
    }
    public String separator() {
      return __separator;
    }
    @NotNull public Option separator(final String separator) {
      __separator = separator;
      return this;
    }
  }

  @SuppressWarnings("static-method")
  public static class TEST {
    private static String[] makeArray(final String... ¢) {
      return ¢;
    }
    @NotNull private static Collection<String> makeCollection(@NotNull final String... ss) {
      @NotNull final ArrayList<String> $ = new ArrayList<>();
      for (final String ¢ : ss)
        $.add(¢);
      return $;
    }
    @NotNull public Collection<String> makeCollectionABC() {
      return makeCollection("A", "B", "C");
    }
    @Test public void testArrayFillerSeparatorBeginEnd() {
      azzert.that(it(//
          makeArray(null, "A", null, "B", "C", null), //
          filler("__").begin("[[").end("]]").separator(":")//
      ), is("[[__:A:__:B:C:__]]"));
    }
    @Test public void testArrayNoBegin() {
      azzert.that(it(makeArray("A", "B", "C"), begin("")), is("A,B,C]"));
    }
    @Test public void testArrayNoEnd() {
      azzert.that(it(makeArray("A", "B", "C"), end("")), is("[A,B,C"));
    }
    @Test public void testDefaultsArray() {
      azzert.that(it(makeArray("A", "B", "C")), is("[A,B,C]"));
    }
    @Test public void testMakeBeginOption() {
      assert begin("(") != null;
    }
    @Test public void testMakeEndOption() {
      assert end(")") != null;
    }
    @Test public void testMakeFillerOption() {
      assert filler(")") != null;
    }
    @Test public void testMakeIgnoreNulls() {
      assert filler(")") != null;
    }
    @Test public void testMakeSeparatorOption() {
      assert separator(")") != null;
    }
    @Test public void testNulledList() {
      azzert.that(it(makeCollection(null, "A", null, "B", null, "C", null)), is("[(null),A,(null),B,(null),C,(null)]"));
    }
    @Test public void testNulledListPruned() {
      azzert.that(it(makeCollection(null, "A", null, "B", null, "C", null), filler(null)), is("[A,B,C]"));
    }
    @Test public void testNulledListPrunedWithOmitNulls() {
      azzert.that(it(makeCollection(null, "A", null, "B", null, "C", null), omitNulls()), is("[A,B,C]"));
    }
    @Test public void testPrunedEmptyCollectionBeginEnd() {
      azzert.that(it(//
          makeCollection(), //
          omitNulls().separator(",").begin("(").end(")")), is(""));
    }
    @Test public void testPrunedEmptyCollectionOmittingNullsBeginEnd() {
      azzert.that(it(//
          makeCollection(null, null, null), //
          omitNulls().separator(",").begin("(").end(")")), is(""));
    }
    @Test public void testPrunedNulledListCommas() {
      azzert.that(it(makeCollection(null, "A", null, "B", null, "C", null), omitNulls().separator(",")), is("[A,B,C]"));
    }
    @Test public void testPrunedNulledListCommasCallsBeginEnd() {
      azzert.that(it(//
          makeCollection(null, "A", null, "B", null, "C", null), //
          omitNulls().separator(",").begin("(").end(")")), is("(A,B,C)"));
    }
    @Test public void testPrunedNulledListSemiColons() {
      azzert.that(it(makeCollection(null, "A", null, "B", null, "C", null), omitNulls().separator(";")), is("[A;B;C]"));
    }
    @Test public void testSimpleList() {
      azzert.that(it(makeCollection("A", "B", "C")), is("[A,B,C]"));
    }
  }
}
