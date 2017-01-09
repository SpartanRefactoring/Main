package il.org.spartan.zoomer.inflate.zoomers;

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
    ConditionalExpression ¢;
    if (!(x instanceof ParenthesizedExpression))
      ¢ = az.conditionalExpression(x);
    else {
      final Expression unpar = az.parenthesizedExpression(x).getExpression();
      if (!(unpar instanceof ConditionalExpression))
        return null;
      ¢ = az.conditionalExpression(unpar);
      if (¢ == null)
        return null;
    }
    final IfStatement $ = s.getAST().newIfStatement();
    try {
      $.setExpression(copy.of(¢.getExpression()));
      final Assignment then = ¢.getAST().newAssignment();
      then.setRightHandSide(copy.of(¢.getThenExpression()));
      then.setLeftHandSide(copy.of(left));
      then.setOperator(o);
      $.setThenStatement(copy.of(az.expressionStatement(¢.getAST().newExpressionStatement(then))));
      System.out.println("then -" + copy.of(¢.getThenExpression()));
      System.out.println("left - " + copy.of(¢.getThenExpression()));
      if (az.expression(copy.of(¢.getThenExpression())).subtreeMatch(new ASTMatcher(), copy.of(left))) {
        System.out.println("HEY1");
        $.setThenStatement(copy.of(¢.getAST().newContinueStatement()));
      }
      final Assignment elze = ¢.getAST().newAssignment();
      elze.setRightHandSide(copy.of(¢.getElseExpression()));
      elze.setLeftHandSide(copy.of(left));
      elze.setOperator(o);
      $.setElseStatement(copy.of(az.expressionStatement(¢.getAST().newExpressionStatement(elze))));
      System.out.println("else -" + copy.of(¢.getThenExpression()));
      System.out.println("left - " + copy.of(¢.getThenExpression()));
      if (copy.of(¢.getElseExpression()).equals(copy.of(left))) {
        System.out.println("HEY1");
        $.setElseStatement(copy.of(¢.getAST().newContinueStatement()));
      }
    } catch (@SuppressWarnings("unused") final NullPointerException e) {
      return null;
    }
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
