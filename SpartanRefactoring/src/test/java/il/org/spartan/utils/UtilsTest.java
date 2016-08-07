package il.org.spartan.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.Utils.*;
import org.junit.*;
import org.junit.runners.*;
import il.org.spartan.*;

@FixMethodOrder(MethodSorters.JVM) //
@SuppressWarnings({ "static-method", "javadoc", }) //
public class UtilsTest {
  @Test public void compareFF() {
    azzert.that(Utils.compare(false, false), is(0));
  }
  @Test public void compareFT() {
    azzert.that(Utils.compare(false, true), lessThan(0));
  }
  @Test public void compareTF() {
    azzert.that(Utils.compare(true, false), greaterThan(0));
  }
  @Test public void compareTT() {
    azzert.that(Utils.compare(true, true), is(0));
  }
  @Test public void inTypicalFalse() {
    azzert.nay(in("X", "A", "B", "C"));
  }
  @Test public void inTypicalTrue() {
    azzert.aye(in("A", "A", "B", "C"));
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
    azzert.that(Utils.removeWhites("ABC"), is("ABC"));
    azzert.that(Utils.removeWhites("ABC\n"), is("ABC"));
    azzert.that(Utils.removeWhites(" ABC\n"), is("ABC"));
    azzert.that(Utils.removeWhites("A BC"), is("ABC"));
    azzert.that(Utils.removeWhites("AB\rC\n"), is("ABC"));
    azzert.that(Utils.removeWhites("A\fB\rC\n"), is("ABC"));
    azzert.that(Utils.removeWhites("\t\tA\fB\rC\n"), is("ABC"));
  }
}
