package il.org.spartan.csv;

import static fluent.ly.azzert.*;

import java.util.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.streotypes.*;

@TestCase @SuppressWarnings("static-method") public class CSVTest {
  public static void main(final String[] args) {
    new CSVTest().testCombineSplitShort();
  }

  @Test public void test1() {
    @NotNull final String s = "abc,def\r\n\tg\\m", t = CSV.escape(s);
    @Nullable final String u = CSV.unescape(t);
    azzert.that(t, is("abc\\.def\\r\\n\\tg\\\\m"));
    azzert.that(u, is(s));
    assert !s.equals(t);
  }

  @Test public void testCombineSplit() {
    @NotNull final String[] parts = { "abc", "", "def", "gh\n,", ",", "\r", "\ta", null, "a\t", "\rz", "o\np", "qwerty", ",,,,", ",1,2,3,4" },
        t = CSV.split(CSV.combine(parts));
    azzert.that(t.length, is(parts.length));
    assert Arrays.deepEquals(parts, t);
  }

  @Test public void testCombineSplitEnum() {
    assert Arrays.deepEquals(Rgb.values(), CSV.split(Rgb.class, CSV.combine(Rgb.values())));
    @NotNull final Rgb[] redNull = { Rgb.RED, null };
    assert Arrays.deepEquals(redNull, CSV.split(Rgb.class, CSV.combine(redNull)));
    @Nullable final Rgb[] justNull = { null };
    assert Arrays.deepEquals(justNull, CSV.split(Rgb.class, CSV.combine(justNull)));
  }

  @Test public void testCombineSplitShort() {
    @NotNull final String[] parts = { "abc", ",", "def" }, t = CSV.split(CSV.combine(parts));
    azzert.that(t.length, is(parts.length));
    assert Arrays.deepEquals(parts, t);
  }

  @Test public void testCombineSplitSingleNullElement() {
    @Nullable final String[] parts = { null };
    @NotNull final String[] t = CSV.split(CSV.combine(parts));
    azzert.that(t.length, is(parts.length));
    assert Arrays.deepEquals(parts, t);
  }

  @Test public void testNull() {
    @Nullable final String s = null;
    @NotNull final String t = CSV.escape(s);
    @Nullable final String u = CSV.unescape(t);
    azzert.isNull(s);
    assert t != null;
    azzert.isNull(u);
  }

  @Test public void testSplitCombineClasses() {
    @Nullable final Class<?>[] cs = { String.class, System.class, Object.class, null };
    assert Arrays.deepEquals(cs, CSV.splitToClasses(CSV.combine(cs)));
    @NotNull final String s = "java.lang.String,java.lang.System,java.lang.Object,\\0";
    azzert.that(CSV.combine(CSV.splitToClasses(s)), is(s));
    azzert.that(CSV.combine(cs), is(s));
  }

  @Test public void testSplitCombineEnum() {
    azzert.that(CSV.combine(CSV.split(Rgb.class, "GREEN,RED,BLUE,\\0,RED")), is("GREEN,RED,BLUE,\\0,RED"));
  }

  public enum Rgb {
    RED, GREEN, BLUE
  }
}
