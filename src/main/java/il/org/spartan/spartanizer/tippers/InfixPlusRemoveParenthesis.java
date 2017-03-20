package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Removes unnecessary parenthesis in infixPlus expression, that may be string
 * concating {@code x+\"\"+(4) } goes to {@code x+\"\"+4 }
 * @author Niv Shalmon
 * @since 2016-09-11 */
public final class InfixPlusRemoveParenthesis extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -4145668192513583707L;

  /** Determines whether the parenthesis around an InfixExpression can be
   * removed in an InfixExpression that is String concatenation.
   * @param ¢ an InfixExpression that's inside parenthesis
   * @return whetherthe parenthesis can be removed and false otherwise */
  private static boolean canRemove(final InfixExpression x) {
    return in(operator(x), TIMES, DIVIDE)
        || operator(x) == wizard.PLUS2 && extract.allOperands(x).stream().allMatch(λ -> type.of(λ) == type.Primitive.Certain.STRING);
  }

  @Override public String description() {
    return "Remove redundant parenthesis";
  }

  @Override public String description(final InfixExpression ¢) {
    return description() + " around " + trivia.gist(¢);
  }

  @Override public Expression replacement(final InfixExpression x) {
    if (operator(x) != wizard.PLUS2)
      return null;
    final List<Expression> es = hop.operands(x);
    boolean isString = false;
    for (final Integer i : range.to(es.size())) {
      final int ii = i.intValue();
      final boolean b = isString;
      isString |= !type.isNotString(es.get(ii));
      // TODO: Dor Ma'ayan: use extract.core --yg
      if (iz.parenthesizedExpression(es.get(ii))) {
        Expression ¢ = expression(az.parenthesizedExpression(es.get(ii)));
        while (iz.parenthesizedExpression(¢)) {
          ¢ = expression(az.parenthesizedExpression(¢));
          replace(es, ¢, ii);
        }
        if (iz.infixExpression(¢) && ii != 0 && b && !canRemove(az.infixExpression(¢)) || iz.conditionalExpression(¢) || iz.lambdaExpression(¢))
          continue;
        replace(es, ¢, ii);
      }
    }
    final Expression $ = subject.operands(es).to(wizard.PLUS2);
    return !wizard.same($, x) ? $ : null;
  }
}
