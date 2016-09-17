package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.idiomatic.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** Replace <code>(double)X</code> by <code>1.*X</code>
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class CastToDouble2Multiply1 extends ReplaceCurrentNode<CastExpression> implements Kind.NOP {
  private static NumberLiteral literal(final Expression x) {
    final NumberLiteral $ = x.getAST().newNumberLiteral();
    $.setToken("1.");
    return $;
  }

  private static InfixExpression replacement(final Expression $) {
    return subject.pair(literal($), $).to(TIMES);
  }

  @Override public String description(final CastExpression ¢) {
    return "Use 1.*" + step.expression(¢) + " instead of (double)" + step.expression(¢);
  }

  @Override public ASTNode replacement(final CastExpression ¢) {
    return eval(//
        () -> replacement(step.expression(¢))//
    ).when(//
        step.type(¢).isPrimitiveType() && "double".equals(step.type(¢) + "") //
    );
  }
}
