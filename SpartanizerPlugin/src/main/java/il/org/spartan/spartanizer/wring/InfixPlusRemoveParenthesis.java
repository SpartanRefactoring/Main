package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** Removes unnecessary parenthesis in infixPlus expression, mostly used for
 * String concating. <br/>
 * <code> x+\"\"+(4) </code> goes to <code> x+\"\"+4 </code>
 * @author Niv Shalmon
 * @since 2016-09-11 */
public class InfixPlusRemoveParenthesis extends ReplaceCurrentNode<InfixExpression> implements Kind.SyntacticBaggage {
  /** TODO: Niv, document this function, but I think it is buggy. If you have
   * #MINUS2 you can do all openings you like.
   * @param ¢
   * @return */
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

  @Override public String description() {
    return "remove uneccecary parenthesis";
  }

  @Override String description(@SuppressWarnings("unused") final InfixExpression n) {
    return description();
  }

  @Override ASTNode replacement(final InfixExpression n) {
    if (n.getOperator() != wizard.PLUS2)
      return null;
    final List<Expression> es = hop.operands(n);
    boolean changed = false;
    for (int i = 0; i < es.size(); ++i)
      if (iz.is(es.get(i), ASTNode.PARENTHESIZED_EXPRESSION)) {
        Expression ¢ = extract.core(es.get(i));
        if (iz.is(¢, ASTNode.INFIX_EXPRESSION)) {
          if (!canRemove((InfixExpression) ¢))
            continue;
        } else if (iz.is(¢, ASTNode.CONDITIONAL_EXPRESSION))
          continue;
        lisp.replace(es, ¢, i);
        changed = true;
      }
    return changed ? subject.operands(es).to(wizard.PLUS2) : null;
  }
}
