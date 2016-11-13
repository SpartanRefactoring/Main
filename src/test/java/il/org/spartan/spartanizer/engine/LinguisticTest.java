package il.org.spartan.spartanizer.engine;

import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("static-method") public class LinguisticTest {
  @Test public void testTrimAbsoluteReturnsSameStringForShortString() {
    assertNull(Linguistic.trimAbsolute(null,Linguistic.TRIM_THRESHOLD,Linguistic.TRIM_SUFFIX));
    assertEquals("short string", Linguistic.trimAbsolute("short string", Linguistic.TRIM_THRESHOLD, Linguistic.TRIM_SUFFIX));
  }
}
