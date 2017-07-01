package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

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
    final SwitchCase ret = az.switchCase(extract.nextStatementInBlock(myCase));
    return new Tip(description(myCase), getClass(), myCase) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(!ret.isDefault() ? ret : myCase, g);
      }
    }.spanning(ret);
  }
  @Override protected boolean prerequisite(final SwitchCase n) {
    final SwitchCase $ = az.switchCase(extract.nextStatementInBlock(n));
    return $ != null && ($.isDefault() || n.isDefault());
  }
  @Override public String description(final SwitchCase n) {
    return "Remove switch case " + Trivia.gist(n);
  }
}