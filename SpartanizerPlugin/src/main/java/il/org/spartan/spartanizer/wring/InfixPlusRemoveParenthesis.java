package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** Removes unnecessary parenthesis in infixPlus expression, that may be string
 * concating <br/>
 * <code> x+\"\"+(4) </code> goes to <code> x+\"\"+4 </code>
 * @author Niv Shalmon
 * @since 2016-09-11 */
public class InfixPlusRemoveParenthesis extends ReplaceCurrentNode<InfixExpression> implements Kind.SyntacticBaggage {
  /** Determines whether the parenthesis around an InfixExpression can be
   * removed in an InfixExpression that is String concatenation.
   * @param ¢ an InfixExpression that's inside parenthesis
   * @return True if the parenthesis can be removed and false otherwise */
  private static boolean canRemove(final InfixExpression ¢) {
    if (in(¢.getOperator(), TIMES, DIVIDE))
      return true;
    if (¢.getOperator() != wizard.PLUS2)
      return false;
    for (final Expression e : extract.allOperands(¢))
      if (type.get(e) != type.Primitive.Certain.STRING)
        return false;
    return true;
  }

  private static Expression makeInfix(final List<Expression> xs, final AST t) {
    if (xs.size() == 1)
      return first(xs);
    final InfixExpression $ = t.newInfixExpression();
    $.setOperator(wizard.PLUS2);
    $.setLeftOperand(duplicate.of(first(xs)));
    $.setRightOperand(duplicate.of(second(xs)));
    for (int i = 2; i < xs.size(); ++i)
      step.extendedOperands($).add(duplicate.of(xs.get(i)));
    return $;
  }

  @Override public String description() {
    return "remove uneccecary parenthesis";
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression __) {
    return description();
  }

  @Override Expression replacement(final InfixExpression x) {
    if (x.getOperator() != wizard.PLUS2)
      return null;
    final List<Expression> es = hop.operands(x);
    boolean changed = false;
    for (int i = 0; i < es.size(); ++i)
      if (iz.parenthesizeExpression(es.get(i))) {
        // TODO: Niv, convert this into a for loop manually, or resolve
        // issues:#145, #144
        Expression ¢ = az.parenthesizedExpression(es.get(i)).getExpression();
        while (iz.parenthesizeExpression(¢)) {
          replace(es, ¢, i);
          changed = true;
          ¢ = az.parenthesizedExpression(¢).getExpression();
        }
        if (iz.infixExpression(¢)) {
          if (i != 0 && !canRemove((InfixExpression) ¢))
            continue;
        } else if (iz.conditional(¢) || iz.is(¢, ASTNode.LAMBDA_EXPRESSION))
          continue;
        replace(es, ¢, i);
        changed = true;
      }
    return !changed ? null : makeInfix(es, x.getAST());
  }
}
