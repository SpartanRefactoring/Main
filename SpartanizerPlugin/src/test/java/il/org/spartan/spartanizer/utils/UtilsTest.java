package il.org.spartan.spartanizer.utils;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

@FixMethodOrder(MethodSorters.JVM) //
@SuppressWarnings({ "static-method", "javadoc", }) //
public class UtilsTest {
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
    azzert.nay(in("X", "A", "B", "C"));
  }

  @Test public void inTypicalTrue() {
    azzert.aye(in("A", "A", "B", "C"));
  }

  @Test public void removePrefiEmpty() {
    assertEquals(removePrefix("BAAAAB", "A"), "BAAAAB");
  }

  @Test public void removePrefiExhaustive() {
    assertEquals(removePrefix("AXAXAXAXAXAXAXAX", "AX"), "");
  }

  @Test public void removePrefixTypical() {
    assertEquals(removePrefix("AAAABC", "AA"), "BC");
  }

  @Test public void removeSuffiEmpty() {
    assertEquals(removeSuffix("BAAAAB", "A"), "BAAAAB");
  }

  @Test public void removeSuffiExhaustive() {
    assertEquals(removeSuffix("AXAXAXAXAXAXAXAX", "AX"), "");
  }

  @Test public void removeSuffixTypical() {
    assertEquals(removeSuffix("AAAABC", "BC"), "AAAA");
  }

  @Test public void removeWhitesTest() {
    azzert.that(removeWhites("ABC"), is("ABC"));
    azzert.that(removeWhites("ABC\n"), is("ABC"));
    azzert.that(removeWhites(" ABC\n"), is("ABC"));
    azzert.that(removeWhites("A BC"), is("ABC"));
    azzert.that(removeWhites("AB\rC\n"), is("ABC"));
    azzert.that(removeWhites("A\fB\rC\n"), is("ABC"));
    azzert.that(removeWhites("\t\tA\fB\rC\n"), is("ABC"));
  }
}
