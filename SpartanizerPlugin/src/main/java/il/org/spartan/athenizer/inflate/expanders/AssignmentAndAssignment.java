package il.org.spartan.athenizer.inflate.expanders;


import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

//TODO: Doron, spartanize your code, add @author, @since, @description
public class AssignmentAndAssignment extends CarefulTipper<ExpressionStatement> implements TipperCategory.InVain { 

  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Split assignment statement";
  }
  
  @Override public Tip tip(final ExpressionStatement ¢) {
    final Expression e = expression(¢);
    if (!iz.assignment(e))
      return null;
    final Assignment $ = az.assignment(e);
    return !iz.assignment(right($)) || iz.assignment(right($)) && iz.assignment(right(az.assignment(right($)))) ? null
        : new Tip(description(¢), ¢, this.getClass()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            AST create = ¢.getAST();
            Assignment newA = create.newAssignment();
            newA.setLeftHandSide(left($));
            newA.setRightHandSide(right(az.assignment(right($))));
            ExpressionStatement head = create.newExpressionStatement(newA);
            ExpressionStatement tail = create.newExpressionStatement(right($));
            az.block(¢.getParent());
            final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);
            l.insertAfter(head, ¢, g);
            l.insertAfter(tail, ¢, g);
            l.remove(¢, g);
          }
        };
  }
}
