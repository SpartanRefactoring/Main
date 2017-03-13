package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import java.text.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.utils.*;

/** Test class for {@link english}
 * @author yonzarecki
 * @author ZivIzhar
 * @author rodedzats
 * @since 2016-11-13 */
@SuppressWarnings("static-method") //
public class LinguisticTest {
  @Test public void testPluralesInt() {
    azzert.that(english.plurales("church", (Int) null), is(english.UNKNOWN + " churches"));
    azzert.that(english.plurales("church", new Int(1)), is("one church"));
    azzert.that(english.plurales("church", new Int(2)), is("2 churches"));
  }

  @Test public void testPluralesInteger() {
    azzert.that(english.plurales("church", (Integer) null), is(english.UNKNOWN + " churches"));
    azzert.that(english.plurales("church", Integer.valueOf(1)), is("one church"));
    azzert.that(english.plurales("church", Integer.valueOf(2)), is("2 churches"));
  }

  @Test public void testPluralInt() {
    azzert.that(english.plurals("house", (Int) null), is(english.UNKNOWN + " houses"));
    azzert.that(english.plurals("house", new Int(1)), is("one house"));
    azzert.that(english.plurals("house", new Int(2)), is("2 houses"));
  }

  @Test public void testPluralInteger() {
    azzert.that(english.plurals("house", (Integer) null), is(english.UNKNOWN + " houses"));
    azzert.that(english.plurals("house", Integer.valueOf(1)), is("one house"));
    azzert.that(english.plurals("house", Integer.valueOf(2)), is("2 houses"));
  }

  @Test public void testPluralsInt() {
    azzert.that(english.plurals("house", 1), is("one house"));
    azzert.that(english.plurals("house", 2), is("2 houses"));
  }

  @Test public void testTimeCorrect() {
    azzert.that(english.time(0), is(new DecimalFormat(english.DOUBLE_FORMAT).format(0)));
  }

  @Test public void testTimeMAX() {
    azzert.that(english.time(Long.MAX_VALUE), is(new DecimalFormat(english.DOUBLE_FORMAT).format(9223372036.85)));
  }

  @Test public void testTimeMIN() {
    azzert.that(english.time(Long.MIN_VALUE), is(new DecimalFormat(english.DOUBLE_FORMAT).format(-9223372036.85)));
  }

  @Test public void testTrimAbsoluteReturnsSameStringForShortString() {
    azzert.isNull(english.trimAbsolute(null, english.TRIM_THRESHOLD, english.TRIM_SUFFIX));
    azzert.that(english.trimAbsolute("short string", english.TRIM_THRESHOLD, english.TRIM_SUFFIX), is("short string"));
  }

  @Test public void testTrimAbsoluteTrimsOnLongStrings() {
    final StringBuilder sb = new StringBuilder();
    for (int ¢ = 0; ¢ <= english.TRIM_THRESHOLD; ++¢)
      sb.append("a");
    azzert.that(english.trimAbsolute(sb + "", english.TRIM_THRESHOLD, english.TRIM_SUFFIX),
        is((sb + "").substring(0, (sb + "").length() - 4) + english.TRIM_SUFFIX));
  }

  @Test public void testTrimLeavesShortStringsAsIs() {
    azzert.that(english.trim("Hello World\n Hello Technion\n "), is("Hello World\n Hello Technion\n "));
  }

  @Test public void testUnknownIfNull() {
    azzert.that(english.unknownIfNull(Integer.valueOf(1)), is("1"));
    azzert.that(english.unknownIfNull(new Int(1)), is("1"));
    azzert.that(english.unknownIfNull(null), is(english.UNKNOWN));
  }

  @Test @SuppressWarnings("boxing") public void testUnknownIfNullWithFunction() {
    azzert.that(english.unknownIfNull(Integer.valueOf(1), (final Integer ¢) -> ¢ + 1), is("2"));
    azzert.that(english.unknownIfNull(new Int(1), (final Int ¢) -> Integer.valueOf(2)), is("2"));
    azzert.that(english.unknownIfNull(null, (final Integer ¢) -> ¢ + 1), is(english.UNKNOWN));
  }
}
