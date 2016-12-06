package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/* converts (a?b:c;) to (if(a) b; else c;)
 * relevant for now to return <ternary> or $ = <ternary> 
 * also relevant for return (<ternary>) or $ = (<ternary)
 * @author Raviv Rachmiel
 * @since 03-12-16
 */
public class TernaryExpander extends ReplaceCurrentNode<Statement> {
  
  @Override public ASTNode replacement(Statement s) {
    if(!(s instanceof ReturnStatement)) 
      return null;
    ReturnStatement __ = az.returnStatement(s); 
    if(!(__.getExpression() instanceof ConditionalExpression) && !(__.getExpression() instanceof ParenthesizedExpression))
      return null;
    ConditionalExpression ¢;
    if (!(__.getExpression() instanceof ParenthesizedExpression))
      ¢ = az.conditionalExpression(__.getExpression());
    else {
      Expression unpar = az.parenthesizedExpression(__.getExpression()).getExpression();
      if (!(unpar instanceof ConditionalExpression))
        return null;
      ¢ = az.conditionalExpression(unpar);
    }
    IfStatement $ = s.getAST().newIfStatement();
    $.setExpression(duplicate.of(¢.getExpression()));
    ReturnStatement then = ¢.getAST().newReturnStatement();
    then.setExpression(duplicate.of(¢.getThenExpression()));
    $.setThenStatement(duplicate.of(az.statement(then)));
    ReturnStatement elze = ¢.getAST().newReturnStatement();
    elze.setExpression(duplicate.of(¢.getElseExpression()));
    $.setElseStatement(duplicate.of(az.statement(elze)));   
    return $;
  }
  
  @Override public String description(@SuppressWarnings("unused") Statement __) {
    return "expanding a ternary operator to a full if-else statement";
  }


}
