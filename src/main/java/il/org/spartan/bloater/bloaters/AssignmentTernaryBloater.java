package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts (a?b:c;) to (if(a) b; else c;) relevant for assignment <ternary>
 * also relevant for assignment (<ternary>) s.e $ = (<ternary) Issue #883
 * {@link Issue883}
 * @author Raviv Rachmiel
 * @since 23-12-16 */
public class AssignmentTernaryBloater extends ReplaceCurrentNode<ExpressionStatement>//
    implements TipperCategory.Bloater {
  private static ASTNode innerAssignReplacement(final Expression x, final Expression left, final Operator o) {
    final ConditionalExpression $ = az.conditionalExpression(core(x));
    return $ == null ? null
        : subject
            .pair(
                copy.of(az.expressionStatement($.getAST().newExpressionStatement(subject.pair(copy.of(left), copy.of($.getThenExpression())).to(o)))),
                copy.of(az.expressionStatement($.getAST().newExpressionStatement(subject.pair(copy.of(left), copy.of($.getElseExpression())).to(o)))))
            .toIf(copy.of($.getExpression()));
  }

  private static ASTNode replaceAssignment(final Statement ¢) {
    final ExpressionStatement expressionStatement = az.expressionStatement(¢);
    if (expressionStatement == null)
      return null;
    final Assignment $ = az.assignment(expressionStatement.getExpression());
    return $ == null ? null : innerAssignReplacement($.getRightHandSide(), $.getLeftHandSide(), $.getOperator());
  }

  @Override public ASTNode replacement(final ExpressionStatement ¢) {
    return replaceAssignment(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Expanding a ternary operator to a full if-else statement";
  }
}
