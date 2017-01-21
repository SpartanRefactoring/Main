package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

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
 * @author Doron Meshulam
 * @since 2016-11-26 */
public class ForRedundantContinue extends CarefulTipper<ForStatement>//
    implements TipperCategory.Shunt {
  @Override public String description(final ForStatement ¢) {
    return "Prune redundant " + lastStatement(¢);
  }

  @Override public String description() {
    return "Prune redundant continue";
  }

  // TODO: Doron Meshulam: please use lisp.last --yg
  // Also, move to class wizard
  static Statement lastStatement(final ForStatement ¢) {
    return !iz.block(body(¢)) ? body(¢) : last(statements(az.block(body(¢))));
  }

  @Override public Tip tip(final ForStatement ¢) {
    return new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Block b = az.block(step.body(¢));
        if (b == null)
          r.replace(lastStatement(¢), make.emptyStatement(¢), g);
        else {
          // TODO: Doron Meshulam: use list rewrite (search for code that does
          // that) --yg
          step.statements(b).remove(lastStatement(¢));
          // TODO: Doron Meshulam: seems like a noop --yg
          r.replace(b, copy.of(b), g);
        }
      }
    };
  }

  @Override public boolean prerequisite(final ForStatement ¢) {
    final Statement s = lastStatement(¢);
    if (iz.continueStatement(s)) {
      final SimpleName n = label(az.continueStatement(s));
      if (n == null || iz.labeledStatement(parent(¢)) && n.getIdentifier().equals(((LabeledStatement) ¢.getParent()).getLabel().getIdentifier()))
        return true;
    }
    return false;
  }
}
