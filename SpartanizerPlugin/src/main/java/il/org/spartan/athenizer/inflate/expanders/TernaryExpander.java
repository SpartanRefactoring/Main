package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts (a?b:c;) to (if(a) b; else c;) relevant for now to return <ternary>
 * or $ = <ternary> also relevant for return (<ternary>) or $ = (<ternary)
 * @author Raviv Rachmiel
 * @since 03-12-16 */
public class TernaryExpander extends ReplaceCurrentNode<Statement> {
  private static ASTNode innerReturnReplacement(final Expression x, final Statement s) {
    ConditionalExpression ¢;
    if (!(x instanceof ParenthesizedExpression))
      ¢ = az.conditionalExpression(x);
    else {
      final Expression unpar = az.parenthesizedExpression(x).getExpression();
      if (!(unpar instanceof ConditionalExpression))
        return null;
      ¢ = az.conditionalExpression(unpar);
    }
    final IfStatement $ = s.getAST().newIfStatement();
    $.setExpression(duplicate.of(¢.getExpression()));
    final ReturnStatement then = ¢.getAST().newReturnStatement();
    then.setExpression(duplicate.of(¢.getThenExpression()));
    $.setThenStatement(duplicate.of(az.statement(then)));
    final ReturnStatement elze = ¢.getAST().newReturnStatement();
    elze.setExpression(duplicate.of(¢.getElseExpression()));
    $.setElseStatement(duplicate.of(az.statement(elze)));
    return $;
  }

  private static ASTNode innerAssignReplacement(final Expression x, final Statement s, final Expression left) {
    ConditionalExpression ¢;
    if (!(x instanceof ParenthesizedExpression))
      ¢ = az.conditionalExpression(x);
    else {
      final Expression unpar = az.parenthesizedExpression(x).getExpression();
      if (!(unpar instanceof ConditionalExpression))
        return null;
      ¢ = az.conditionalExpression(unpar);
    }
    final IfStatement $ = s.getAST().newIfStatement();
    $.setExpression(duplicate.of(¢.getExpression()));
    final Assignment then = ¢.getAST().newAssignment();
    then.setRightHandSide(duplicate.of(¢.getThenExpression()));
    then.setLeftHandSide(left);
    $.setThenStatement(duplicate.of(az.statement(then)));
    final Assignment elze = ¢.getAST().newAssignment();
    elze.setRightHandSide(duplicate.of(¢.getElseExpression()));
    elze.setLeftHandSide(left);
    $.setElseStatement(duplicate.of(az.statement(elze)));
    return $;
  }

  private static ASTNode replaceAssignment(final Statement ¢) {
    if (az.expressionStatement(¢) == null)
      return null;
    final Assignment $ = az.assignment(az.expressionStatement(¢).getExpression());
    return $ == null ? null : innerAssignReplacement($.getRightHandSide(), ¢, $.getLeftHandSide());
  }

  private static ASTNode replaceReturn(final Statement ¢) {
    final ReturnStatement $ = az.returnStatement(¢);
    return $ == null || !($.getExpression() instanceof ConditionalExpression) && !($.getExpression() instanceof ParenthesizedExpression) ? null
        : innerReturnReplacement($.getExpression(), ¢);
  }

  @Override public ASTNode replacement(final Statement ¢) {
    final ASTNode $ = replaceReturn(¢);
    return $ != null ? $ : replaceAssignment(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final Statement __) {
    return "expanding a ternary operator to a full if-else statement";
  }
}
