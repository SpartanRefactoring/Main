package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.Wring.*;

/** Replace <code>x = x # a </code> by <code> x #= a </code> where # can be any
 * operator.
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentOpSelf extends ReplaceCurrentNode<Assignment> implements Kind.Abbreviation {
  private static boolean asLeft(final Expression ¢, final Expression left) {
    return wizard.same(¢, left);
  }

  private static List<Expression> associativeReplace(final List<Expression> xs, final Expression left) {
    final List<Expression> $ = new ArrayList<>(xs);
    for (final Expression ¢ : xs)
      if (asLeft(¢, left)) {
        $.remove(¢);
        break;
      }
    return $;
  }

  private static List<Expression> nonAssociativeReplace(final List<Expression> xs, final Expression left) {
    final List<Expression> $ = new ArrayList<>(xs);
    if (asLeft(xs.get(0), left))
      $.remove(0);
    return $;
  }

  private static ASTNode replace(final Assignment a) {
    final InfixExpression ¢ = az.infixExpression(step.right(a));
    final Expression newRightExpr = az.expression(rightInfixReplacement(¢, a.getLeftHandSide()));
    return newRightExpr == null ? null : subject.pair(step.left(a), newRightExpr).to(wizard.infix.get(step.operator(¢)));
  }

  private static ASTNode rightInfixReplacement(final InfixExpression x, final Expression left) {
    final List<Expression> es = extract.allOperands(x);
    final InfixExpression.Operator o = step.operator(x);
    final List<Expression> $ = !wizard.nonAssociative(x) ? associativeReplace(es, left) : nonAssociativeReplace(es, left);
    return $.size() == es.size() ? null : $.size() == 1 ? duplicate.of(lisp.first($)) : subject.operands($).to(o);
  }

  @Override String description(final Assignment a) {
    return "Replace x = x " + step.operator(a) + "a; to x " + step.operator(a) + "= a;";
  }

  @Override ASTNode replacement(final Assignment a) {
    return !iz.isOpAssign(a) || !iz.infixExpression(step.right(a)) ? null : replace(a);
  }
}
