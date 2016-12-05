package il.org.spartan.athenizer;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/* converts (a?b:c;) to (if(a) b; else c;)
 * @author Raviv Rachmiel
 * @since 03-12-16
 */
public class TernaryExpander extends ReplaceCurrentNode<ConditionalExpression> {
  
  @Override public ASTNode replacement(ConditionalExpression ¢) {
    IfStatement $ = ¢.getAST().newIfStatement();
    $.setExpression(duplicate.of(¢.getExpression()));
    ReturnStatement then = ¢.getAST().newReturnStatement();
    then.setExpression(duplicate.of(¢.getThenExpression()));
    $.setThenStatement(duplicate.of(az.statement(then)));
    ReturnStatement elze = ¢.getAST().newReturnStatement();
    elze.setExpression(duplicate.of(¢.getElseExpression()));
    $.setElseStatement(duplicate.of(az.statement(elze)));   
    return $;
  }

  @Override public String description(@SuppressWarnings("unused") ConditionalExpression __) {
    return "expanding a ternary operator to a full if-else statement";
  }
}
