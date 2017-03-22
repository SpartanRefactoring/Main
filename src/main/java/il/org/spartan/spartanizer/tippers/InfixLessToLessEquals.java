package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** Simplify comparison of additions by moving negative elements sides and by
 * moving integers convert {@code
 * a <= length - 1
 * } into {@code
 * a < length
 * }
 * @author Dor Ma'ayan
 * @since 2-12-2016 */
public class InfixLessToLessEquals extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.Unite {
  private static final long serialVersionUID = -4807179505337496498L;

  @Nullable @Override public ASTNode replacement(final InfixExpression ¢) {
    return !isLegalOperation(¢)//
        || !iz.infixPlus(right(¢))//
        || !"1".equals(token(az.numberLiteral(right(az.infixExpression(right(¢))))))//
        || iz.number(left(az.infixExpression(right(¢))))//
        || type.isDouble(left(¢)) ? null : subject.pair(left(¢), left(az.infixExpression(right(¢)))).to(Operator.LESS_EQUALS);
  }

  private static boolean isLegalOperation(final InfixExpression ¢) {
    return iz.infixLess(¢);
  }

  @NotNull @Override public String description(final InfixExpression ¢) {
    return "Convert Less Equals Operator to Less " + ¢;
  }
}
