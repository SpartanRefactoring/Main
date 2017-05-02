/**
 *
 */
package il.org.spartan.utils;

import static nano.ly.box.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.utils.Separate.*;

@SuppressWarnings("static-method") //
public class SeparateTest {
  private static <T> Collection<T> makeCollection(final T... ts) {
    final ArrayList<T> $ = new ArrayList<>();
    for (final T ¢ : ts)
      $.add(¢);
    return $;
  }

  @Test public final void testByBooleanArrayChar() {
    assertEquals("true:false", Separate.by(new boolean[] { true, false }, ':'));
  }

  @Test public final void testByBooleanArrayString() {
    assertEquals("true; false", Separate.by(new boolean[] { true, false }, "; "));
  }

  @Test public final void testByByteArrayChar() {
    assertEquals("3:-5", Separate.by(new byte[] { 3, -5 }, ':'));
  }

  @Test public final void testByByteArrayString() {
    assertEquals("-1; 2", Separate.by(new byte[] { -1, 2 }, "; "));
  }

  @Test public final void testByCharArrayChar() {
    assertEquals("3:x", Separate.by(new char[] { '3', 'x' }, ':'));
  }

  @Test public final void testByCharArrayString() {
    assertEquals("a; x", Separate.by(new char[] { 'a', 'x' }, "; "));
  }

  @Test public final void testByCommas() {
    assertEquals("A,B,C", Separate.byCommas("A", "B", "C"));
  }

  @Test public final void testByDoubleArrayChar() {
    assertEquals("3.3:4.2", Separate.by(new double[] { 3.3, 4.2 }, ':'));
  }

  @Test public final void testByDoubleArrayString() {
    assertEquals("-1.0; 2.0", Separate.by(new double[] { -1.0, 2.0 }, "; "));
  }

  @Test public final void testByFloatArrayChar() {
    assertEquals("3.3:4.2", Separate.by(new float[] { 3.3F, 4.2F }, ':'));
  }

  @Test public final void testByFloatArrayString() {
    assertEquals("-1.0; 2.0", Separate.by(new float[] { -1F, 2F }, "; "));
  }

  @Test public final void testByFOfTIterableOfTChar() {
    assertEquals("<A> <B>", Separate.by((F<String>) λ -> "<" + λ + '>', new String[] { "A", "B" }, ' '));
  }

  @Test public final void testByFOfTIterableOfTString() {
    assertEquals("'Hello', 'World'", Separate.by((F<String>) λ -> "'" + λ + '\'', makeCollection("Hello", "World"), ", "));
  }

  @Test public final void testByFOfTTArrayChar() {
    assertEquals("'Hello' 'World'", Separate.by((F<String>) λ -> "'" + λ + '\'', makeCollection("Hello", "World"), ' '));
  }

  @Test public final void testByFOfTTArrayString() {
    assertEquals("'Hello', 'World'", Separate.by((F<String>) λ -> "'" + λ + '\'', new String[] { "Hello", "World" }, ", "));
  }

  @Test public final void testByIntArrayChar() {
    assertEquals("3:4", Separate.by(new int[] { 3, 4 }, ':'));
  }

  @Test public final void testByIntArrayString() {
    assertEquals("-1; 2", Separate.by(new int[] { -1, 2 }, "; "));
  }

  @Test public final void testByIterableOfTChar() {
    assertEquals("Hello,World", Separate.by(makeCollection("Hello", "World"), ','));
  }

  @Test public final void testByIterableOfTString() {
    assertEquals("Hello, World", Separate.by(makeCollection("Hello", "World"), ", "));
  }

  @Test public final void testByLongArrayChar() {
    assertEquals("3:4", Separate.by(new long[] { 3, 4 }, ':'));
  }

  @Test public final void testByLongArrayString() {
    assertEquals("-1; 2", Separate.by(new long[] { -1L, 2L }, "; "));
  }

  @Test public final void testByMapOfKeyValueStringString() {
    final Map<String, Integer> map = new TreeMap<>();
    map.put("One", box(1));
    map.put("Two", box(2));
    map.put("Three", box(3));
    map.put("Four", box(4));
    assertEquals("Four->4, One->1, Three->3, Two->2", Separate.by(map, ", ", "->"));
  }

  @Test public final void testByShortArrayChar() {
    assertEquals("3:4", Separate.by(new short[] { 3, 4 }, ':'));
  }

  @Test public final void testByShortArrayString() {
    assertEquals("-1; 2", Separate.by(new short[] { (short) -1, (short) 2 }, "; "));
  }

  @Test public final void testBySpaces() {
    assertEquals("A B C", Separate.bySpaces("A", "B", "C"));
  }

  @Test public final void testByTArrayChar() {
    assertEquals("Hello,World", Separate.by(new String[] { "Hello", "World" }, ','));
  }

  @Test public final void testByTArrayString() {
    assertEquals("Hello, World", Separate.by(new String[] { "Hello", "World" }, ", "));
  }

  @Test public final void testNlIterableOfString() {
    assertEquals("Hello\nWorld", Separate.nl(makeCollection("Hello", "World")));
  }

  @Test public final void testNlStringArray() {
    assertEquals("Hello\nWorld", Separate.nl("Hello", "World"));
  }
}