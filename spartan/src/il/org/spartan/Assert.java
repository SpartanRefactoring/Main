/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;
import static il.org.spartan.__.cantBeNull;

import org.hamcrest.Matcher;
@SuppressWarnings({ "javadoc" })//
public class Assert extends org.junit.Assert {
	public static void assertEquals(final int expected, final int actual) {
		assertEquals(Box.it(expected), Box.it(actual));
	}
	public static void assertPositive(final int i) {
		assertTrue(i > 0);
	}
	public static void assertZero(final int i) {
		assertEquals(0, i);
	}
	public static void assertThat(final byte b, final Matcher<Byte> m) {
		org.junit.Assert.assertThat(new Byte(b), cantBeNull(m));
	}
	public static void assertThat(final boolean b, final Matcher<Boolean> m) {
		org.junit.Assert.assertThat(new Boolean(b), cantBeNull(m));
	}
	public static void assertThat(final char c, final Matcher<Character> m) {
		org.junit.Assert.assertThat(new Character(c), cantBeNull(m));
	}
	public static void assertThat(final double d, final Matcher<Double> m) {
		org.junit.Assert.assertThat(new Double(d), cantBeNull(m));
	}
	public static void assertThat(final float f, final Matcher<Float> m) {
		org.junit.Assert.assertThat(new Float(f), cantBeNull(m));
	}
	public static void assertThat(final int i, final Matcher<Integer> m) {
		org.junit.Assert.assertThat(new Integer(i), cantBeNull(m));
	}
	public static void assertThat(final long l, final Matcher<Long> m) {
		org.junit.Assert.assertThat(new Long(l), cantBeNull(m));
	}
	public static void assertThat(final short s, final Matcher<Short> m) {
		org.junit.Assert.assertThat(new Short(s), cantBeNull(m));
	}
	public static Matcher<Byte> not(final byte b) {
		return cantBeNull(org.hamcrest.core.IsNot.not(new Byte(b)));
	}
	public static Matcher<Boolean> not(final boolean b) {
		return cantBeNull(org.hamcrest.core.IsNot.not(new Boolean(b)));
	}
	public static Matcher<Character> not(final char i) {
		return cantBeNull(org.hamcrest.core.IsNot.not(new Character(i)));
	}
	public static Matcher<Double> not(final double d) {
		return cantBeNull(org.hamcrest.core.IsNot.not(new Double(d)));
	}
	public static Matcher<Float> not(final float f) {
		return cantBeNull(org.hamcrest.core.IsNot.not(new Float(f)));
	}
	public static Matcher<Integer> not(final int i) {
		return cantBeNull(org.hamcrest.core.IsNot.not(new Integer(i)));
	}
	public static Matcher<Long> not(final long i) {
		return cantBeNull(org.hamcrest.core.IsNot.not(new Long(i)));
	}
	public static Matcher<Short> not(final short s) {
		return cantBeNull(org.hamcrest.core.IsNot.not(new Short(s)));
	}
	public static Matcher<Byte> is(final byte b) {
		return cantBeNull(org.hamcrest.core.Is.is(new Byte(b)));
	}
	public static Matcher<Boolean> is(final boolean b) {
		return cantBeNull(org.hamcrest.core.Is.is(new Boolean(b)));
	}
	public static Matcher<Character> is(final char i) {
		return cantBeNull(org.hamcrest.core.Is.is(new Character(i)));
	}
	public static Matcher<Double> is(final double d) {
		return cantBeNull(org.hamcrest.core.Is.is(new Double(d)));
	}
	public static Matcher<Float> is(final float f) {
		return cantBeNull(org.hamcrest.core.Is.is(new Float(f)));
	}
	public static Matcher<Integer> is(final int i) {
		return cantBeNull(org.hamcrest.core.Is.is(new Integer(i)));
	}
	public static Matcher<Long> is(final long i) {
		return cantBeNull(org.hamcrest.core.Is.is(new Long(i)));
	}
	public static Matcher<Short> is(final short s) {
		return cantBeNull(org.hamcrest.core.Is.is(new Short(s)));
	}
}
