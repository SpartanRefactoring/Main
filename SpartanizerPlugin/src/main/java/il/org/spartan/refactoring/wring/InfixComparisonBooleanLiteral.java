package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.utils.Plant.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

/** eliminates redundant comparison with the two boolean literals:
 *
 * <pre>
 * <b>true</b>
 * </pre>
 *
 * and
 *
 * <pre>
 * <b>false</b>
 * </pre>
 *
 * .
 * @author Yossi Gil
 * @since 2015-07-17 */
public final class InfixComparisonBooleanLiteral extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NoImpact {
  private static BooleanLiteral literal(final InfixExpression e) {
    return az.booleanLiteral(core(literalOnLeft(e) ? expose.left(e) : expose.right(e)));
  }

  private static boolean literalOnLeft(final InfixExpression e) {
    return iz.booleanLiteral(core(expose.left(e)));
  }

  private static boolean literalOnRight(final InfixExpression e) {
    return iz.booleanLiteral(core(expose.right(e)));
  }

  private static boolean negating(final InfixExpression e, final BooleanLiteral l) {
    return l.booleanValue() != (e.getOperator() == EQUALS);
  }

  private static Expression nonLiteral(final InfixExpression e) {
    return literalOnLeft(e) ? expose.right(e) : expose.left(e);
  }

  @Override String description(final InfixExpression e) {
    return "Eliminate redundant comparison with '" + literal(e) + "'";
  }

  @Override Expression replacement(final InfixExpression e) {
    final BooleanLiteral literal = literal(e);
    final Expression nonliteral = core(nonLiteral(e));
    return plant(!negating(e, literal) ? nonliteral : il.org.spartan.refactoring.utils.make.logicalNot(nonliteral)).into(e.getParent());
  }

  @Override public boolean scopeIncludes(final InfixExpression e) {
    return !e.hasExtendedOperands() && in(e.getOperator(), EQUALS, NOT_EQUALS) && (literalOnLeft(e) || literalOnRight(e));
  }
}
