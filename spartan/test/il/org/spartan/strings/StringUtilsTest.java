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
    azzert.assertThat("", StringUtils.lowCounter(25), is("z"));
    azzert.assertThat("", StringUtils.lowCounter(26), is("ba"));
    azzert.assertThat("", StringUtils.lowCounter(27), is("bb"));
  }

  @Test public void testToLowerSmallNumbers() {
    azzert.assertThat("", StringUtils.lowCounter(-1), is(""));
    azzert.assertThat("", StringUtils.lowCounter(0), is("a"));
    azzert.assertThat("", StringUtils.lowCounter(1), is("b"));
  }

  @Test public final void testValidStrip() {
    azzert.assertThat("", StringUtils.strip("xaby"), is("ab"));
    azzert.assertThat("", StringUtils.strip("ab"), is(""));
    azzert.assertThat("", StringUtils.strip("Abc"), is("b"));
  }
}
