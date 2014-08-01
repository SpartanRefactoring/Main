/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package org.spartan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.spartan.Assert.assertEquals;
import static org.spartan.Assert.assertZero;
import static org.spartan.__.apply;
import static org.spartan.__.cantBeNull;

import java.util.*;
import java.util.function.Function;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.spartan.__.Applicator;
import org.spartan.iterables.Iterables;

/**
 * A utility class providing library functions that take an array or a
 * collection, and return a {@link String} composed by the elements of this
 * collection, separated by a given {@link String} or <code><b>char</b></code>.
 *
 * @author Yossi Gil
 * @since 07/08/2008
 */
public enum Separate {
	;
	/** The comma character */
	public static final String COMMA = ",";
	/** The dot character */
	public static final String DOT = ".";
	/** The Unix line separator character */
	public static final String NL = "\n";
	/** The space character */
	public static final String SPACE = " ";
	/**
	 * A simple program demonstrating the use of this class. This program prints a
	 * comma separated list of its arguments, where special characters in each
	 * argument are escaped prior to printing.
	 *
	 * @param args list of the command line arguments.
	 */
	public static void main(final String[] args) {
		System.out.println("Arguments are: " + Separate.these(args).by(", "));
	}
	/**
	 * Separates a sequence of strings by {@link #SPACE} characters
	 *
	 * @param $ what needs to be separated
	 * @return the parameters, separated by {@link #SPACE}
	 */
	public static String bySpaces(final String... $) {
		return separateBySpaces(As.iterable($));
	}
	/**
	 * Separates an {@link Iterable} strings by {@link #SPACE} characters
	 *
	 * @param $ what needs to be separated
	 * @return the parameters, separated by {@link #SPACE}
	 */
	public static String separateBySpaces(final Iterable<String> $) {
		return As.string(separateBySpaces($.iterator()));
	}
	/**
	 * Separates an {@link Iterable} strings (specified by an {@link Iterator}
	 * over it by {@link #SPACE} characters
	 *
	 * @param ss what needs to be separated
	 * @return the parameters, separated by {@link #SPACE}
	 */
	public static String separateBySpaces(final Iterator<String> ss) {
		final StringBuilder $ = new StringBuilder();
		final Separator s = new Separator(SPACE);
		while (ss.hasNext())
			$.append(s).append(ss.next());
		return As.string($);
	}
	/**
	 * Factory method for generating a {@link SeparationSubject}, to be used
	 * further for actual separation.
	 *
	 * @return an empty {@link SeparationSubject}
	 */
	public static SeparationSubject these() {
		return new SeparationSubject(new String[] {});
	}
	/**
	 * Separate elements of a given array of <code><b>boolean</b></code>s by a
	 * given <code><b>char</b></code>
	 *
	 * @param bs an array of elements to be separated
	 * @return a {@String} obtained by concatenating the textual
	 *         representation of the elements in <code>bs</code> separated by
	 *         <code>between</code>
	 */
	public static SeparationSubject these(final boolean[] bs) {
		return these(Box.it(bs));
	}
	/**
	 * Separate elements of a given array of <code><b>byte</b></code>s by a given
	 * <code><b>char</b></code>
	 *
	 * @param bs an array of elements to be separated
	 * @return a {@String} obtained by concatenating the textual
	 *         representation of the elements in <code>bs</code> separated by
	 *         <code>between</code>
	 */
	public static SeparationSubject these(final byte[] bs) {
		return these(Box.it(bs));
	}
	/**
	 * Separate elements of a given array of <code><b>char</b></code>s by a given
	 * <code><b>char</b></code>
	 *
	 * @param cs an array of elements to be separated
	 * @return a {@String} obtained by concatenating the textual
	 *         representation of the elements in <code>cs</code> separated by
	 *         <code>between</code>
	 */
	public static SeparationSubject these(final char[] cs) {
		return these(Box.it(cs));
	}
	/**
	 * Separate elements of a given array of <code><b>double</b></code>s by a
	 * given <code><b>char</b></code>
	 *
	 * @param ds an array of elements to be separated
	 * @return a {@String} obtained by concatenating the textual
	 *         representation of the elements in <code>ds</code> separated by
	 *         <code>between</code>
	 */
	public static SeparationSubject these(final double[] ds) {
		return these(Box.it(ds));
	}
	/**
	 * Separate elements of a given array of <code><b>float</b></code>s by a given
	 * <code><b>char</b></code>
	 *
	 * @param fs an array of elements to be separated
	 * @return a {@String} obtained by concatenating the textual
	 *         representation of the elements in <code>fs</code> separated by
	 *         <code>between</code>
	 */
	public static SeparationSubject these(final float[] fs) {
		return these(Box.it(fs));
	}
	/**
	 * Separate elements of a given array of <code><b>int</b></code>s by a given
	 * <code><b>char</b></code>
	 *
	 * @param is an array of elements to be separated
	 * @return a {@String} obtained by concatenating the textual
	 *         representation of the elements in <code>is</code> separated by
	 *         <code>between</code>
	 */
	private static SeparationSubject these(final int[] is) {
		return these(Box.it(is));
	}
	/**
	 * Separate a variable length list of arguments by a comma character.
	 *
	 * @param os the objects to be separated.
	 */
	public static SeparationSubject these(final Iterable<? extends Object> os) {
		return new SeparationSubject(os);
	}
	/**
	 * Separate elements of a given array of <code><b>long</b></code>s by a given
	 * <code><b>char</b></code>
	 *
	 * @param ls an array of elements to be separated
	 * @return a {@link String} obtained by concatenating the textual
	 *         representation of the elements in <code>ls</code> separated by
	 *         <code>between</code>
	 */
	public static SeparationSubject these(final long[] ls) {
		return these(Box.it(ls));
	}
	/**
	 * A simple minded separation of members of a {@link Map} data type.
	 *
	 * @param <Key> type of elements serving as keys of the map.
	 * @param <Value> type of elements serving as values of the map.
	 * @param map a non-<code><b>null</b></code> {@link Map} objects whose entries
	 *          are to be separated.
	 * @return a concatenation of all map entries, separated by
	 *         <code>separator</code>, and where the key of each entry is
	 *         separated from the value by <code>arrow</code>.
	 */
	public static <Key, Value> SeparationSubject these(final Map<Key, Value> map) {
		cantBeNull(map);
		final List<Object> $ = new ArrayList<>();
		for (final Key k : map.keySet())
			$.add(k + "->" + map.get(k));
		return new SeparationSubject($);
	}
	/**
	 * Separate elements of a given array of <code><b>short</b></code>s by a given
	 * <code><b>char</b></code>
	 *
	 * @param ss an array of elements to be separated
	 * @return a {@String} obtained by concatenating the textual
	 *         representation of the elements in <code>ss</code> separated by
	 *         <code>between</code>
	 */
	public static SeparationSubject these(final short[] ss) {
		return these(Box.it(ss));
	}
	/**
	 * Separate a variable length list of arguments by a comma character.
	 *
	 * @param <T> type of items
	 * @param ts the objects to be separated.
	 */
	@SafeVarargs public static <T> SeparationSubject these(final T... ts) {
		return new SeparationSubject(ts);
	}
	public static class SeparationSubject {

		/**
		 * Separate elements of a given {@link Iterable} collection by a given
		 * {@String}
		 *
		 * @param ts an {@link Iterable} collection of elements to be separated
		 * @param <T> type of elements in the {@link Iterable} collection parameter
		 * @param between what should be used for separating these elements
		 * @return a {@String} obtained by concatenating the textual
		 *         representation of the elements in <code>ts</code> separated by
		 *         <code>between</code>
		 */
		private static <T> String by(final Iterable<? extends T> ts, final String between) {
			final Separator s = new Separator(between);
			final StringBuffer $ = new StringBuffer();
			for (final T t : ts)
				$.append(s).append(t);
			return As.string($);
		}
		/**
		 * Separate a list of elements by a given {@String}
		 *
		 * @param os what needs to be separated
		 * @param between what should be used for separating these elements
		 * @return a {@String} obtained by concatenating the textual
		 *         representation of the elements in <code>ts</code> separated by
		 *         <code>between</code>
		 */
		private static String separateBy(final Iterable<? extends Object> os, final String between) {
			final Separator s = new Separator(between);
			final StringBuffer $ = new StringBuffer();
			for (final Object o : os)
				$.append(s).append(o);
			return As.string($);
		}
		private static String separateBy(final Object[] os, final String between) {
			final Separator s = new Separator(between);
			final StringBuffer $ = new StringBuffer();
			for (final Object o : os)
				$.append(s).append(o);
			return As.string($);
		}
		public final Iterable<? extends Object> os;
		public SeparationSubject(final Iterable<? extends Object> os) {
			this.os = os;
		}
		public SeparationSubject(final Object[] os) {
			this.os = As.iterable(os);
		}
		/**
		 * Separate elements of a given array of <code><b>boolean</b></code>s by a
		 * given character
		 *
		 * @param between what should be used for separating these elements
		 * @return a concatenation of the newline separated
		 *         {@link Object#toString()} representations of the elements of
		 *         saved objects <code>between</code>
		 */
		public String by(final char between) {
			return by("" + between);
		}
		/**
		 * Separate elements of a given array of <code><b>boolean</b></code>s by a
		 * given {@String}
		 *
		 * @param between what should be used for separating these elements
		 * @return a {@String} obtained by concatenating the textual
		 *         representation of the elements in <code>bs</code> separated by
		 *         <code>between</code>
		 */
		public String by(final String between) {
			return separateBy(os, between);
		}
		/**
		 * Separate a variable length list of arguments by a comma character.
		 *
		 * @return a concatenation of the comma separated {@link Object#toString()}
		 *         representations of the elements of saved objects
		 */
		public String byCommas() {
			return by(COMMA);
		}
		/**
		 * Separate a variable length list of arguments by a dot character.
		 *
		 * @return a concatenation of the dot separated {@link Object#toString()}
		 *         representations of the elements of saved objects
		 */
		public String byDots() {
			return separateBy(Prune.whites(As.strings(os)), DOT);
		}
		/**
		 * Separate a variable length list of arguments by new lines.
		 *
		 * @return a concatenation of the newline separated
		 *         {@link Object#toString()} representations of the elements of
		 *         saved objects
		 */
		public String byNLs() {
			return separateBy(Prune.whites(As.strings(os)), NL);
		}
		/**
		 * Separate a variable length list of arguments by a space character.
		 *
		 * @return a concatenation of the comma separated {@link Object#toString()}
		 *         representations of the elements of saved objects
		 */
		public String bySpaces() {
			return separateBy(Prune.whites(As.strings(os)), SPACE);
		}
		/**
		 * Separates the objects in some order
		 *
		 * @return the
		 */
		public String byNothing() {
			return separateBy(Prune.whites(As.strings(os)), "");
		}
	}
	@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
	@SuppressWarnings({ "static-method", "javadoc", "synthetic-access" })//
	public static class TEST {
		private static final Function<Object, String> quote = t -> "'" + t + "'";
		@Test public final void asArrayBetweenChar() {
			assertEquals("Hello,World", Separate.these(As.array("Hello", "World")).by(','));
		}
		@Test public final void byArrayString() {
			assertEquals("Hello, World", Separate.these(new String[] { "Hello", "World" }).by(", "));
		}
		@Test public final void byArrayStringUsingLiterals() {
			assertEquals("Hello, World", Separate.these(As.array("Hello", "World")).by(", "));
		}
		@Test public final void byBooleanArrayChar() {
			assertEquals("true:false", Separate.these(new boolean[] { true, false }).by(':'));
		}
		@Test public final void byBooleanArrayString() {
			assertEquals("true; false", Separate.these(new boolean[] { true, false }).by("; "));
		}
		@Test public final void byByteArrayChar() {
			assertEquals("3:-5", Separate.these(new byte[] { 3, -5 }).by(':'));
		}
		@Test public final void byByteArrayString() {
			assertEquals("-1; 2", Separate.these(new byte[] { -1, 2 }).by("; "));
		}
		@Test public final void byCharArrayChar() {
			assertEquals("3:x", Separate.these(new char[] { '3', 'x' }).by(':'));
		}
		@Test public final void byCharArrayString() {
			assertEquals("a; x", Separate.these(new char[] { 'a', 'x' }).by("; "));
		}
		@Test public final void byCommasTypical() {
			assertEquals("A,B,C", Separate.these("A", "B", "C").byCommas());
		}
		@Test public final void byDoubleArrayChar() {
			assertEquals("3.3:4.2", Separate.these(new double[] { 3.3, 4.2 }).by(':'));
		}
		@Test public final void byDoubleArrayString() {
			assertEquals("-1.0; 2.0", Separate.these(new double[] { -1.0, 2.0 }).by("; "));
		}
		@Test public final void byFloatArrayChar() {
			assertEquals("3.3:4.2", Separate.these(new float[] { 3.3F, 4.2F }).by(':'));
		}
		@Test public final void byFloatArrayString() {
			assertEquals("-1.0; 2.0", Separate.these(new float[] { -1F, 2F }).by("; "));
		}
		@Test public final void byFOfTIterableOfTChar() {
			assertEquals("<A> <B>", Separate.these(apply(a -> "<" + a + ">").to("A", "B")).by(' '));
		}
		@Test public final void byFOfTIterableOfTString() {
			assertEquals("'Hello', 'World'", //
					Separate.these(new Applicator<>(quote).to(Arrays.asList("Hello", "World"))).by(", "));
		}
		@Test public final void byFOfTTArrayChar() {
			final Applicator<Object, String> f = new Applicator<>(a -> "'" + a + "'");
			assertNotNull("Function literals should never by null.", f);
			final Collection<String> c = Arrays.asList("Hello", "World");
			assertEquals(2, c.size());
			final Iterable<String> ts = f.to(c);
			assertEquals(2, Iterables.count(ts));
			assertEquals("'Hello' 'World'", Separate.these(ts).by(' '));
		}
		@Test public final void byFOfTTArrayString() {
			assertEquals("'Hello', 'World'", Separate.these(apply(quote).to("Hello", "World")).by(", "));
		}
		@Test public final void byIntArrayChar() {
			assertEquals("3:4", Separate.these(new int[] { 3, 4 }).by(':'));
		}
		@Test public final void byIntArrayString() {
			assertEquals("-1; 2", Separate.these(new int[] { -1, 2 }).by("; "));
		}
		@Test public final void byIterableOfChar() {
			assertEquals("Hello,World", Separate.these(As.array("Hello", "World")).by(','));
		}
		@Test public final void byIterableOfString() {
			assertEquals("Hello, World", Separate.these(Arrays.asList("Hello", "World")).by(", "));
		}
		@Test public final void byLongArrayChar() {
			assertEquals("3:4", Separate.these(new long[] { 3, 4 }).by(':'));
		}
		@Test public final void byLongArrayString() {
			assertEquals("-1; 2", Separate.these(new long[] { -1L, 2L }).by("; "));
		}
		@Test public final void byMapOfKeyValueStringString() {
			final Map<String, Integer> map = new TreeMap<>();
			map.put("One", Box.it(1));
			map.put("Two", Box.it(2));
			map.put("Three", Box.it(3));
			map.put("Four", Box.it(4));
			assertEquals("Four->4, One->1, Three->3, Two->2", Separate.these(map).by(", "));
		}
		@Test public final void byShortArrayChar() {
			assertEquals("3:4", Separate.these(new short[] { 3, 4 }).by(':'));
		}
		@Test public final void byShortArrayString() {
			assertEquals("-1: 2", Separate.these(new short[] { (short) -1, (short) 2 }).by(": "));
		}
		@Test public final void bySpacesEmptyl() {
			assertEquals("", Separate.these().bySpaces());
		}
		@Test public final void bySpacesLengthLessThan2() {
			assertTrue(Separate.these().bySpaces().length() < 2);
		}
		@Test public final void bySpacesLengthLessThan3() {
			assertTrue(Separate.these().bySpaces().length() < 3);
		}
		@Test public final void bySpacesTypical() {
			assertEquals("A B C", Separate.these("A", "B", "C").bySpaces());
		}
		@Test public final void byTArrayChar() {
			assertEquals("Hello,World", Separate.these(new String[] { "Hello", "World" }).by(','));
		}
		@Test public final void nlIterableOfString() {
			assertEquals("Hello\nWorld", Separate.these(Arrays.asList("Hello", "World")).byNLs());
		}
		@Test public final void nlStringArray() {
			assertEquals("Hello\nWorld", Separate.these("Hello", "World").byNLs());
		}
		@Test public final void separateByNoItemslPruneWhitesSpaceSeparated() {
			final SeparationSubject these = Separate.these();
			assertNotNull(these);
			final Iterable<? extends Object> os = these.os;
			assertNotNull(os);
			assertTrue(Iterables.isEmpty(os));
			final String[] ss = As.strings(os);
			assertNotNull(ss);
			assertZero(ss.length);
			final String[] noWhites = Prune.whites(ss);
			assertZero(noWhites.length);
			assertEquals("", SeparationSubject.separateBy(noWhites, " "));
		}
		@Test public final void separateByNoItemslSpaceSeparated() {
			assertEquals("", SeparationSubject.separateBy(Separate.these().os, " "));
		}
		@Test public void separateBySpaceEmpty() {
			assertEquals("", bySpaces());
		}
		@Test public void separateBySpaceEmptyIterator() {
			assertEquals("", separateBySpaces(Iterables.<String> empty()));
		}
		@Test public void separateBySpaceMultipleIterator() {
			assertEquals("X Y Z", separateBySpaces(As.iterable("X", "Y", "Z")));
		}
		@Test public void separateBySpaceOnIteator() {
			assertEquals("Hello World ", separateBySpaces(As.iterable("Hello", "World ")));
		}
		@Test public void separateBySpaceOnSingletonIteator() {
			assertEquals("Hello", separateBySpaces(Iterables.singleton("Hello")));
		}
		@Test public void separateBySpaceSimple() {
			assertEquals("A", bySpaces("A"));
		}
		@Test public void separateBySpaceSingletonIterator() {
			assertEquals("X", separateBySpaces(Iterables.singleton("X")));
		}
		@Test public void separateBySpaceTwoStrings() {
			assertEquals("A B", bySpaces("A", "B"));
		}
		@Test public final void spaceIsSpace() {
			assertEquals(" ", "" + SPACE);
		}
		@Test public final void theseArraySize0() {
			assertEquals(0, Iterables.count(Separate.these(As.array()).os));
		}
		@Test public final void theseArraySize1() {
			assertEquals(1, Iterables.count(Separate.these(As.array("Rosebud")).os));
		}
		@Test public final void theseArraySize2() {
			assertEquals(2, Iterables.count(Separate.these(As.array("Hello", "World")).os));
		}
		@Test public final void theseArraySize3() {
			assertEquals(3, Iterables.count(Separate.these(As.array("A", "B", "C")).os));
		}
		@Test public final void theseFromOneItem() {
			assertEquals(1, Iterables.count(Separate.these(Arrays.asList("Rosebud")).os));
		}
		@Test public final void theseFromThreeItems() {
			assertEquals(3, Iterables.count(Separate.these(Arrays.asList("A", "B", "C")).os));
		}
		@Test public final void theseFromTwoItems() {
			assertEquals(2, Iterables.count(Separate.these(Arrays.asList("Hello", "World")).os));
		}
		@Test public final void theseFromZeroItems() {
			assertEquals(0, Iterables.count(Separate.these(Arrays.asList()).os));
		}
		@Test public final void theseOfNoItemsl() {
			assertTrue(Iterables.isEmpty(Separate.these(new String[] {}).os));
		}
		@Test public final void theseOfNoItemslSpaceSeparated() {
			assertEquals("", Separate.these(new String[] {}).bySpaces());
		}
	}
}
