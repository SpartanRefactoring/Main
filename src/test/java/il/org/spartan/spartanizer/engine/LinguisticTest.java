package il.org.spartan.spartanizer.engine;

import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("static-method") public class LinguisticTest {
  @Test public void testTrimAbsoluteReturnsSameStringForShortString() {
    assertNull(Linguistic.trimAbsolute(null,Linguistic.TRIM_THRESHOLD,Linguistic.TRIM_SUFFIX));
    assertEquals("short string", Linguistic.trimAbsolute("short string", Linguistic.TRIM_THRESHOLD, Linguistic.TRIM_SUFFIX));
  }
  
  @Test public void testTrimAbsoluteTrimsOnLongStrings() {
    StringBuilder sb = new StringBuilder();
    for (int ¢=0;¢<=Linguistic.TRIM_THRESHOLD;++¢)
      sb.append("a");
    assertEquals((sb + "").substring(0, (sb + "").length() - 4) + Linguistic.TRIM_SUFFIX,
        Linguistic.trimAbsolute((sb + ""), Linguistic.TRIM_THRESHOLD, Linguistic.TRIM_SUFFIX));
  }
  
  @Test public void testTrimLeavesShortStringsAsIs() {
    assertEquals("Hello World\n Hello Technion\n ",Linguistic.trim("Hello World\n Hello Technion\n "));
  }
}
