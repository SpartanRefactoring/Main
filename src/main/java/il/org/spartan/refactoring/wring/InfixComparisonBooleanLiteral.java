package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.assemble.plant.*;
import static il.org.spartan.refactoring.ast.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

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
  private static BooleanLiteral literal(final InfixExpression x) {
    return az.booleanLiteral(core(literalOnLeft(x) ? step.left(x) : step.right(x)));
  }

  private static boolean literalOnLeft(final InfixExpression x) {
    return iz.booleanLiteral(core(step.left(x)));
  }

  private static boolean literalOnRight(final InfixExpression x) {
    return iz.booleanLiteral(core(step.right(x)));
  }

  private static boolean negating(final InfixExpression x, final BooleanLiteral l) {
    return l.booleanValue() != (x.getOperator() == EQUALS);
  }

  private static Expression nonLiteral(final InfixExpression x) {
    return literalOnLeft(x) ? step.right(x) : step.left(x);
  }

  @Override public boolean scopeIncludes(final InfixExpression x) {
    return !x.hasExtendedOperands() && in(x.getOperator(), EQUALS, NOT_EQUALS) && (literalOnLeft(x) || literalOnRight(x));
  }

  @Override String description(final InfixExpression x) {
    return "Eliminate redundant comparison with '" + literal(x) + "'";
  }

  @Override public boolean claims(final InfixExpression e) {
    return !e.hasExtendedOperands() && in(e.getOperator(), EQUALS, NOT_EQUALS) && (literalOnLeft(e) || literalOnRight(e));
  }
}
