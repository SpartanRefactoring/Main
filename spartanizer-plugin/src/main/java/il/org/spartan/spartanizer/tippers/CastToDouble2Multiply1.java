package il.org.spartan.spartanizer.tippers;

import static fluent.ly.idiomatic.eval;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.type;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.TIMES;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;

import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Replace {@code (double)X} by {@code 1.*X}
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class CastToDouble2Multiply1 extends ReplaceCurrentNode<CastExpression>//
    implements Category.Theory.Arithmetics.Symbolic {
  private static final long serialVersionUID = -0x159736130C9F5D80L;

  private static NumberLiteral literal(final Expression ¢) {
    final NumberLiteral $ = ¢.getAST().newNumberLiteral();
    $.setToken("1.");
    return $;
  }
  private static InfixExpression replacement(final Expression $) {
    return subject.pair(literal($), $).to(TIMES);
  }
  @Override public String description(final CastExpression ¢) {
    return "Use 1.*" + expression(¢) + " instead of (double)" + expression(¢);
  }
  @Override public ASTNode replacement(final CastExpression ¢) {
    return eval(() -> replacement(expression(¢))).when(type(¢).isPrimitiveType() && "double".equals(type(¢) + ""));
  }
}
