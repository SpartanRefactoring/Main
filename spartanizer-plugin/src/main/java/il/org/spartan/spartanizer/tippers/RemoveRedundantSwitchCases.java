package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.engine.nominal.Trivia;
import il.org.spartan.spartanizer.issues.Issue0880;
import il.org.spartan.spartanizer.issues.Issue0913;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code switch (x) { case a: case b: x = 5; break; case c: default:
 * case d: break; } switch (x) { case a: case b: x = 5; break; default: } } into
 * {@code
* (some commands)
 * } . Tested in {@link Issue0880} and {@link Issue0913}
 * @author Yuval Simon
 * @since 2016-11-27 */
public class RemoveRedundantSwitchCases extends CarefulTipper<SwitchCase>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = -0x173EADC6209210E7L;

  @Override public Tip tip(final SwitchCase myCase) {
    final SwitchCase $ = az.switchCase(extract.nextStatementInBlock(myCase));
    return new Tip(description(myCase), getClass(), myCase) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(!$.isDefault() ? $ : myCase, g);
      }
    }.spanning($);
  }
  @Override protected boolean prerequisite(final SwitchCase n) {
    final SwitchCase $ = az.switchCase(extract.nextStatementInBlock(n));
    return $ != null && ($.isDefault() || n.isDefault());
  }
  @Override public String description(final SwitchCase n) {
    return "Remove switch case " + Trivia.gist(n);
  }
}