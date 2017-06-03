/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package fluent.ly;

import static il.org.spartan.Utils.*;

import static fluent.ly.azzert.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.junit.*;

import an.*;
import il.org.spartan.*;

/** A utilities library functions that take an array or a collection, and return
 * a {@link String} composed by the elements of this collection, separated by a
 * given {@link String} or <code><b>char</b></code>.
 * @author Yossi Gil
 * @since 07/08/2008 */
public enum separate {
  ;
  /** The comma character */
  public static final String COMMA = ",";
  /** The dot character */
  public static final String DOT = ".";
  /** The Unix line separator character */
  public static final String NL = "\n";
  /** The space character */
  public static final String SPACE = " ";

  /** Separates a sequence of strings by {@link #SPACE} characters
   * @param $ what needs to be separated
   * @return parameters, separated by {@link #SPACE} */
  public static String bySpaces(final String... $) {
    return separateBySpaces(as.list($));
  }
  /** A simple program demonstrating the use of this class. This program prints
   * a comma separated list of its arguments, where special characters in each
   * argument are escaped prior to printing.
   * @param args list of the command line arguments. */
  public static void main(final String[] args) {
    System.out.println("Arguments are: " + separate.these(args).by(", "));
  }
  /** Separates an {@link Iterable} strings by {@link #SPACE} characters
   * @param $ what needs to be separated
   * @return parameters, separated by {@link #SPACE} */
  public static String separateBySpaces(final Iterable<String> $) {
    return as.string(separateBySpaces($.iterator()));
  }
  /** Separates an {@link Iterable} strings (specified by an {@link Iterator}
   * over it by {@link #SPACE} characters
   * @param s what needs to be separated
   * @return parameters, separated by {@link #SPACE} */
  public static String separateBySpaces(final Iterator<String> s) {
    final StringBuilder $ = new StringBuilder();
    for (final Separator ¢ = new Separator(SPACE); s.hasNext();)
      $.append(¢).append(s.next());
    return as.string($);
  }
  /** Factory method for generating a {@link SeparationSubject}, to be used
   * further for actual separation.
   * @return an empty {@link SeparationSubject} */
  public static SeparationSubject these() {
    return new SeparationSubject(new String[] {});
  }
  /** Separate elements of a given array of <code><b>boolean</b></code>s by a
   * given <code><b>char</b></code>
   * @param ¢ an array of elements to be separated
   * @return a {{@link String}} obtained by concatenating the textual
   *         representation of the elements in <code>bs</code> separated by
   *         <code>between</code> */
  public static SeparationSubject these(final boolean[] ¢) {
    return these(box.it(¢));
  }
  /** Separate elements of a given array of <code><b>byte</b></code>s by a given
   * <code><b>char</b></code>
   * @param ¢ an array of elements to be separated
   * @return a {{@link String}} obtained by concatenating the textual
   *         representation of the elements in <code>bs</code> separated by
   *         <code>between</code> */
  public static SeparationSubject these(final byte[] ¢) {
    return these(box.it(¢));
  }
  /** Separate elements of a given array of <code><b>char</b></code>s by a given
   * <code><b>char</b></code>
   * @param ¢ an array of elements to be separated
   * @return a {{@link String}} obtained by concatenating the textual
   *         representation of the elements in <code>cs</code> separated by
   *         <code>between</code> */
  public static SeparationSubject these(final char[] ¢) {
    return these(box.it(¢));
  }
  /** Separate elements of a given array of <code><b>double</b></code>s by a
   * given <code><b>char</b></code>
   * @param ¢ an array of elements to be separated
   * @return a {{@link String}} obtained by concatenating the textual
   *         representation of the elements in <code>ds</code> separated by
   *         <code>between</code> */
  public static SeparationSubject these(final double[] ¢) {
    return these(box.it(¢));
  }
  /** Separate elements of a given array of <code><b>float</b></code>s by a
   * given <code><b>char</b></code>
   * @param ¢ an array of elements to be separated
   * @return a {{@link String}} obtained by concatenating the textual
   *         representation of the elements in <code>fs</code> separated by
   *         <code>between</code> */
  public static SeparationSubject these(final float[] ¢) {
    return these(box.it(¢));
  }
  /** Separate a variable length list of arguments by a comma character.
   * @param os the objects to be separated. */
  public static SeparationSubject these(final Iterable<?> os) {
    return new SeparationSubject(os);
  }
  /** Separate elements of a given array of <code><b>long</b></code>s by a given
   * <code><b>char</b></code>
   * @param ¢ an array of elements to be separated
   * @return a {@link String} obtained by concatenating the textual
   *         representation of the elements in <code>ls</code> separated by
   *         <code>between</code> */
  public static SeparationSubject these(final long[] ¢) {
    return these(box.it(¢));
  }
  /** A simple minded separation of members of a {@link Map} data type.
   * @param <Key> type of elements serving as keys of the map.
   * @param <Value> type of elements serving as values of the map.
   * @param m a non-<code><b>null</b></code> {@link Map} objects whose entries
   *        are to be separated.
   * @return a concatenation of all map entries, separated by
   *         <code>separator</code>, and where the key of each entry is
   *         separated from the value by <code>arrow</code>. */
  public static <Key, Value> SeparationSubject these(final Map<Key, Value> ¢) {
    cantBeNull(¢);
    return new SeparationSubject(¢.keySet().stream().map(λ -> λ + "->" + ¢.get(λ)).collect(Collectors.toList()));
  }
  /** Separate elements of a given array of <code><b>short</b></code>s by a
   * given <code><b>char</b></code>
   * @param ¢ an array of elements to be separated
   * @return a {{@link String}} obtained by concatenating the textual
   *         representation of the elements in <code>ss</code> separated by
   *         <code>between</code> */
  public static SeparationSubject these(final short[] ¢) {
    return these(box.it(¢));
  }
  /** Separate a variable length list of arguments by a comma character.
   * @param < T > type of items
   * @param ¢ the objects to be separated. */
  @SafeVarargs public static <T> SeparationSubject these(final T... ¢) {
    return new SeparationSubject(¢);
  }
  static <T> void assertEquals(final String reason, final T t1, final T t2) {
    azzert.that(reason, t2, is(t1));
  }
  static <T> void assertEquals(final T t1, final T t2) {
    azzert.that(t2, is(t1));
  }
  static void assertFalse(final boolean ¢) {
    azzert.nay(¢);
  }
  static void assertFalse(final String reason, final boolean b) {
    azzert.nay(reason, b);
  }
  static <T> void assertNotEquals(final T t1, final T t2) {
    azzert.that(t2, is(t1));
  }
  static void assertTrue(final boolean ¢) {
    azzert.aye(¢);
  }
  static void assertTrue(final String reason, final boolean b) {
    azzert.aye(reason, b);
  }
  /** Separate elements of a given array of <code><b>int</b></code>s by a given
   * <code><b>char</b></code>
   * @param ¢ an array of elements to be separated
   * @return a {{@link String}} obtained by concatenating the textual
   *         representation of the elements in <code>is</code> separated by
   *         <code>between</code> */
  private static SeparationSubject these(final int[] ¢) {
    return these(box.it(¢));
  }

  /** Auxiliary class for fluent API.
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2016-09-10 */
  public static class SeparationSubject {
    /** Separate elements of a given {@link Iterable} collection by a given
     * {{@link String}}
     * @param ts an {@link Iterable} collection of elements to be separated
     * @param <T> type of elements in the {@link Iterable} collection parameter
     * @param between what should be used for separating these elements
     * @return a {{@link String}} obtained by concatenating the textual
     *         representation of the elements in <code>ts</code> separated by
     *         <code>between</code> */
    static <T> String by(final Iterable<? extends T> ts, final String between) {
      final Separator s = new Separator(between);
      final StringBuffer $ = new StringBuffer();
      for (final T ¢ : ts)
        $.append(s).append(¢);
      return as.string($);
    }
    /** Separate a list of elements by a given {{@link String}}
     * @param os what needs to be separated
     * @param between what should be used for separating these elements
     * @return a {{@link String}} obtained by concatenating the textual
     *         representation of the elements in <code>ts</code> separated by
     *         <code>between</code> */
    static String separateBy(final Iterable<?> os, final String between) {
      final Separator s = new Separator(between);
      final StringBuffer $ = new StringBuffer();
      for (final Object ¢ : os)
        $.append(s).append(¢);
      return as.string($);
    }
    private static String separateBy(final Object[] os, final String between) {
      final Separator s = new Separator(between);
      final StringBuffer $ = new StringBuffer();
      for (final Object ¢ : os)
        $.append(s).append(¢);
      return as.string($);
    }

    public final Iterable<?> os;

    public SeparationSubject(final Iterable<?> os) {
      this.os = os;
    }
    public SeparationSubject(final Object[] os) {
      this.os = as.list(os);
    }
    /** Separate elements of a given array of <code><b>boolean</b></code>s by a
     * given character
     * @param between what should be used for separating these elements
     * @return a concatenation of the newline separated
     *         {@link Object#toString()} representations of the elements of
     *         saved objects <code>between</code> */
    public String by(final char between) {
      return by(between + "");
    }
    /** Separate elements of a given array of <code><b>boolean</b></code>s by a
     * given {{@link String}}
     * @param between what should be used for separating these elements
     * @return a {{@link String}} obtained by concatenating the textual
     *         representation of the elements in <code>bs</code> separated by
     *         <code>between</code> */
    public String by(final String between) {
      return separateBy(os, between);
    }
    /** Separate a variable length list of arguments by a comma character.
     * @return a concatenation of the comma separated {@link Object#toString()}
     *         representations of the elements of saved objects */
    public String byCommas() {
      return by(COMMA);
    }
    /** Separate a variable length list of arguments by a dot character.
     * @return a concatenation of the dot separated {@link Object#toString()}
     *         representations of the elements of saved objects */
    public String byDots() {
      return separateBy(prune.whites(as.strings(os)), DOT);
    }
    /** Separate a variable length list of arguments by new lines.
     * @return a concatenation of the newline separated
     *         {@link Object#toString()} representations of the elements of
     *         saved objects */
    public String byNLs() {
      return separateBy(prune.whites(as.strings(os)), NL);
    }
    /** Separates the objects by nothing
     * @return separated text */
    public String byNothing() {
      return separateBy(prune.whites(as.strings(os)), "");
    }
    /** Separate a variable length list of arguments by a space character.
     * @return a concatenation of the comma separated {@link Object#toString()}
     *         representations of the elements of saved objects */
    public String bySpaces() {
      return separateBy(prune.whites(as.strings(os)), SPACE);
    }
  }

  @SuppressWarnings({ "static-method", "synthetic-access" }) //
  public static class TEST {
    private static final Function<String, String> quote = λ -> "'" + λ + "'";

    static <T> void assertEquals(final String reason, final T t1, final T t2) {
      azzert.that(reason, t2, is(t1));
    }
    @Test public final void asArrayBetweenChar() {
      azzert.that(separate.these(as.array("Hello", "World")).by(','), is("Hello,World"));
    }
    @Test public final void byArrayString() {
      azzert.that(separate.these(new String[] { "Hello", "World" }).by(", "), is("Hello, World"));
    }
    @Test public final void byArrayStringUsingLiterals() {
      azzert.that(separate.these(as.array("Hello", "World")).by(", "), is("Hello, World"));
    }
    @Test public final void byBooleanArrayChar() {
      azzert.that(separate.these(new boolean[] { true, false }).by(':'), is("true:false"));
    }
    @Test public final void byBooleanArrayString() {
      azzert.that(separate.these(new boolean[] { true, false }).by("; "), is("true; false"));
    }
    @Test public final void byByteArrayChar() {
      azzert.that(separate.these(new byte[] { 3, -5 }).by(':'), is("3:-5"));
    }
    @Test public final void byByteArrayString() {
      azzert.that(separate.these(new byte[] { -1, 2 }).by("; "), is("-1; 2"));
    }
    @Test public final void byCharArrayChar() {
      azzert.that(separate.these(new char[] { '3', 'x' }).by(':'), is("3:x"));
    }
    @Test public final void byCharArrayString() {
      azzert.that(separate.these(new char[] { 'a', 'x' }).by("; "), is("a; x"));
    }
    @Test public final void byCommasTypical() {
      azzert.that(separate.these("A", "B", "C").byCommas(), is("A,B,C"));
    }
    @Test public final void byDoubleArrayChar() {
      azzert.that(separate.these(new double[] { 3.3, 4.2 }).by(':'), is("3.3:4.2"));
    }
    @Test public final void byDoubleArrayString() {
      azzert.that(separate.these(new double[] { -1.0, 2.0 }).by("; "), is("-1.0; 2.0"));
    }
    @Test public final void byFloatArrayChar() {
      azzert.that(separate.these(new float[] { 3.3F, 4.2F }).by(':'), is("3.3:4.2"));
    }
    @Test public final void byFloatArrayString() {
      azzert.that(separate.these(new float[] { -1F, 2F }).by("; "), is("-1.0; 2.0"));
    }
    @Test public final void byFOfTIterableOfTChar() {
      azzert.that(separate.these(apply(λ -> "<" + λ + ">").to("A", "B")).by(' '), is("<A> <B>"));
    }
    @Test public final void byFOfTIterableOfTString() {
      azzert.that(separate.these(new Applicator<>(quote).to(as.list("Hello", "World"))).by(", "), is("'Hello', 'World'"));
    }
    @Test public final void byFOfTTArrayChar() {
      final Applicator<Object, String> f = new Applicator<>(λ -> "'" + λ + "'");
      assert f != null : "Function literals should never by null.";
      final Collection<String> c = as.list("Hello", "World");
      azzert.that(c.size(), is(2));
      final Iterable<String> ts = f.to(c);
      azzert.that(count.of(ts), is(2));
      azzert.that(separate.these(ts).by(' '), is("'Hello' 'World'"));
    }
    @Test public final void byFOfTTArrayString() {
      azzert.that(separate.these(apply(quote).to("Hello", "World")).by(", "), is("'Hello', 'World'"));
    }
    @Test public final void byIntArrayChar() {
      azzert.that(separate.these(new int[] { 3, 4 }).by(':'), is("3:4"));
    }
    @Test public final void byIntArrayString() {
      azzert.that(separate.these(new int[] { -1, 2 }).by("; "), is("-1; 2"));
    }
    @Test public final void byIterableOfChar() {
      azzert.that(separate.these(as.array("Hello", "World")).by(','), is("Hello,World"));
    }
    @Test public final void byIterableOfString() {
      azzert.that(separate.these(as.list("Hello", "World")).by(", "), is("Hello, World"));
    }
    @Test public final void byLongArrayChar() {
      azzert.that(separate.these(new long[] { 3, 4 }).by(':'), is("3:4"));
    }
    @Test public final void byLongArrayString() {
      azzert.that(separate.these(new long[] { -1L, 2L }).by("; "), is("-1; 2"));
    }
    @Test public final void byMapOfKeyValueStringString() {
      final Map<String, Integer> map = new TreeMap<>();
      map.put("One", box.it(1));
      map.put("Two", box.it(2));
      map.put("Three", box.it(3));
      map.put("Four", box.it(4));
      azzert.that(separate.these(map).by(", "), is("Four->4, One->1, Three->3, Two->2"));
    }
    @Test public final void byShortArrayChar() {
      azzert.that(separate.these(new short[] { 3, 4 }).by(':'), is("3:4"));
    }
    @Test public final void byShortArrayString() {
      azzert.that(separate.these(new short[] { (short) -1, (short) 2 }).by(": "), is("-1: 2"));
    }
    @Test public final void bySpacesEmptyl() {
      azzert.that(separate.these().bySpaces(), is(""));
    }
    @Test public final void bySpacesLengthLessThan2() {
      azzert.aye(separate.these().bySpaces().length() < 2);
    }
    @Test public final void bySpacesLengthLessThan3() {
      azzert.aye(separate.these().bySpaces().length() < 3);
    }
    @Test public final void bySpacesTypical() {
      azzert.that(separate.these("A", "B", "C").bySpaces(), is("A B C"));
    }
    @Test public final void byTArrayChar() {
      azzert.that(separate.these(new String[] { "Hello", "World" }).by(','), is("Hello,World"));
    }
    @Test public final void nlIterableOfString() {
      azzert.that(separate.these(as.list("Hello", "World")).byNLs(), is("Hello\nWorld"));
    }
    @Test public final void nlStringArray() {
      azzert.that(separate.these("Hello", "World").byNLs(), is("Hello\nWorld"));
    }
    @Test public final void separateByNoItemslPruneWhitesSpaceSeparated() {
      final SeparationSubject these = separate.these();
      assert these != null : null;
      final Iterable<?> os = these.os;
      assert os != null : null;
      azzert.aye(is.empty(os));
      final String[] ss = as.strings(os);
      assert ss != null : null;
      azzert.zero(ss.length);
      final String[] noWhites = prune.whites(ss);
      azzert.zero(noWhites.length);
      azzert.that(SeparationSubject.separateBy(noWhites, " "), is(""));
    }
    @Test public final void separateByNoItemslSpaceSeparated() {
      azzert.that(SeparationSubject.separateBy(separate.these().os, " "), is(""));
    }
    @Test public void separateBySpaceEmpty() {
      azzert.that(bySpaces(), is(""));
    }
    @Test public void separateBySpaceEmptyIterator() {
      azzert.that(separateBySpaces(an.empty.list()), is(""));
    }
    @Test public void separateBySpaceMultipleIterator() {
      azzert.that(separateBySpaces(as.list(new String[] { "X", "Y", "Z" })), is("X Y Z"));
    }
    @Test public void separateBySpaceOnIteator() {
      azzert.that(separateBySpaces(as.list(new String[] { "Hello", "World " })), is("Hello World "));
    }
    @Test public void separateBySpaceOnSingletonIteator() {
      azzert.that(separateBySpaces(iterable.singleton("Hello")), is("Hello"));
    }
    @Test public void separateBySpaceSimple() {
      azzert.that(bySpaces("A"), is("A"));
    }
    @Test public void separateBySpaceSingletonIterator() {
      azzert.that(separateBySpaces(iterable.singleton("X")), is("X"));
    }
    @Test public void separateBySpaceTwoStrings() {
      azzert.that(bySpaces("A", "B"), is("A B"));
    }
    @Test public final void spaceIsSpace() {
      azzert.that(SPACE + "", is(" "));
    }
    @Test public final void theseArraySize0() {
      azzert.that(count.of(separate.these(as.array()).os), is(0));
    }
    @Test public final void theseArraySize1() {
      azzert.that(count.of(separate.these(as.array("Rosebud")).os), is(1));
    }
    @Test public final void theseArraySize2() {
      azzert.that(count.of(separate.these(as.array("Hello", "World")).os), is(2));
    }
    @Test public final void theseArraySize3() {
      azzert.that(count.of(separate.these(as.array("A", "B", "C")).os), is(3));
    }
    @Test public final void theseFromOneItem() {
      azzert.that(count.of(separate.these(as.list("Rosebud")).os), is(1));
    }
    @Test public final void theseFromThreeItems() {
      azzert.that(count.of(separate.these(as.list("A", "B", "C")).os), is(3));
    }
    @Test public final void theseFromTwoItems() {
      azzert.that(count.of(separate.these(as.list("Hello", "World")).os), is(2));
    }
    @Test public final void theseFromZeroItems() {
      azzert.that(count.of(separate.these(as.list((Double) null)).os), is(0));
    }
    @Test public final void theseOfNoItemsl() {
      azzert.aye(is.empty(separate.these(new String[] {}).os));
    }
    @Test public final void theseOfNoItemslSpaceSeparated() {
      azzert.that(separate.these(new String[] {}).bySpaces(), is(""));
    }
  }
}
