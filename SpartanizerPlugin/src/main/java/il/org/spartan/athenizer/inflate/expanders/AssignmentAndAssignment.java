package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Issue #999 Convert (a=b=??;) to (a=3;b=??;)
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-24 */
public class AssignmentAndAssignment extends CarefulTipper<ExpressionStatement> implements TipperCategory.InVain {
  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Split assignment statement";
  }

  @Override public Tip tip(final ExpressionStatement ¢) {
    final Expression e = expression(¢);
    if (!iz.assignment(e))
      return null;
    final Assignment ass = az.assignment(e);
    return !iz.assignment(right(ass)) ? null : new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final AST create = ¢.getAST();
        final Assignment newHead = create.newAssignment();
        Assignment newTail = create.newAssignment();
        newHead.setLeftHandSide(duplicate.of(left(ass)));
        newTail = duplicate.of(az.assignment(right(ass)));
        Assignment p = newTail;
        while (iz.assignment(right(p)))
          p = az.assignment(right(p));
        newHead.setRightHandSide(duplicate.of(right(p)));
        // if (sideEffects.free(right(p))) In the future side effect check
        // should be here
        p.setRightHandSide(duplicate.of(left(ass)));
        final ExpressionStatement head = create.newExpressionStatement(newHead);
        final ExpressionStatement tail = create.newExpressionStatement(newTail);
        az.block(¢.getParent());
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter(tail, ¢, g);
        l.insertAfter(head, ¢, g);
        l.remove(¢, g);
      }
    };
  }
}
