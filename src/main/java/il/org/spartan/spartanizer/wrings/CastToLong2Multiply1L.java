package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.idiomatic.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** Replace <code>(long)X</code> by <code>1L*X</code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class CastToLong2Multiply1L extends ReplaceCurrentNode<CastExpression> implements Kind.NOP {
  private static NumberLiteral literal(final Expression ¢) {
    final NumberLiteral $ = ¢.getAST().newNumberLiteral();
    $.setToken("1L");
    return $;
  }

  private static InfixExpression replacement(final Expression $) {
    return subject.pair(literal($), $).to(TIMES);
  }

  @Override public String description(final CastExpression ¢) {
    return "Use 1L*" + step.expression(¢) + " instead of (long)" + step.expression(¢);
  }

  @Override public ASTNode replacement(final CastExpression ¢) {
    return eval(//
        () -> replacement(step.expression(¢))//
    ).when(//
        step.type(¢).isPrimitiveType() && "long".equals(step.type(¢) + "") && type.get(step.expression(¢)).isIntegral() //
    );
  }
}
