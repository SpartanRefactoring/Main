package il.org.spartan.utils;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

@FixMethodOrder(MethodSorters.JVM) //
@SuppressWarnings({ "static-method", "javadoc", }) //
public class UtilsTest {
  @Test public void compareFF() {
    that(Utils.compare(false, false), is(0));
  }
  @Test public void compareFT() {
    that(Utils.compare(false, true), lessThan(0));
  }
  @Test public void compareTF() {
    that(Utils.compare(true, false), greaterThan(0));
  }
  @Test public void compareTT() {
    that(Utils.compare(true, true), is(0));
  }
  @Test public void inTypicalFalse() {
    that(in("X", "A", "B", "C"), is(false));
  }
  @Test public void inTypicalTrue() {
    azzert.that(in("A", "A", "B", "C"), is(true));
  }
  @Test public void removePrefiEmpty() {
    that("BAAAAB", is(Utils.removePrefix("BAAAAB", "A")));
  }
  @Test public void removePrefiExhaustive() {
    that("", is(Utils.removePrefix("AXAXAXAXAXAXAXAX", "AX")));
  }
  @Test public void removePrefixTypical() {
    that("BC", is(Utils.removePrefix("AAAABC", "AA")));
  }
  @Test public void removeSuffiEmpty() {
    that("BAAAAB", is(Utils.removeSuffix("BAAAAB", "A")));
  }
  @Test public void removeSuffiExhaustive() {
    that("", is(Utils.removeSuffix("AXAXAXAXAXAXAXAX", "AX")));
  }
  @Test public void removeSuffixTypical() {
    that("AAAA", is(Utils.removeSuffix("AAAABC", "BC")));
  }
  @Test public void removeWhites() {
    that(Utils.removeWhites("ABC"), is("ABC"));
    that(Utils.removeWhites("ABC\n"), is("ABC"));
    that(Utils.removeWhites(" ABC\n"), is("ABC"));
    that(Utils.removeWhites("A BC"), is("ABC"));
    that(Utils.removeWhites("AB\rC\n"), is("ABC"));
    that(Utils.removeWhites("A\fB\rC\n"), is("ABC"));
    that(Utils.removeWhites("\t\tA\fB\rC\n"), is("ABC"));
  }
}
