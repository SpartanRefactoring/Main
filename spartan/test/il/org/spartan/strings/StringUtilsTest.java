package il.org.spartan.strings;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.utils.*;

@SuppressWarnings("static-method") //
public class StringUtilsTest {
  @Test(expected = ___.Bug.Contract.Precondition.class) public final void testStripLength0() {
    StringUtils.strip(".");
  }

  @Test(expected = ___.Bug.Contract.Precondition.class) public final void testStripLength1() {
    StringUtils.strip(".");
  }

  @Test(expected = ___.Bug.Contract.Precondition.class) public final void testStripLength1Alternate() {
    StringUtils.strip("X");
  }

  @Test(expected = ___.Bug.Assertion.Value.NonNull.class) public final void testStripNull() {
    StringUtils.strip(null);
  }

  @Test public void testToLowerLargeNumbers() {
    assertEquals("z", StringUtils.lowCounter(25));
    assertEquals("ba", StringUtils.lowCounter(26));
    assertEquals("bb", StringUtils.lowCounter(27));
  }

  @Test public void testToLowerSmallNumbers() {
    assertEquals("", StringUtils.lowCounter(-1));
    assertEquals("a", StringUtils.lowCounter(0));
    assertEquals("b", StringUtils.lowCounter(1));
  }

  @Test public final void testValidStrip() {
    assertEquals("ab", StringUtils.strip("xaby"));
    assertEquals("", StringUtils.strip("ab"));
    assertEquals("b", StringUtils.strip("Abc"));
  }
}
