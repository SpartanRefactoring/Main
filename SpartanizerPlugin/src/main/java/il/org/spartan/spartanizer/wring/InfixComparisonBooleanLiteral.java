package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.assemble.plant.*;
import static il.org.spartan.spartanizer.ast.extract.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

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
public final class InfixComparisonBooleanLiteral extends Wring.ReplaceCurrentNode<InfixExpression> implements Kind.NOP {
  private static BooleanLiteral literal(final InfixExpression x) {
    return az.booleanLiteral(core(literalOnLeft(x) ? left(x) : right(x)));
  }

  private static boolean literalOnLeft(final InfixExpression x) {
    return iz.booleanLiteral(core(left(x)));
  }

  private static boolean literalOnRight(final InfixExpression x) {
    return iz.booleanLiteral(core(right(x)));
  }

  private static boolean negating(final InfixExpression x, final BooleanLiteral l) {
    return l.booleanValue() != (x.getOperator() == EQUALS);
  }

  private static Expression nonLiteral(final InfixExpression x) {
    return literalOnLeft(x) ? right(x) : left(x);
  }

  @Override public boolean scopeIncludes(final InfixExpression x) {
    return !x.hasExtendedOperands() && in(x.getOperator(), EQUALS, NOT_EQUALS) && (literalOnLeft(x) || literalOnRight(x));
  }

  @Override String description(final InfixExpression x) {
    return "Eliminate redundant comparison with '" + literal(x) + "'";
  }

  @Override Expression replacement(final InfixExpression x) {
    final BooleanLiteral literal = literal(x);
    final Expression nonliteral = core(nonLiteral(x));
    return plant(!negating(x, literal) ? nonliteral : make.notOf(nonliteral)).into(x.getParent());
  }
}
