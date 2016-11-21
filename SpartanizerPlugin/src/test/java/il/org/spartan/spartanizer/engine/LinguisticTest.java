package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import java.text.*;
import java.util.concurrent.atomic.*;

import org.junit.*;

import il.org.spartan.*;

/** Test class for {@link Linguistic}
 * @author yonzarecki
 * @author ZivIzhar
 * @author rodedzats
 * @since 2016-11-13 */
@SuppressWarnings("static-method") //
public class LinguisticTest {
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

  @Test public void testTimeCorrect() {
    azzert.that(Linguistic.time(0), is(new DecimalFormat(Linguistic.DOUBLE_FORMAT).format(0)));
  }

  @Test public void testPluralInteger() {
    azzert.that(Linguistic.plurals("house", (Integer) null), is(Linguistic.UNKNOWN + " houses"));
    azzert.that(Linguistic.plurals("house", Integer.valueOf(1)), is("one house"));
    azzert.that(Linguistic.plurals("house", Integer.valueOf(2)), is("2 houses"));
  }

  @Test public void testPluralesInteger() {
    azzert.that(Linguistic.plurales("church", (Integer) null), is(Linguistic.UNKNOWN + " churches"));
    azzert.that(Linguistic.plurales("church", Integer.valueOf(1)), is("one church"));
    azzert.that(Linguistic.plurales("church", Integer.valueOf(2)), is("2 churches"));
  }

  @Test public void testPluralAtomicInteger() {
    azzert.that(Linguistic.plurals("house", (AtomicInteger) null), is(Linguistic.UNKNOWN + " houses"));
    azzert.that(Linguistic.plurals("house", new AtomicInteger(1)), is("one house"));
    azzert.that(Linguistic.plurals("house", new AtomicInteger(2)), is("2 houses"));
  }

  @Test public void testPluralesAtomicInteger() {
    azzert.that(Linguistic.plurales("church", (AtomicInteger) null), is(Linguistic.UNKNOWN + " churches"));
    azzert.that(Linguistic.plurales("church", new AtomicInteger(1)), is("one church"));
    azzert.that(Linguistic.plurales("church", new AtomicInteger(2)), is("2 churches"));
  }

  @Test public void testPluralsInt() {
    azzert.that(Linguistic.plurals("house", 1), is("one house"));
    azzert.that(Linguistic.plurals("house", 2), is("2 houses"));
  }

  @Test public void testPluralesInt() {
    azzert.that(Linguistic.plurales("church", 1), is("one church"));
    azzert.that(Linguistic.plurales("church", 2), is("2 churches"));
  }

  @Test public void testUnknownIfNull() {
    azzert.that(Linguistic.unknownIfNull(Integer.valueOf(1)), is("1"));
    azzert.that(Linguistic.unknownIfNull(new AtomicInteger(1)), is("1"));
    azzert.that(Linguistic.unknownIfNull(null), is(Linguistic.UNKNOWN));
  }

  @SuppressWarnings("boxing") @Test public void testUnknownIfNullWithFunction() {
    azzert.that(Linguistic.unknownIfNull(Integer.valueOf(1), (final Integer i) -> i + 1), is("2"));
    azzert.that(Linguistic.unknownIfNull(new AtomicInteger(1), (final AtomicInteger i) -> Integer.valueOf(2)), is("2"));
    azzert.that(Linguistic.unknownIfNull(null, (final Integer i) -> i + 1), is(Linguistic.UNKNOWN));
  }
}
