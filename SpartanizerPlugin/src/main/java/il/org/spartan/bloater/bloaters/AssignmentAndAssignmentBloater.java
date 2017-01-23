package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.zoomer.zoomin.expanders.*;

/** Convert (a=b=??;) to (a=3;b=??;) Tested in {@link Issue0999}
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-24 */
public class AssignmentAndAssignmentBloater extends CarefulTipper<ExpressionStatement>//
    implements TipperCategory.Bloater {
  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Split assignment statement";
  }
  
  // TODO: Doron - I spartanized your code. --yg

  @Override public Tip tip(final ExpressionStatement ¢) {
    final Assignment $ = az.assignment(expression(¢));
    return $ == null || !iz.assignment(right($)) ? null : new Tip(description(¢), ¢, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final AST create = ¢.getAST();
        Assignment newHead = create.newAssignment();
        final Assignment newTail = copy.of($);
        Assignment p = newTail;
        for (; iz.assignment(right(az.assignment(right(p)))); p = az.assignment(right(p))) {}
        newHead = copy.of(az.assignment(right(p)));
        p.setRightHandSide(copy.of(left(newHead)));
        final ExpressionStatement head = create.newExpressionStatement(newHead), tail = create.newExpressionStatement(newTail);
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
        l.insertAfter(tail, ¢, g);
        l.insertAfter(head, ¢, g);
        l.remove(¢, g);
      }
    };
  }
}
