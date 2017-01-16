package il.org.spartan.bloater.bloaters;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts (a?b:c;) to (if(a) b; else c;) relevant to return <ternary> also
 * relevant for return (<ternary>) Issue #883 {@link Issue883}
 * @author Raviv Rachmiel
 * @since 03-12-16 */
public class ReturnTernaryExpander extends ReplaceCurrentNode<ReturnStatement> implements TipperCategory.Expander {
  private static ASTNode innerReturnReplacement(final Expression x, final Statement s) {
    ConditionalExpression ¢;
    if (!(x instanceof ParenthesizedExpression))
      ¢ = az.conditionalExpression(x);
    else {
      final Expression unpar = expression(az.parenthesizedExpression(x));
      if (!(unpar instanceof ConditionalExpression))
        return null;
      ¢ = az.conditionalExpression(unpar);
      if (¢ == null)
        return null;
    }
    final IfStatement $ = s.getAST().newIfStatement();
    $.setExpression(copy.of(expression(¢)));
    final ReturnStatement then = ¢.getAST().newReturnStatement();
    then.setExpression(copy.of(¢.getThenExpression()));
    $.setThenStatement(copy.of(az.statement(then)));
    final ReturnStatement elze = ¢.getAST().newReturnStatement();
    elze.setExpression(copy.of(¢.getElseExpression()));
    $.setElseStatement(copy.of(az.statement(elze)));
    return $;
  }

  private static ASTNode replaceReturn(final Statement ¢) {
    final ReturnStatement $ = az.returnStatement(¢);
    return $ == null || !($.getExpression() instanceof ConditionalExpression) && !(expression($) instanceof ParenthesizedExpression) ? null
        : innerReturnReplacement(expression($), ¢);
  }

  @Override public ASTNode replacement(final ReturnStatement ¢) {
    return replaceReturn(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final ReturnStatement __) {
    return "expanding a ternary operator to a full if-else statement";
  }
}
