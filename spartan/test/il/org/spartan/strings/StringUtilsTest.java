package il.org.spartan.strings;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
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
    azzert.that(StringUtils.lowCounter(25), is("z"));
    azzert.that(StringUtils.lowCounter(26), is("ba"));
    azzert.that(StringUtils.lowCounter(27), is("bb"));
  }

  @Test public void testToLowerSmallNumbers() {
    azzert.that(StringUtils.lowCounter(-1), is(""));
    azzert.that(StringUtils.lowCounter(0), is("a"));
    azzert.that(StringUtils.lowCounter(1), is("b"));
  }

  @Test public final void testValidStrip() {
    azzert.that(StringUtils.strip("xaby"), is("ab"));
    azzert.that(StringUtils.strip("ab"), is(""));
    azzert.that(StringUtils.strip("Abc"), is("b"));
  }
}
