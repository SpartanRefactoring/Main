package il.org.spartan.strings;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;

@SuppressWarnings("static-method") public class TestLeadingTabs {
  @Test public void testEmptyString() {
    azzert.that(StringUtils.expandLeadingTabs(""), is(""));
  }

  @Test public void testFinalTab() {
    azzert.that(StringUtils.expandLeadingTabs("AXY\t"), is("AXY\t"));
  }

  @Test public void testInnerTab() {
    azzert.that(StringUtils.expandLeadingTabs("A\tB\t"), is("A\tB\t"));
  }

  @Test public void testLeadingInnerFinalTab() {
    azzert.that(StringUtils.expandLeadingTabs("\t\tA\tX\tY\t"), is("        A\tX\tY\t"));
  }

  @Test public void testNonEmptyString() {
    azzert.that(StringUtils.expandLeadingTabs("ABC"), is("ABC"));
  }

  @Test public void testOneTab() {
    azzert.that(StringUtils.expandLeadingTabs("\t"), is("    "));
  }

  @Test public void testSpaceThenTab() {
    azzert.that(StringUtils.visualize(StringUtils.expandLeadingTabs(" \tX")), is(StringUtils.visualize(" \tX")));
  }

  @Test public void testTabThenSpace() {
    azzert.that(StringUtils.expandLeadingTabs("\t X"), is("     X"));
  }

  @Test public void testThreeTabs() {
    azzert.that(StringUtils.expandLeadingTabs("\t\t\t"), is("            "));
  }

  @Test public void testTwoLinesTabDOSMode() {
    azzert.that(StringUtils.expandLeadingTabs("\r\nA\r\n\tB"), is("\r\nA\r\n    B"));
  }

  @Test public void testTwoLinesTabUnixMode() {
    azzert.that(StringUtils.expandLeadingTabs("\nA\n\tB"), is("\nA\n    B"));
  }

  @Test public void testTwoTabs() {
    azzert.that(StringUtils.expandLeadingTabs("\t\t"), is("        "));
  }
}
