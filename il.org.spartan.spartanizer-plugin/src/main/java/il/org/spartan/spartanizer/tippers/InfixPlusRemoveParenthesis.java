package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.*;
import static fluent.ly.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Removes unnecessary parenthesis in infixPlus expression, that may be string
 * concating {@code x+\"\"+(4) } goes to {@code x+\"\"+4 }
 * @author Niv Shalmon
 * @since 2016-09-11 */
public final class InfixPlusRemoveParenthesis extends ReplaceCurrentNode<InfixExpression>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = -0x39885F41A3686A5BL;

  /** Determines whether the parenthesis around an InfixExpression can be
   * removed in an InfixExpression that is String concatenation.
   * @param ¢ an InfixExpression that's inside parenthesis
   * @return whether the parenthesis can be removed and false otherwise */
  private static boolean canRemove(final InfixExpression x) {
    return in(operator(x), TIMES, DIVIDE)
        || operator(x) == op.PLUS2 && extract.allOperands(x).stream().allMatch(λ -> type.of(λ) == type.Primitive.Certain.STRING);
  }
  @Override public String description() {
    return "Remove redundant parenthesis";
  }
  @Override public String description(final InfixExpression ¢) {
    return description() + " around " + Trivia.gist(¢);
  }
  @Override public Expression replacement(final InfixExpression x) {
    if (operator(x) != op.PLUS2)
      return null;
    final List<Expression> es = hop.operands(x);
    boolean isString = false;
    for (final Integer i : range.to(es.size())) {
      final int ii = i.intValue();
      final boolean b = isString;
      isString |= !type.isNotString(es.get(ii));
      if (iz.parenthesizedExpression(es.get(ii))) {
        Expression ¢ = extract.core(es.get(ii));
        for (; iz.parenthesizedExpression(¢); replace(es, ¢, ii))
          ¢ = expression(az.parenthesizedExpression(¢));
        if (iz.infixExpression(¢) && ii != 0 && b && !canRemove(az.infixExpression(¢)) || iz.conditionalExpression(¢) || iz.lambdaExpression(¢))
          continue;
        replace(es, ¢, ii);
      }
    }
    final Expression ret = subject.operands(es).to(op.PLUS2);
    return !wizard.eq(ret, x) ? ret : null;
  }
}
