package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static il.org.spartan.spartanizer.ast.navigate.step.token;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.engine.type;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Simplify comparison of additions by moving negative elements sides and by
 * moving integers convert {@code
 * a <= length - 1
 * } into {@code
 * a < length
 * }
 * @author Dor Ma'ayan
 * @since 2-12-2016 */
public class InfixLessToLessEquals extends ReplaceCurrentNode<InfixExpression>//
    implements Category.Theory.Arithmetics.Symbolic {
  private static final long serialVersionUID = -0x42B6884A632DF3B2L;

  @Override public ASTNode replacement(final InfixExpression ¢) {
    return !isLegalOperation(¢)//
        || !iz.infixPlus(right(¢))//
        || !"1".equals(token(az.numberLiteral(right(az.infixExpression(right(¢))))))//
        || iz.number(left(az.infixExpression(right(¢))))//
        || type.isDouble(left(¢)) ? null : subject.pair(left(¢), left(az.infixExpression(right(¢)))).to(Operator.LESS_EQUALS);
  }
  private static boolean isLegalOperation(final InfixExpression ¢) {
    return iz.infixLess(¢);
  }
  @Override public String description(final InfixExpression ¢) {
    return "Convert Less Equals Operator to Less " + ¢;
  }
}
