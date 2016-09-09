package il.org.spartan.csv;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.streotypes.*;

@SuppressWarnings("static-method") @TestCase public class CSVTest {
  public static void main(final String[] args) {
    new CSVTest().testCombineSplitShort();
  }

  @Test public void test1() {
    final String s = "abc,def\r\n\tg\\m";
    final String t = CSV.escape(s);
    final String u = CSV.unescape(t);
    assertEquals("abc\\.def\\r\\n\\tg\\\\m", t);
    assertEquals(s, u);
    assertFalse(s.equals(t));
  }

  @Test public void testCombineSplit() {
    final String[] parts = { "abc", "", "def", "gh\n,", ",", "\r", "\ta", null, "a\t", "\rz", "o\np", "qwerty", ",,,,", ",1,2,3,4" };
    final String combo = CSV.combine(parts);
    final String[] t = CSV.split(combo);
    assertEquals(parts.length, t.length);
    assertTrue(Arrays.deepEquals(parts, t));
  }

  @Test public void testCombineSplitEnum() {
    assertTrue(Arrays.deepEquals(Rgb.values(), CSV.split(Rgb.class, CSV.combine(Rgb.values()))));
    final Rgb[] redNull = { Rgb.RED, null };
    assertTrue(Arrays.deepEquals(redNull, CSV.split(Rgb.class, CSV.combine(redNull))));
    final Rgb[] justNull = { null };
    assertTrue(Arrays.deepEquals(justNull, CSV.split(Rgb.class, CSV.combine(justNull))));
  }

  @Test public void testCombineSplitShort() {
    final String[] parts = { "abc", ",", "def" };
    final String combo = CSV.combine(parts);
    final String[] t = CSV.split(combo);
    assertEquals(parts.length, t.length);
    assertTrue(Arrays.deepEquals(parts, t));
  }

  @Test public void testCombineSplitSingleNullElement() {
    final String[] parts = { null, };
    final String combo = CSV.combine(parts);
    final String[] t = CSV.split(combo);
    assertEquals(parts.length, t.length);
    assertTrue(Arrays.deepEquals(parts, t));
  }

  @Test public void testNull() {
    final String s = null;
    final String t = CSV.escape(s);
    final String u = CSV.unescape(t);
    assertNull(s);
    assertNotNull(t);
    assertNull(u);
  }

  @Test public void testSplitCombineClasses() {
    final Class<?>[] cs = { String.class, System.class, Object.class, null };
    assertTrue(Arrays.deepEquals(cs, CSV.splitToClasses(CSV.combine(cs))));
    final String s = "java.lang.String,java.lang.System,java.lang.Object,\\0";
    assertEquals(s, CSV.combine(CSV.splitToClasses(s)));
    assertEquals(s, CSV.combine(cs));
  }

  @Test public void testSplitCombineEnum() {
    final String s = "GREEN,RED,BLUE,\\0,RED";
    assertEquals(s, CSV.combine(CSV.split(Rgb.class, s)));
  }

  public static enum Rgb {
    RED, GREEN, BLUE
  }
}
