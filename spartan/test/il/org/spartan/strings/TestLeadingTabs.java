/**
 *
 */
package il.org.spartan.strings;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings("static-method") public class TestLeadingTabs {
  @Test public void testEmptyString() {
    azzert.assertThat("", StringUtils.expandLeadingTabs(""), is(""));
  }

  @Test public void testFinalTab() {
    azzert.assertThat("", StringUtils.expandLeadingTabs("AXY\t"), is("AXY\t"));
  }

  @Test public void testInnerTab() {
    azzert.assertThat("", StringUtils.expandLeadingTabs("A\tB\t"), is("A\tB\t"));
  }

  @Test public void testLeadingInnerFinalTab() {
    azzert.assertThat("", StringUtils.expandLeadingTabs("\t\tA\tX\tY\t"), is("        A\tX\tY\t"));
  }

  @Test public void testNonEmptyString() {
    azzert.assertThat("", StringUtils.expandLeadingTabs("ABC"), is("ABC"));
  }

  @Test public void testOneTab() {
    azzert.assertThat("", StringUtils.expandLeadingTabs("\t"), is("    "));
  }

  @Test public void testSpaceThenTab() {
    azzert.assertThat("", StringUtils.visualize(StringUtils.expandLeadingTabs(" \tX")), is(StringUtils.visualize(" \tX")));
  }

  @Test public void testTabThenSpace() {
    azzert.assertThat("", StringUtils.expandLeadingTabs("\t X"), is("     X"));
  }

  @Test public void testThreeTabs() {
    azzert.assertThat("", StringUtils.expandLeadingTabs("\t\t\t"), is("            "));
  }

  @Test public void testTwoLinesTabDOSMode() {
    azzert.assertThat("", StringUtils.expandLeadingTabs("\r\nA\r\n\tB"), is("\r\nA\r\n    B"));
  }

  @Test public void testTwoLinesTabUnixMode() {
    azzert.assertThat("", StringUtils.expandLeadingTabs("\nA\n\tB"), is("\nA\n    B"));
  }

  @Test public void testTwoTabs() {
    azzert.assertThat("", StringUtils.expandLeadingTabs("\t\t"), is("        "));
  }
}
