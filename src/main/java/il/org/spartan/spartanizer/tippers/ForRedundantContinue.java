package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** removes continue in for loop if it's last statement in the loop.
 * link {Issue0147}
 * @author Doron Meshulam
 * @since 2016-11-26 */
public class ForRedundantContinue extends CarefulTipper<ForStatement>//
    implements TipperCategory.Shortcircuit {
  @Override public String description(final ForStatement ¢) {
    return "Prune redundant " + wizard.lastStatement(¢);
  }

  @Override public String description() {
    return "Prune redundant continue";
  }

  @Override public Tip tip(final ForStatement ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Block b = az.block(step.body(¢));
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        if (b == null) {
          l.replace(wizard.lastStatement(¢), make.emptyStatement(¢), g);
//          r.replace(wizard.lastStatement(¢), make.emptyStatement(¢), g);
        } else {
//          step.statements(b).remove(wizard.lastStatement(¢));
          l.remove(wizard.lastStatement(¢), g);
        }
      }
    };
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    final Statement s = wizard.lastStatement(¢);
    if (iz.continueStatement(s)) {
      final SimpleName n = label(az.continueStatement(s));
      if (n == null || iz.labeledStatement(parent(¢)) && n.getIdentifier().equals(((LabeledStatement) ¢.getParent()).getLabel().getIdentifier()))
        return true;
    }
    return false;
  }
}
