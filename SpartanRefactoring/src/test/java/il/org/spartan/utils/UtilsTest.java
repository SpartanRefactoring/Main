package il.org.spartan.utils;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.hamcrest.OrderingComparison.greaterThan;
import static il.org.spartan.hamcrest.OrderingComparison.lessThan;
import static il.org.spartan.utils.Utils.in;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import il.org.spartan.utils.Utils;

@FixMethodOrder(MethodSorters.JVM) //
@SuppressWarnings({ "static-method", "javadoc", }) //
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
    assertFalse(in("X", "A", "B", "C"));
  }
  @Test public void inTypicalTrue() {
    assertTrue(in("A", "A", "B", "C"));
  }
  @Test public void removePrefiEmpty() {
    assertEquals(Utils.removePrefix("BAAAAB", "A"), "BAAAAB");
  }
  @Test public void removePrefiExhaustive() {
    assertEquals(Utils.removePrefix("AXAXAXAXAXAXAXAX", "AX"), "");
  }
  @Test public void removePrefixTypical() {
    assertEquals(Utils.removePrefix("AAAABC", "AA"), "BC");
  }
  @Test public void removeSuffiEmpty() {
    assertEquals(Utils.removeSuffix("BAAAAB", "A"), "BAAAAB");
  }
  @Test public void removeSuffiExhaustive() {
    assertEquals(Utils.removeSuffix("AXAXAXAXAXAXAXAX", "AX"), "");
  }
  @Test public void removeSuffixTypical() {
    assertEquals(Utils.removeSuffix("AAAABC", "BC"), "AAAA");
  }
  @Test public void removeWhites() {
    assertThat(Utils.removeWhites("ABC"), is("ABC"));
    assertThat(Utils.removeWhites("ABC\n"), is("ABC"));
    assertThat(Utils.removeWhites(" ABC\n"), is("ABC"));
    assertThat(Utils.removeWhites("A BC"), is("ABC"));
    assertThat(Utils.removeWhites("AB\rC\n"), is("ABC"));
    assertThat(Utils.removeWhites("A\fB\rC\n"), is("ABC"));
    assertThat(Utils.removeWhites("\t\tA\fB\rC\n"), is("ABC"));
  }
}
