package il.org.spartan.athenizer.inflate.expanders;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** converts (a?b:c;) to (if(a) b; else c;) relevant to return <ternary> also
 * relevant for return (<ternary>)
 * @author Raviv Rachmiel
 * @since 03-12-16 */
public class ReturnTernaryExpander extends ReplaceCurrentNode<ReturnStatement> implements TipperCategory.InVain {
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

  private static ASTNode replaceReturn(final Statement ¢) {
    final ReturnStatement $ = az.returnStatement(¢);
    return $ == null || !($.getExpression() instanceof ConditionalExpression) && !($.getExpression() instanceof ParenthesizedExpression) ? null
        : innerReturnReplacement($.getExpression(), ¢);
  }

  @Override public ASTNode replacement(final ReturnStatement ¢) {
    return replaceReturn(¢);
  }

  @Override public String description(@SuppressWarnings("unused") final ReturnStatement __) {
    return "expanding a ternary operator to a full if-else statement";
  }
}
