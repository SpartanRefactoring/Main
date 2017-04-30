package il.org.spartan.spartanizer.tippers;
import static fluent.ly.idiomatic.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace {@code (long)X} by {@code 1L*X}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class CastToLong2Multiply1L extends ReplaceCurrentNode<CastExpression>//
    implements TipperCategory.NOP {
  private static final long serialVersionUID = -0x6ACC6AD3D77236F4L;

  private static NumberLiteral literal(final Expression ¢) {
    final NumberLiteral $ = ¢.getAST().newNumberLiteral();
    $.setToken("1L");
    return $;
  }

  private static InfixExpression replacement(final Expression $) {
    return subject.pair(literal($), $).to(TIMES);
  }

  @Override public String description(final CastExpression ¢) {
    return "Use 1L*" + expression(¢) + " instead of (long)" + expression(¢);
  }

  @Override public ASTNode replacement(final CastExpression ¢) {
    return eval(() -> replacement(expression(¢)))
        .when(step.type(¢).isPrimitiveType() && "long".equals(step.type(¢) + "") && type.of(expression(¢)).isIntegral());
  }
}
