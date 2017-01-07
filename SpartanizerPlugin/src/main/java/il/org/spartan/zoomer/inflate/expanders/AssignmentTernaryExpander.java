package il.org.spartan.zoomer.inflate.expanders;

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
      $.setExpression(duplicate.of(¢.getExpression()));
      final Assignment then = ¢.getAST().newAssignment();
      then.setRightHandSide(duplicate.of(¢.getThenExpression()));
      then.setLeftHandSide(duplicate.of(left));
      then.setOperator(o);
      $.setThenStatement(duplicate.of(az.expressionStatement(¢.getAST().newExpressionStatement(then))));
      System.out.println("then -" + duplicate.of(¢.getThenExpression()));
      System.out.println("left - " + duplicate.of(¢.getThenExpression()));
      if(az.expression(duplicate.of(¢.getThenExpression())).subtreeMatch(new ASTMatcher(), duplicate.of(left))) {
        System.out.println("HEY1");
        $.setThenStatement(duplicate.of(¢.getAST().newContinueStatement()));
      }
      
      final Assignment elze = ¢.getAST().newAssignment();
      elze.setRightHandSide(duplicate.of(¢.getElseExpression()));
      elze.setLeftHandSide(duplicate.of(left));
      elze.setOperator(o);
      $.setElseStatement(duplicate.of(az.expressionStatement(¢.getAST().newExpressionStatement(elze))));
      System.out.println("else -" + duplicate.of(¢.getThenExpression()));
      System.out.println("left - " + duplicate.of(¢.getThenExpression()));
      if(duplicate.of(¢.getElseExpression()).equals(duplicate.of(left))) {
        System.out.println("HEY1");
        $.setElseStatement(duplicate.of(¢.getAST().newContinueStatement()));
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
