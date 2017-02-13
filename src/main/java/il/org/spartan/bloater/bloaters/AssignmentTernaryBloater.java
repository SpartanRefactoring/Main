package il.org.spartan.bloater.bloaters;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.extract.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts {@code (a?b:c;)} to {@code (if(a) b; else c;)} relevant for
 * assignment <em>ternary</em> also relevant for assignment (<em>ternary</em>)
 * s.e $ = (<ternary) Issue #883 {@link Issue883}
 * @author Raviv Rachmiel
 * @since 23-12-16 */
public class AssignmentTernaryBloater extends ReplaceCurrentNode<ExpressionStatement>//
    implements TipperCategory.Bloater {
  private static ASTNode innerAssignReplacement(final Expression x, final Expression left, final Operator o) {
    final ConditionalExpression $ = az.conditionalExpression(core(x));
    return $ == null ? null
        : subject.pair(az.expressionStatement($.getAST().newExpressionStatement(subject.pair(left, then($)).to(o))),
            az.expressionStatement($.getAST().newExpressionStatement(subject.pair(left, elze($)).to(o)))).toIf($.getExpression());
  }

  private static ASTNode replaceAssignment(final Statement ¢) {
    final ExpressionStatement expressionStatement = az.expressionStatement(¢);
    if (expressionStatement == null)
      return null;
    final Assignment $ = az.assignment(expressionStatement.getExpression());
    return $ == null ? null : innerAssignReplacement(right($), left($), $.getOperator());
  }

  @Override public ASTNode replacement(final ExpressionStatement ¢) {
    return replaceAssignment(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final ExpressionStatement __) {
    return "Expanding a ternary operator to a full if-else statement";
  }
}
