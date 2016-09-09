package il.org.spartan.strings;

import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("static-method") public class StringerTest {
  @Test public void testFixNumericalSuffix() {
    assertEquals("abc0000001", Stringer.fixNumericalSuffix("abc1"));
    assertEquals("abc0000012", Stringer.fixNumericalSuffix("abc12"));
    assertEquals("abc0000123", Stringer.fixNumericalSuffix("abc123"));
    assertEquals("abc0001234", Stringer.fixNumericalSuffix("abc1234"));
    assertEquals("abc0012345", Stringer.fixNumericalSuffix("abc12345"));
    assertEquals("abc0123456", Stringer.fixNumericalSuffix("abc123456"));
    assertEquals("abc1234567", Stringer.fixNumericalSuffix("abc1234567"));
    assertEquals("abc12345678", Stringer.fixNumericalSuffix("abc12345678"));
    assertEquals(null, Stringer.fixNumericalSuffix(null));
    assertEquals("", Stringer.fixNumericalSuffix(""));
    assertEquals("abc", Stringer.fixNumericalSuffix("abc"));
  }
}
