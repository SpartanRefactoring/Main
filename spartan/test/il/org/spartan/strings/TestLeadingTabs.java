/**
 *
 */
package il.org.spartan.strings;

import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("static-method") public class TestLeadingTabs {
  @Test public void testEmptyString() {
    assertEquals("", StringUtils.expandLeadingTabs(""));
  }

  @Test public void testFinalTab() {
    assertEquals("AXY\t", StringUtils.expandLeadingTabs("AXY\t"));
  }

  @Test public void testInnerTab() {
    assertEquals("A\tB\t", StringUtils.expandLeadingTabs("A\tB\t"));
  }

  @Test public void testLeadingInnerFinalTab() {
    assertEquals("        A\tX\tY\t", StringUtils.expandLeadingTabs("\t\tA\tX\tY\t"));
  }

  @Test public void testNonEmptyString() {
    assertEquals("ABC", StringUtils.expandLeadingTabs("ABC"));
  }

  @Test public void testOneTab() {
    assertEquals("    ", StringUtils.expandLeadingTabs("\t"));
  }

  @Test public void testSpaceThenTab() {
    assertEquals(StringUtils.visualize(" \tX"), StringUtils.visualize(StringUtils.expandLeadingTabs(" \tX")));
  }

  @Test public void testTabThenSpace() {
    assertEquals("     X", StringUtils.expandLeadingTabs("\t X"));
  }

  @Test public void testThreeTabs() {
    assertEquals("            ", StringUtils.expandLeadingTabs("\t\t\t"));
  }

  @Test public void testTwoLinesTabDOSMode() {
    assertEquals("\r\nA\r\n    B", StringUtils.expandLeadingTabs("\r\nA\r\n\tB"));
  }

  @Test public void testTwoLinesTabUnixMode() {
    assertEquals("\nA\n    B", StringUtils.expandLeadingTabs("\nA\n\tB"));
  }

  @Test public void testTwoTabs() {
    assertEquals("        ", StringUtils.expandLeadingTabs("\t\t"));
  }
}
