package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts (a?b:c;) to (if(a) b; else c;) relevant for assignment <ternary>
 * also relevant for assignment (<ternary>) s.e $ = (<ternary) Issue #883
 * {@link Issue883}
 * @author Raviv Rachmiel
 * @since 23-12-16 */
public class AssignmentTernaryExpander extends ReplaceCurrentNode<ExpressionStatement> implements TipperCategory.Expander {
  private static ASTNode innerAssignReplacement(final Expression x, final Statement s, final Expression left, final Operator o) {
    ConditionalExpression ¢ = az.conditionalExpression(!(x instanceof ParenthesizedExpression) ? x : az.parenthesizedExpression(x).getExpression());
    if (¢ == null)
      return null;
    // TODO: Raviv use class subject --yg
    final IfStatement $ = s.getAST().newIfStatement();
    $.setExpression(copy.of(¢.getExpression()));
    final Assignment then = ¢.getAST().newAssignment();
    then.setRightHandSide(copy.of(¢.getThenExpression()));
    then.setLeftHandSide(copy.of(left));
    then.setOperator(o);
    $.setThenStatement(copy.of(az.expressionStatement(¢.getAST().newExpressionStatement(then))));
    final Assignment elze = ¢.getAST().newAssignment();
    elze.setRightHandSide(copy.of(¢.getElseExpression()));
    elze.setLeftHandSide(copy.of(left));
    elze.setOperator(o);
    $.setElseStatement(copy.of(az.expressionStatement(¢.getAST().newExpressionStatement(elze))));
    return $;
  }

  private static ASTNode replaceAssignment(final Statement ¢) {
    if (az.expressionStatement(¢) == null)
      return null;
    final Assignment $ = az.assignment(az.expressionStatement(¢).getExpression());
    return $ == null ? null : innerAssignReplacement($.getRightHandSide(), ¢, $.getLeftHandSide(), $.getOperator());
  }

  @Override public ASTNode replacement(final ExpressionStatement ¢) {
    return replaceAssignment(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "expanding a ternary operator to a full if-else statement";
  }
}
