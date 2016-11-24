package il.org.spartan.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.Box.*;

import java.util.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings({ "static-method", "javadoc" }) //
public class SeparateTest {
  @NotNull private static <T> Collection<T> makeCollection(@NotNull final T... ts) {
    @NotNull final ArrayList<T> $ = new ArrayList<>();
    for (final T ¢ : ts)
      $.add(¢);
    return $;
  }

  @Test public final void testByBooleanArrayChar() {
    azzert.that(Separate.by(new boolean[] { true, false }, ':'), is("true:false"));
  }

  @Test public final void testByBooleanArrayString() {
    azzert.that(Separate.by(new boolean[] { true, false }, "; "), is("true; false"));
  }

  @Test public final void testByByteArrayChar() {
    azzert.that(Separate.by(new byte[] { 3, -5 }, ':'), is("3:-5"));
  }

  @Test public final void testByByteArrayString() {
    azzert.that(Separate.by(new byte[] { -1, 2 }, "; "), is("-1; 2"));
  }

  @Test public final void testByCharArrayChar() {
    azzert.that(Separate.by(new char[] { '3', 'x' }, ':'), is("3:x"));
  }

  @Test public final void testByCharArrayString() {
    azzert.that(Separate.by(new char[] { 'a', 'x' }, "; "), is("a; x"));
  }

  @Test public final void testByCommas() {
    azzert.that(Separate.byCommas("A", "B", "C"), is("A,B,C"));
  }

  @Test public final void testByDoubleArrayChar() {
    azzert.that(Separate.by(new double[] { 3.3, 4.2 }, ':'), is("3.3:4.2"));
  }

  @Test public final void testByDoubleArrayString() {
    azzert.that(Separate.by(new double[] { -1.0, 2.0 }, "; "), is("-1.0; 2.0"));
  }

  @Test public final void testByFloatArrayChar() {
    azzert.that(Separate.by(new float[] { 3.3F, 4.2F }, ':'), is("3.3:4.2"));
  }

  @Test public final void testByFloatArrayString() {
    azzert.that(Separate.by(new float[] { -1F, 2F }, "; "), is("-1.0; 2.0"));
  }

  @Test public final void testByFOfTIterableOfTChar() {
    azzert.that(Separate.by(a -> "<" + a + '>', new String[] { "A", "B" }, ' '), is("<A> <B>"));
  }

  @Test public final void testByFOfTIterableOfTString() {
    azzert.that(Separate.by(a -> "'" + a + '\'', makeCollection("Hello", "World"), ", "), is("'Hello', 'World'"));
  }

  @Test public final void testByFOfTTArrayChar() {
    azzert.that(Separate.by(a -> "'" + a + '\'', makeCollection("Hello", "World"), ' '), is("'Hello' 'World'"));
  }

  @Test public final void testByFOfTTArrayString() {
    azzert.that(Separate.by(a -> "'" + a + '\'', new String[] { "Hello", "World" }, ", "), is("'Hello', 'World'"));
  }

  @Test public final void testByIntArrayChar() {
    azzert.that(Separate.by(new int[] { 3, 4 }, ':'), is("3:4"));
  }

  @Test public final void testByIntArrayString() {
    azzert.that(Separate.by(new int[] { -1, 2 }, "; "), is("-1; 2"));
  }

  @Test public final void testByIterableOfTChar() {
    azzert.that(Separate.by(makeCollection("Hello", "World"), ','), is("Hello,World"));
  }

  @Test public final void testByIterableOfTString() {
    azzert.that(Separate.by(makeCollection("Hello", "World"), ", "), is("Hello, World"));
  }

  @Test public final void testByLongArrayChar() {
    azzert.that(Separate.by(new long[] { 3, 4 }, ':'), is("3:4"));
  }

  @Test public final void testByLongArrayString() {
    azzert.that(Separate.by(new long[] { -1L, 2L }, "; "), is("-1; 2"));
  }

  @Test public final void testByMapOfKeyValueStringString() {
    @NotNull final Map<String, Integer> map = new TreeMap<>();
    map.put("One", box(1));
    map.put("Two", box(2));
    map.put("Three", box(3));
    map.put("Four", box(4));
    azzert.that(Separate.by(map, ", ", "->"), is("Four->4, One->1, Three->3, Two->2"));
  }

  @Test public final void testByShortArrayChar() {
    azzert.that(Separate.by(new short[] { 3, 4 }, ':'), is("3:4"));
  }

  @Test public final void testByShortArrayString() {
    azzert.that(Separate.by(new short[] { (short) -1, (short) 2 }, "; "), is("-1; 2"));
  }

  @Test public final void testBySpaces() {
    azzert.that(Separate.bySpaces("A", "B", "C"), is("A B C"));
  }

  @Test public final void testByTArrayChar() {
    azzert.that(Separate.by(new String[] { "Hello", "World" }, ','), is("Hello,World"));
  }

  @Test public final void testByTArrayString() {
    azzert.that(Separate.by(new String[] { "Hello", "World" }, ", "), is("Hello, World"));
  }

  @Test public final void testNlIterableOfString() {
    azzert.that(Separate.nl(makeCollection("Hello", "World")), is("Hello\nWorld"));
  }

  @Test public final void testNlStringArray() {
    azzert.that(Separate.nl("Hello", "World"), is("Hello\nWorld"));
  }
}