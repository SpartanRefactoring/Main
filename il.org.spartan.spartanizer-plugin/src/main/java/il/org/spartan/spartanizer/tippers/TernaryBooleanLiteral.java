package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** {@code
 * a ? b : c
 * } is the same as {@code
 * (a && b) || (!a && c)
 * } if b is false than: {@code
 * (a && false) || (!a && c) == (!a && c)
 * } if b is true than: {@code
 * (a && true) || (!a && c) == a || (!a && c) == a || c
 * } if c is false than: {@code
 * (a && b) || (!a && false) == (!a && c)
 * } if c is true than {@code
 * (a && b) || (!a && true) == (a && b) || (!a) == !a || b
 * } keywords <code>
 * <b>this</b>
 * </code> or <code> <b>null</b> Consider an expression {@code a ? b : c}. It is
 * logically equivalent to {@code (a && b) || (!a && c)}
 * <ol>
 * <li>if b is false then: {@code
  (a && false) || (!a && c) == !a && c
  }
 * <li>if b is true then: {@code
  (a && true) || (!a && c) == a || (!a && c) == a || c
  }
 * <li>if c is false then: {@code
  (a && b) || (!a && false) == a && b
  }
 * <li>if c is true then {@code
  (a && b) || (!a && true) == !a || b
  }
 * </ol>
 * @author Yossi Gil
 * @since 2015-07-20 */
public final class TernaryBooleanLiteral extends ReplaceCurrentNode<ConditionalExpression> //
    implements Category.Transformation.Reshape {
  private static final long serialVersionUID = 0x16FBB28C0081E600L;

  private static Expression simplifyTernary(final ConditionalExpression ¢) {
    return simplifyTernary(¢.getThenExpression(), ¢.getElseExpression(), copy.of(¢.getExpression()));
  }
  private static Expression simplifyTernary(final Expression then, final Expression elze, final Expression main) {
    final boolean $ = !iz.booleanLiteral(then);
    final Expression other = $ ? then : elze;
    final boolean literal = az.booleanLiteral($ ? elze : then).booleanValue();
    return subject.pair(literal != $ ? main : make.notOf(main), other).to(literal ? CONDITIONAL_OR : CONDITIONAL_AND);
  }
  @Override public String description(@SuppressWarnings("unused") final ConditionalExpression __) {
    return "Convert ?: into Boolean expression";
  }
  @Override public boolean prerequisite(final ConditionalExpression ¢) {
    return have.booleanLiteral(¢.getThenExpression(), ¢.getElseExpression());
  }
  @Override public Expression replacement(final ConditionalExpression ¢) {
    return simplifyTernary(¢);
  }
}
