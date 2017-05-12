package il.org.spartan.spartanizer.tippers;
import static fluent.ly.is.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** eliminates redundant comparison with {@code true</b> </code> and
 * {@code false} .
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixComparisonBooleanLiteral extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.NOP.onBooleans {
  private static final long serialVersionUID = 0xB818D6DBD293A8CL;

  private static BooleanLiteral literal(final InfixExpression ¢) {
    return az.booleanLiteral(core(literalOnLeft(¢) ? left(¢) : right(¢)));
  }

  private static boolean literalOnLeft(final InfixExpression ¢) {
    return iz.booleanLiteral(core(left(¢)));
  }

  private static boolean literalOnRight(final InfixExpression ¢) {
    return iz.booleanLiteral(core(right(¢)));
  }

  private static boolean negating(final InfixExpression x, final BooleanLiteral l) {
    return l.booleanValue() != (x.getOperator() == EQUALS);
  }

  private static Expression nonLiteral(final InfixExpression ¢) {
    return literalOnLeft(¢) ? right(¢) : left(¢);
  }

  @Override public String description(final InfixExpression ¢) {
    return "Omit redundant comparison with '" + literal(¢) + "'";
  }

  @Override public boolean prerequisite(final InfixExpression ¢) {
    return !¢.hasExtendedOperands() && in(¢.getOperator(), EQUALS, NOT_EQUALS) && (literalOnLeft(¢) || literalOnRight(¢));
  }

  @Override public Expression replacement(final InfixExpression x) {
    final BooleanLiteral $ = literal(x);
    final Expression nonliteral = core(nonLiteral(x));
    return make.plant(!negating(x, $) ? nonliteral : make.notOf(nonliteral)).into(x.getParent());
  }
}
