package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** removes continue in for loop if it's last statement in the loop.
 * @author Doron Meshulam
 * @since 2016-11-26 */
public class ForRedundantContinue extends CarefulTipper<ForStatement> implements TipperCategory.SyntacticBaggage {
  @Override public String description(final ForStatement ¢) {
    return "Eliminate redundant " + lastStatement(¢);
  }

  @Override public String description() {
    return "Eliminate redundant continue";
  }

  static Statement lastStatement(final ForStatement ¢) {
    return !iz.block(¢.getBody()) ? ¢.getBody() : (Statement) az.block(¢.getBody()).statements().get(az.block(¢.getBody()).statements().size() - 1);
  }

  @Override public Tip tip(final ForStatement ¢) {
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        // remove(r, lastStatement(¢), g);
        final ASTNode b = az.block(step.body(¢));
        if (!(b instanceof Block))
          r.replace(lastStatement(¢), make.emptyStatement(¢), g);
        else
          step.statements(az.block(step.body(¢))).remove(lastStatement(¢));
      }
    };
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    final Statement s = lastStatement(¢);
    if (s instanceof ContinueStatement) {
      SimpleName l = ((ContinueStatement) s).getLabel();
      if (l == null
          || ¢.getParent() instanceof LabeledStatement && l.getIdentifier().equals(((LabeledStatement) ¢.getParent()).getLabel().getIdentifier()))
        return true;
    }
    return false;
  }
}
