package il.org.spartan.spartanizer.utils;

import static fluent.ly.azzert.*;
import static il.org.spartan.Utils.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;

import fluent.ly.*;

@SuppressWarnings({ "static-method", "javadoc" })
public final class UtilsTest {
  @Test public void compareFF() {
    azzert.that(compare(false, false), is(0));
  }

  @Test public void compareFT() {
    azzert.that(compare(false, true), lessThan(0));
  }

  @Test public void compareTF() {
    azzert.that(compare(true, false), greaterThan(0));
  }

  @Test public void compareTT() {
    azzert.that(compare(true, true), is(0));
  }

  @Test public void inTypicalFalse() {
    String @NonNull [] ts = { "A", "B", "C" };
    assert !is.in("X",ts);
  }

  @Test public void inTypicalTrue() {
    String @NonNull [] ts = { "A", "B", "C" };
    assert is.in("A",ts);
  }

  @Test public void removePrefiEmpty() {
    azzert.that("BAAAAB", is(removePrefix("BAAAAB", "A")));
  }

  @Test public void removePrefiExhaustive() {
    azzert.that("", is(removePrefix("AXAXAXAXAXAXAXAX", "AX")));
  }

  @Test public void removePrefixTypical() {
    azzert.that("BC", is(removePrefix("AAAABC", "AA")));
  }

  @Test public void removeSuffiEmpty() {
    azzert.that("BAAAAB", is(removeSuffix("BAAAAB", "A")));
  }

  @Test public void removeSuffiExhaustive() {
    azzert.that("", is(removeSuffix("AXAXAXAXAXAXAXAX", "AX")));
  }

  @Test public void removeSuffixTypical() {
    azzert.that("AAAA", is(removeSuffix("AAAABC", "BC")));
  }

  @Test public void removeWhitesTest() {
    azzert.that(removeWhites("ABC"), is("ABC"));
    azzert.that(removeWhites("ABC\n"), is("ABC"));
    azzert.that(removeWhites(" ABC\n"), is("ABC"));
    azzert.that(removeWhites("A BC"), is("ABC"));
    azzert.that(removeWhites("AB\rC\n"), is("ABC"));
    azzert.that(removeWhites("A\fB\rC\n"), is("ABC"));
    azzert.that(removeWhites("\t\t\nA\tA\fB\rC\n"), is("AABC"));
  }
}
