package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.assemble.make.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.step.*;

import static il.org.spartan.spartanizer.ast.extract.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** eliminates redundant comparison with <code><b>true</b> </code> and
 * <code><b>false</b></code> .
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixComparisonBooleanLiteral extends ReplaceCurrentNode<InfixExpression> implements Kind.Collapse {
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
    return "Eliminate redundant comparison with '" + literal(¢) + "'";
  }

  @Override public boolean prerequisite(final InfixExpression ¢) {
    return !¢.hasExtendedOperands() && in(¢.getOperator(), EQUALS, NOT_EQUALS) && (literalOnLeft(¢) || literalOnRight(¢));
  }

  @Override public Expression replacement(final InfixExpression x) {
    final BooleanLiteral literal = literal(x);
    final Expression nonliteral = core(nonLiteral(x));
    return plant(!negating(x, literal) ? nonliteral : make.notOf(nonliteral)).into(x.getParent());
  }
}
