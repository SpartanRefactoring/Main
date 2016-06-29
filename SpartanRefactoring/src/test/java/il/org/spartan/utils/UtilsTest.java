package il.org.spartan.utils;

import static il.org.spartan.hamcrest.SpartanAssert.*;
import static il.org.spartan.utils.Utils.*;
import static org.junit.Assert.*;
import il.org.spartan.Assert;

import org.junit.*;
import org.junit.runners.*;

@FixMethodOrder(MethodSorters.JVM)//
@SuppressWarnings({ "static-method", "javadoc", })//
public class UtilsTest {
  @Test public void compareFF() {
    assertThat(Utils.compare(false, false), is(0));
  }
  @Test public void compareFT() {
    assertThat(Utils.compare(false, true), lessThan(0));
  }
  @Test public void compareTF() {
    assertThat(Utils.compare(true, false), greaterThan(0));
  }
  @Test public void compareTT() {
    assertThat(Utils.compare(true, true), is(0));
  }
  @Test public void inTypicalFalse() {
    assertThat(in("X", "A", "B", "C"), is(false));
  }
  @Test public void inTypicalTrue() {
    Assert.assertThat(in("A", "A", "B", "C"), is(true));
  }
  @Test public void removePrefiEmpty() {
    assertThat("", "BAAAAB", is(Utils.removePrefix("BAAAAB", "A")));
  }
  @Test public void removePrefiExhaustive() {
    assertThat("", "", is(Utils.removePrefix("AXAXAXAXAXAXAXAX", "AX")));
  }
  @Test public void removePrefixTypical() {
    assertThat("", "BC", is(Utils.removePrefix("AAAABC", "AA")));
  }
  @Test public void removeSuffiEmpty() {
    assertThat("", "BAAAAB", is(Utils.removeSuffix("BAAAAB", "A")));
  }
  @Test public void removeSuffiExhaustive() {
    assertThat("", "", is(Utils.removeSuffix("AXAXAXAXAXAXAXAX", "AX")));
  }
  @Test public void removeSuffixTypical() {
    assertThat("", "AAAA", is(Utils.removeSuffix("AAAABC", "BC")));
  }
  @Test public void removeWhites() {
    assertThat("", Utils.removeWhites("ABC"), is("ABC"));
    assertThat("", Utils.removeWhites("ABC\n"), is("ABC"));
    assertThat("", Utils.removeWhites(" ABC\n"), is("ABC"));
    assertThat("", Utils.removeWhites("A BC"), is("ABC"));
    assertThat("", Utils.removeWhites("AB\rC\n"), is("ABC"));
    assertThat("", Utils.removeWhites("A\fB\rC\n"), is("ABC"));
    assertThat("", Utils.removeWhites("\t\tA\fB\rC\n"), is("ABC"));
  }
}
