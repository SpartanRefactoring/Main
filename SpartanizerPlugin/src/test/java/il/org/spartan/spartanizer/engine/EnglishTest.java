package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import java.text.*;
import java.util.stream.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.utils.*;

/** Test class for {@link English}
 * @author yonzarecki
 * @author ZivIzhar
 * @author rodedzats
 * @since 2016-11-13 */
@SuppressWarnings("static-method") //
public class EnglishTest {
  @Test public void testPluralesInt() {
    azzert.that(English.plurales("church", (Int) null), is(English.UNKNOWN + " churches"));
    azzert.that(English.plurales("church", new Int(1)), is("one church"));
    azzert.that(English.plurales("church", new Int(2)), is("2 churches"));
  }

  @Test public void testPluralesInteger() {
    azzert.that(English.plurales("church", (Integer) null), is(English.UNKNOWN + " churches"));
    azzert.that(English.plurales("church", Integer.valueOf(1)), is("one church"));
    azzert.that(English.plurales("church", Integer.valueOf(2)), is("2 churches"));
  }

  @Test public void testPluralInt() {
    azzert.that(English.plurals("house", (Int) null), is(English.UNKNOWN + " houses"));
    azzert.that(English.plurals("house", new Int(1)), is("one house"));
    azzert.that(English.plurals("house", new Int(2)), is("2 houses"));
  }

  @Test public void testPluralInteger() {
    azzert.that(English.plurals("house", (Integer) null), is(English.UNKNOWN + " houses"));
    azzert.that(English.plurals("house", Integer.valueOf(1)), is("one house"));
    azzert.that(English.plurals("house", Integer.valueOf(2)), is("2 houses"));
  }

  @Test public void testPluralsInt() {
    azzert.that(English.plurals("house", 1), is("one house"));
    azzert.that(English.plurals("house", 2), is("2 houses"));
  }

  @Test public void testTimeCorrect() {
    azzert.that(English.time(0), is(new DecimalFormat(English.DOUBLE_FORMAT).format(0)));
  }

  @Test public void testTimeMAX() {
    azzert.that(English.time(Long.MAX_VALUE), is(new DecimalFormat(English.DOUBLE_FORMAT).format(9223372036.85)));
  }

  @Test public void testTimeMIN() {
    azzert.that(English.time(Long.MIN_VALUE), is(new DecimalFormat(English.DOUBLE_FORMAT).format(-9223372036.85)));
  }

  @Test public void testTrimAbsoluteReturnsSameStringForShortString() {
    azzert.isNull(English.trimAbsolute(null, English.TRIM_THRESHOLD, English.TRIM_SUFFIX));
    azzert.that(English.trimAbsolute("short string", English.TRIM_THRESHOLD, English.TRIM_SUFFIX), is("short string"));
  }

  @Test public void testTrimAbsoluteTrimsOnLongStrings() {
    final StringBuilder sb = new StringBuilder();
    IntStream.rangeClosed(0, English.TRIM_THRESHOLD).forEach(λ -> sb.append("a"));
    azzert.that(English.trimAbsolute(sb + "", English.TRIM_THRESHOLD, English.TRIM_SUFFIX),
        is((sb + "").substring(0, (sb + "").length() - 4) + English.TRIM_SUFFIX));
  }

  @Test public void testTrimLeavesShortStringsAsIs() {
    azzert.that(English.trim("Hello World\n Hello Technion\n "), is("Hello World\n Hello Technion\n "));
  }

  @Test public void testUnknownIfNull() {
    azzert.that(English.unknownIfNull(Integer.valueOf(1)), is("1"));
    azzert.that(English.unknownIfNull(new Int(1)), is("1"));
    azzert.that(English.unknownIfNull(null), is(English.UNKNOWN));
  }

  @Test @SuppressWarnings("boxing") public void testUnknownIfNullWithFunction() {
    azzert.that(English.unknownIfNull(Integer.valueOf(1), (final Integer ¢) -> ¢ + 1), is("2"));
    azzert.that(English.unknownIfNull(new Int(1), (final Int ¢) -> Integer.valueOf(2)), is("2"));
    azzert.that(English.unknownIfNull(null, (final Integer ¢) -> ¢ + 1), is(English.UNKNOWN));
  }
}
