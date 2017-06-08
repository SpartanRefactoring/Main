package il.org.spartan.athenizer.zoomers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** Same as ReturnTernaryExpander just for "throw" @link {Issue0998}
 * @author Doron Meshulam {@code doronmmm@hotmail.com}
 * @since 2016-12-26 */
public class ThrowTernaryBloater extends ReplaceCurrentNode<ThrowStatement>//
    implements TipperCategory.Bloater {
  private static final long serialVersionUID = 0x6F38EBD99BF2A49EL;

  private static ASTNode innerThrowReplacement(final Expression x, final Statement s) {
    final ConditionalExpression ¢ = az.conditionalExpression(extract.core(x));
    if (¢ == null)
      return null;
    final IfStatement $ = s.getAST().newIfStatement();
    $.setExpression(copy.of(expression(¢)));
    final ThrowStatement then = ¢.getAST().newThrowStatement();
    then.setExpression(copy.of(¢.getThenExpression()));
    $.setThenStatement(copy.of(az.statement(then)));
    final ThrowStatement elze = ¢.getAST().newThrowStatement();
    elze.setExpression(copy.of(¢.getElseExpression()));
    $.setElseStatement(copy.of(az.statement(elze)));
    return $;
  }
  private static ASTNode replaceReturn(final Statement ¢) {
    final ThrowStatement $ = az.throwStatement(¢);
    return $ == null || !(expression($) instanceof ConditionalExpression) && !(expression($) instanceof ParenthesizedExpression) ? null
        : innerThrowReplacement(expression($), ¢);
  }
  @Override public ASTNode replacement(final ThrowStatement ¢) {
    return replaceReturn(¢);
  }
  @Override public String description(@SuppressWarnings("unused") final ThrowStatement __) {
    return "expanding a ternary operator to a full if-else statement";
  }
}
