package il.org.spartan.csv;

import static il.org.spartan.azzert.*;

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
    azzert.that(t, is("abc\\.def\\r\\n\\tg\\\\m"));
    azzert.that(u, is(s));
    assert !s.equals(t);
  }

  @Test public void testCombineSplit() {
    final String[] parts = { "abc", "", "def", "gh\n,", ",", "\r", "\ta", null, "a\t", "\rz", "o\np", "qwerty", ",,,,", ",1,2,3,4" };
    final String combo = CSV.combine(parts);
    final String[] t = CSV.split(combo);
    azzert.that(t.length, is(parts.length));
    assert Arrays.deepEquals(parts, t);
  }

  @Test public void testCombineSplitEnum() {
    assert Arrays.deepEquals(Rgb.values(), CSV.split(Rgb.class, CSV.combine(Rgb.values())));
    final Rgb[] redNull = { Rgb.RED, null };
    assert Arrays.deepEquals(redNull, CSV.split(Rgb.class, CSV.combine(redNull)));
    final Rgb[] justNull = { null };
    assert Arrays.deepEquals(justNull, CSV.split(Rgb.class, CSV.combine(justNull)));
  }

  @Test public void testCombineSplitShort() {
    final String[] parts = { "abc", ",", "def" };
    final String combo = CSV.combine(parts);
    final String[] t = CSV.split(combo);
    azzert.that(t.length, is(parts.length));
    assert Arrays.deepEquals(parts, t);
  }

  @Test public void testCombineSplitSingleNullElement() {
    final String[] parts = { null, };
    final String combo = CSV.combine(parts);
    final String[] t = CSV.split(combo);
    azzert.that(t.length, is(parts.length));
    assert Arrays.deepEquals(parts, t);
  }

  @Test public void testNull() {
    final String s = null;
    final String t = CSV.escape(s);
    final String u = CSV.unescape(t);
    azzert.isNull(s);
    assert t != null;
    azzert.isNull(u);
  }

  @Test public void testSplitCombineClasses() {
    final Class<?>[] cs = { String.class, System.class, Object.class, null };
    assert Arrays.deepEquals(cs, CSV.splitToClasses(CSV.combine(cs)));
    final String s = "java.lang.String,java.lang.System,java.lang.Object,\\0";
    azzert.that(CSV.combine(CSV.splitToClasses(s)), is(s));
    azzert.that(CSV.combine(cs), is(s));
  }

  @SuppressWarnings("javadoc") @Test public void testSplitCombineEnum() {
    azzert.that(CSV.combine(CSV.split(Rgb.class, "GREEN,RED,BLUE,\\0,RED")), is("GREEN,RED,BLUE,\\0,RED"));
  }

  public enum Rgb {
    RED, GREEN, BLUE
  }
}
