package il.org.spartan.spartanizer.engine;

import static org.junit.Assert.*;

import java.text.*;
import java.util.concurrent.atomic.*;

import org.junit.*;

/** Test class for {@link Linguistic}
 * @author yonzarecki 
 * @author ZivIzhar 
 * @author rodedzats
 * @since 2016-11-13 */
@SuppressWarnings("static-method") public class LinguisticTest {
  @Test public void testTrimAbsoluteReturnsSameStringForShortString() {
    assertNull(Linguistic.trimAbsolute(null, Linguistic.TRIM_THRESHOLD, Linguistic.TRIM_SUFFIX));
    assertEquals("short string", Linguistic.trimAbsolute("short string", Linguistic.TRIM_THRESHOLD, Linguistic.TRIM_SUFFIX));
  }
  @Test public void testTrimAbsoluteTrimsOnLongStrings() {
    final StringBuilder sb = new StringBuilder();
    for (int ¢ = 0; ¢ <= Linguistic.TRIM_THRESHOLD; ++¢)
      sb.append("a");
    assertEquals((sb + "").substring(0, (sb + "").length() - 4) + Linguistic.TRIM_SUFFIX,
        Linguistic.trimAbsolute(sb + "", Linguistic.TRIM_THRESHOLD, Linguistic.TRIM_SUFFIX));
  }
  @Test public void testTrimLeavesShortStringsAsIs() {
    assertEquals("Hello World\n Hello Technion\n ", Linguistic.trim("Hello World\n Hello Technion\n "));
  }
  @Test public void testTimeCorrect() {
    assertEquals(new DecimalFormat(Linguistic.DOUBLE_FORMAT).format(0), Linguistic.time(0));
  }
  @Test public void testPluralInteger() {
    assertEquals(Linguistic.UNKNOWN + " houses", Linguistic.plurals("house", (Integer) null));
    assertEquals("one house", Linguistic.plurals("house", Integer.valueOf(1)));
    assertEquals("2 houses", Linguistic.plurals("house", Integer.valueOf(2)));
  }
  @Test public void testPluralesInteger() {
    assertEquals(Linguistic.UNKNOWN + " churches", Linguistic.plurales("church", (Integer) null));
    assertEquals("one church", Linguistic.plurales("church", Integer.valueOf(1)));
    assertEquals("2 churches", Linguistic.plurales("church", Integer.valueOf(2)));
  }
  @Test public void testPluralAtomicInteger() {
    assertEquals(Linguistic.UNKNOWN + " houses", Linguistic.plurals("house", (AtomicInteger) null));
    assertEquals("one house", Linguistic.plurals("house", new AtomicInteger(1)));
    assertEquals("2 houses", Linguistic.plurals("house", new AtomicInteger(2)));
  }
  @Test public void testPluralesAtomicInteger() {
    assertEquals(Linguistic.UNKNOWN + " churches", Linguistic.plurales("church", (AtomicInteger) null));
    assertEquals("one church", Linguistic.plurales("church", new AtomicInteger(1)));
    assertEquals("2 churches", Linguistic.plurales("church", new AtomicInteger(2)));
  }
  @Test public void testPluralsInt() {
    assertEquals("one house", Linguistic.plurals("house", 1));
    assertEquals("2 houses", Linguistic.plurals("house", 2));
  }
  @Test public void testPluralesInt() {
    assertEquals("one church", Linguistic.plurales("church", 1));
    assertEquals("2 churches", Linguistic.plurales("church", 2));
  }
}
