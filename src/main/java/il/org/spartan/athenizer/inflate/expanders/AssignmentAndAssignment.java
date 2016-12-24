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

/** Convert (a=b=#;) to (a=3;b=#;)
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-24 */
public class AssignmentAndAssignment extends CarefulTipper<ExpressionStatement> implements TipperCategory.InVain {
  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Split assignment statement";
  }

  @Override public Tip tip(final ExpressionStatement ¢) {
    // System.out.println("@@@@@@@@Toolbox worked!");
    final Expression e = expression(¢);
    if (!iz.assignment(e))
      return null;
    final Assignment a = az.assignment(e);
    return !iz.assignment(right(a)) ? null : new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        AST create = ¢.getAST();
        Assignment newA = create.newAssignment();
        newA.setLeftHandSide(duplicate.of(left(a)));
        Assignment p = a;
        while (iz.assignment(right(p))) {
          p = (Assignment) right(p);
        }
        newA.setRightHandSide(duplicate.of(right(p)));
        ExpressionStatement head = create.newExpressionStatement(newA);
        ExpressionStatement tail = create.newExpressionStatement(duplicate.of(right(a)));
        az.block(¢.getParent());
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter(head, ¢, g);
        l.insertAfter(tail, ¢, g);
        l.remove(¢, g);
      }
    };
  }
}
