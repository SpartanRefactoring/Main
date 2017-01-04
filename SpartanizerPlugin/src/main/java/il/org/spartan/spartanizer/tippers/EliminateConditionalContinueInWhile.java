package il.org.spartan.spartanizer.tippers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Eliminate conditional continue before last statement in a while loop </br>
 * Issue #1014
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-01-04 */
public class EliminateConditionalContinueInWhile extends EagerTipper<WhileStatement> implements TipperCategory.Inlining {
  @Override public String description(@SuppressWarnings("unused") WhileStatement __) {
    return "Eliminate conditional continue before last statement in the for loop";
  }

  @Override public Tip tip(final WhileStatement s) {
    Block b = az.block(s.getBody());
    if (iz.whileStatement(s))
      b = az.block(az.whileStatement(s).getBody());
    if (b == null || step.statements(b).size() < 2)
      return null;
    List<Statement> lst = step.statements(b);
    IfStatement continueStatement = az.ifStatement(lst.get(lst.size() - 2));
    if (continueStatement == null || !iz.continueStatement(continueStatement.getThenStatement()))
      return null;
    IfStatement replacementIf = subject.pair(duplicate.of(lst.get(lst.size() - 1)), null).toNot(continueStatement.getExpression());
    return new Tip(description(s), s, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(lst.get(lst.size() - 1), g);
        r.replace(continueStatement, replacementIf, g);
      }
    };
  }
}
