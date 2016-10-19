/**
 *
 */
package il.org.spartan.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.Box.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.utils.Separate.*;

@SuppressWarnings({ "static-method", "javadoc" }) //
public class SeparateTest {
  private static <T> Collection<T> makeCollection(final T... ts) {
    final ArrayList<T> $ = new ArrayList<>();
    for (final T ¢ : ts)
      $.add(¢);
    return $;
  }

  @Test public final void testByBooleanArrayChar() {
    azzert.assertThat("", Separate.by(new boolean[] { true, false }, ':'), is("true:false"));
  }

  @Test public final void testByBooleanArrayString() {
    azzert.assertThat("", Separate.by(new boolean[] { true, false }, "; "), is("true; false"));
  }

  @Test public final void testByByteArrayChar() {
    azzert.assertThat("", Separate.by(new byte[] { 3, -5 }, ':'), is("3:-5"));
  }

  @Test public final void testByByteArrayString() {
    azzert.assertThat("", Separate.by(new byte[] { -1, 2 }, "; "), is("-1; 2"));
  }

  @Test public final void testByCharArrayChar() {
    azzert.assertThat("", Separate.by(new char[] { '3', 'x' }, ':'), is("3:x"));
  }

  @Test public final void testByCharArrayString() {
    azzert.assertThat("", Separate.by(new char[] { 'a', 'x' }, "; "), is("a; x"));
  }

  @Test public final void testByCommas() {
    azzert.assertThat("", Separate.byCommas("A", "B", "C"), is("A,B,C"));
  }

  @Test public final void testByDoubleArrayChar() {
    azzert.assertThat("", Separate.by(new double[] { 3.3, 4.2 }, ':'), is("3.3:4.2"));
  }

  @Test public final void testByDoubleArrayString() {
    azzert.assertThat("", Separate.by(new double[] { -1.0, 2.0 }, "; "), is("-1.0; 2.0"));
  }

  @Test public final void testByFloatArrayChar() {
    azzert.assertThat("", Separate.by(new float[] { 3.3F, 4.2F }, ':'), is("3.3:4.2"));
  }

  @Test public final void testByFloatArrayString() {
    azzert.assertThat("", Separate.by(new float[] { -1F, 2F }, "; "), is("-1.0; 2.0"));
  }

  @Test public final void testByFOfTIterableOfTChar() {
    azzert.assertThat("", Separate.by((F<String>) a -> "<" + a + '>', new String[] { "A", "B" }, ' '), is("<A> <B>"));
  }

  @Test public final void testByFOfTIterableOfTString() {
    azzert.assertThat("", Separate.by((F<String>) a -> "'" + a + '\'', makeCollection("Hello", "World"), ", "), is("'Hello', 'World'"));
  }

  @Test public final void testByFOfTTArrayChar() {
    azzert.assertThat("", Separate.by((F<String>) a -> "'" + a + '\'', makeCollection("Hello", "World"), ' '), is("'Hello' 'World'"));
  }

  @Test public final void testByFOfTTArrayString() {
    azzert.assertThat("", Separate.by((F<String>) a -> "'" + a + '\'', new String[] { "Hello", "World" }, ", "), is("'Hello', 'World'"));
  }

  @Test public final void testByIntArrayChar() {
    azzert.assertThat("", Separate.by(new int[] { 3, 4 }, ':'), is("3:4"));
  }

  @Test public final void testByIntArrayString() {
    azzert.assertThat("", Separate.by(new int[] { -1, 2 }, "; "), is("-1; 2"));
  }

  @Test public final void testByIterableOfTChar() {
    azzert.assertThat("", Separate.by(makeCollection("Hello", "World"), ','), is("Hello,World"));
  }

  @Test public final void testByIterableOfTString() {
    azzert.assertThat("", Separate.by(makeCollection("Hello", "World"), ", "), is("Hello, World"));
  }

  @Test public final void testByLongArrayChar() {
    azzert.assertThat("", Separate.by(new long[] { 3, 4 }, ':'), is("3:4"));
  }

  @Test public final void testByLongArrayString() {
    azzert.assertThat("", Separate.by(new long[] { -1L, 2L }, "; "), is("-1; 2"));
  }

  @Test public final void testByMapOfKeyValueStringString() {
    final Map<String, Integer> map = new TreeMap<>();
    map.put("One", box(1));
    map.put("Two", box(2));
    map.put("Three", box(3));
    map.put("Four", box(4));
    azzert.assertThat("", Separate.by(map, ", ", "->"), is("Four->4, One->1, Three->3, Two->2"));
  }

  @Test public final void testByShortArrayChar() {
    azzert.assertThat("", Separate.by(new short[] { 3, 4 }, ':'), is("3:4"));
  }

  @Test public final void testByShortArrayString() {
    azzert.assertThat("", Separate.by(new short[] { (short) -1, (short) 2 }, "; "), is("-1; 2"));
  }

  @Test public final void testBySpaces() {
    azzert.assertThat("", Separate.bySpaces("A", "B", "C"), is("A B C"));
  }

  @Test public final void testByTArrayChar() {
    azzert.assertThat("", Separate.by(new String[] { "Hello", "World" }, ','), is("Hello,World"));
  }

  @Test public final void testByTArrayString() {
    azzert.assertThat("", Separate.by(new String[] { "Hello", "World" }, ", "), is("Hello, World"));
  }

  @Test public final void testNlIterableOfString() {
    azzert.assertThat("", Separate.nl(makeCollection("Hello", "World")), is("Hello\nWorld"));
  }

  @Test public final void testNlStringArray() {
    azzert.assertThat("", Separate.nl("Hello", "World"), is("Hello\nWorld"));
  }
}