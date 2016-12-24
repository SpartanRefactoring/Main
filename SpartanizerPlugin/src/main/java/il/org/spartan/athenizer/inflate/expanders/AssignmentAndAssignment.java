package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
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
    final Assignment $ = az.assignment(e);
    return !iz.assignment(right($)) ? null : new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final AST create = ¢.getAST();
        final Assignment newA = create.newAssignment();
        newA.setLeftHandSide(duplicate.of(left($)));
        Assignment p = $;
        while (iz.assignment(right(p)))
          p = (Assignment) right(p);
        newA.setRightHandSide(duplicate.of(right(p)));
        final ExpressionStatement head = create.newExpressionStatement(newA);
        final ExpressionStatement tail = create.newExpressionStatement(duplicate.of(right($)));
        az.block(¢.getParent());
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter(head, ¢, g);
        l.insertAfter(tail, ¢, g);
        l.remove(¢, g);
      }
    };
  }
}
