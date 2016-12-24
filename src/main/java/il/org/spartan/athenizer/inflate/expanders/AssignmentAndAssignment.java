package il.org.spartan.athenizer.inflate.expanders;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public class AssignmentAndAssignment extends CarefulTipper<ExpressionStatement> implements TipperCategory.InVain { 

  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Split assignment statement";
  }
  
  @Override public Tip tip(final ExpressionStatement ¢) {
//    System.out.println("@@@@@@@@Toolbox worked!");
    final Expression e = expression(¢);
    if (!iz.assignment(e))
      return null;
    final Assignment a = az.assignment(e);
    if (!iz.assignment(right(a)) || iz.assignment(right(a)) 
        && iz.assignment(right(az.assignment(right(a)))))
      return null;
//    System.out.println("@@@@@@@@@@Identification worked!");
    return new Tip(description(¢), ¢, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        AST create = ¢.getAST();
        Assignment newA = create.newAssignment();
        newA.setLeftHandSide(left(a));
        newA.setRightHandSide(right(az.assignment(right(a))));
        
        ExpressionStatement head = create.newExpressionStatement(newA);
        ExpressionStatement tail = create.newExpressionStatement(right(a));    
        
        az.block(¢.getParent());
        final ListRewrite l = r.getListRewrite(¢.getParent(), Block.STATEMENTS_PROPERTY);        
        l.insertAfter(head, ¢, g);
        l.insertAfter(tail, ¢, g);
        l.remove(¢, g);
      }
    };
  }
}
