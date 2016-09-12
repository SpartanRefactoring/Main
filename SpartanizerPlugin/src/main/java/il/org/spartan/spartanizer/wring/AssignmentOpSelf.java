package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

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
    if (asLeft(first(xs), left))
      $.remove(0);
    return $;
  }

  private static ASTNode replace(final Assignment a) {
    final InfixExpression ¢ = az.infixExpression(right(a));
    final Expression newRightExpr = az.expression(rightInfixReplacement(¢, step.left(a)));
    return newRightExpr == null ? null : subject.pair(left(a), newRightExpr).to(wizard.infix.get(step.operator(¢)));
  }

  private static ASTNode rightInfixReplacement(final InfixExpression x, final Expression left) {
    final List<Expression> es = extract.allOperands(x);
    final InfixExpression.Operator o = step.operator(x);
    final List<Expression> $ = !wizard.nonAssociative(x) ? associativeReplace(es, left) : nonAssociativeReplace(es, left);
    return $.size() == es.size() ? null : $.size() == 1 ? duplicate.of(first($)) : subject.operands($).to(o);
  }

  @Override String description(final Assignment ¢) {
    return "Replace x = x " + step.operator(¢) + "a; to x " + step.operator(¢) + "= a;";
  }

  @Override ASTNode replacement(final Assignment ¢) {
    return !iz.isOpAssign(¢) || !iz.infixExpression(right(¢)) ? null : replace(¢);
  }
}
