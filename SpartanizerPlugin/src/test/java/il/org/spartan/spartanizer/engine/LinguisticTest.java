package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import java.text.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.utils.*;

/** Test class for {@link Linguistic}
 * @author yonzarecki
 * @author ZivIzhar
 * @author rodedzats
 * @since 2016-11-13 */
@SuppressWarnings("static-method") //
public class LinguisticTest {
  @Test public void testPluralesInt() {
    azzert.that(Linguistic.plurales("church", (Int) null), is(Linguistic.UNKNOWN + " churches"));
    azzert.that(Linguistic.plurales("church", new Int(1)), is("one church"));
    azzert.that(Linguistic.plurales("church", new Int(2)), is("2 churches"));
  }

  @Test public void testPluralesInteger() {
    azzert.that(Linguistic.plurales("church", (Integer) null), is(Linguistic.UNKNOWN + " churches"));
    azzert.that(Linguistic.plurales("church", Integer.valueOf(1)), is("one church"));
    azzert.that(Linguistic.plurales("church", Integer.valueOf(2)), is("2 churches"));
  }

  @Test public void testPluralInt() {
    azzert.that(Linguistic.plurals("house", (Int) null), is(Linguistic.UNKNOWN + " houses"));
    azzert.that(Linguistic.plurals("house", new Int(1)), is("one house"));
    azzert.that(Linguistic.plurals("house", new Int(2)), is("2 houses"));
  }

  @Test public void testPluralInteger() {
    azzert.that(Linguistic.plurals("house", (Integer) null), is(Linguistic.UNKNOWN + " houses"));
    azzert.that(Linguistic.plurals("house", Integer.valueOf(1)), is("one house"));
    azzert.that(Linguistic.plurals("house", Integer.valueOf(2)), is("2 houses"));
  }

  @Test public void testPluralsInt() {
    azzert.that(Linguistic.plurals("house", 1), is("one house"));
    azzert.that(Linguistic.plurals("house", 2), is("2 houses"));
  }

  @Test public void testTimeCorrect() {
    azzert.that(Linguistic.time(0), is(new DecimalFormat(Linguistic.DOUBLE_FORMAT).format(0)));
  }

  @Test public void testTimeMAX() {
    azzert.that(Linguistic.time(Long.MAX_VALUE), is(new DecimalFormat(Linguistic.DOUBLE_FORMAT).format(9223372036.85)));
  }

  @Test public void testTimeMIN() {
    azzert.that(Linguistic.time(Long.MIN_VALUE), is(new DecimalFormat(Linguistic.DOUBLE_FORMAT).format(-9223372036.85)));
  }

  @Test public void testTrimAbsoluteReturnsSameStringForShortString() {
    azzert.isNull(Linguistic.trimAbsolute(null, Linguistic.TRIM_THRESHOLD, Linguistic.TRIM_SUFFIX));
    azzert.that(Linguistic.trimAbsolute("short string", Linguistic.TRIM_THRESHOLD, Linguistic.TRIM_SUFFIX), is("short string"));
  }

  @Test public void testTrimAbsoluteTrimsOnLongStrings() {
    final StringBuilder sb = new StringBuilder();
    for (int ¢ = 0; ¢ <= Linguistic.TRIM_THRESHOLD; ++¢)
      sb.append("a");
    azzert.that(Linguistic.trimAbsolute(sb + "", Linguistic.TRIM_THRESHOLD, Linguistic.TRIM_SUFFIX),
        is((sb + "").substring(0, (sb + "").length() - 4) + Linguistic.TRIM_SUFFIX));
  }

  @Test public void testTrimLeavesShortStringsAsIs() {
    azzert.that(Linguistic.trim("Hello World\n Hello Technion\n "), is("Hello World\n Hello Technion\n "));
  }

  @Test public void testUnknownIfNull() {
    azzert.that(Linguistic.unknownIfNull(Integer.valueOf(1)), is("1"));
    azzert.that(Linguistic.unknownIfNull(new Int(1)), is("1"));
    azzert.that(Linguistic.unknownIfNull(null), is(Linguistic.UNKNOWN));
  }

  @Test @SuppressWarnings("boxing") public void testUnknownIfNullWithFunction() {
    azzert.that(Linguistic.unknownIfNull(Integer.valueOf(1), (final Integer ¢) -> ¢ + 1), is("2"));
    azzert.that(Linguistic.unknownIfNull(new Int(1), (final Int ¢) -> Integer.valueOf(2)), is("2"));
    azzert.that(Linguistic.unknownIfNull(null, (final Integer ¢) -> ¢ + 1), is(Linguistic.UNKNOWN));
  }
}
