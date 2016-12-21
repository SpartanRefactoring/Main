package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TESTUtils.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;

/** Testing utils for expander
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-19 */
public class ExpanderTestUtils {
  @SuppressWarnings({ "rawtypes", "unchecked" }) public static String applyExpander(final ReplaceCurrentNode n, final String from) {
    final ASTNode $ = wizard.ast(from);
    assert $ != null;
    final ASTNode ret = n.replacement($);
    return ret == null ? from : ret + "";
  }

  /** check that the expender over input returns what we expect
   * @param from the original signal
   * @param expected the desired signal
   * @param n an instance of the expander */
  public static void expanderCheck(final String from, final String expected, final Tipper<? extends ASTNode> n) {
    final String unpeeled = apply(n, from);
    if (from.equals(unpeeled))
      azzert.fail("Nothing done on " + from);
    if (tide.clean(unpeeled).equals(tide.clean(from)))
      azzert.that("Simpification of " + from + " is just reformatting", tide.clean(from), is(not(tide.clean(unpeeled))));
    assertSimilar(expected, unpeeled);
  }

  /** Validate that the expander does nothing over a given input
   * @param from
   * @param n */
  public static void expanderCheckStays(final String from, final Tipper<? extends ASTNode> n) {
    assertSimilar(from, apply(n, from));
  }

  @SuppressWarnings("rawtypes") private static String apply(final Tipper<? extends ASTNode> n, final String from) {
    return !(n instanceof ReplaceCurrentNode) ? "" : applyExpander((ReplaceCurrentNode) n, from);
  }
}
