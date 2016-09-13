package il.org.spartan.spartanizer.wring;
import static il.org.spartan.spartanizer.ast.wizard.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** Replace <code>x = x # a </code> by <code> x #= a </code> where # can be any
 * operator.
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentOpSelf extends ReplaceCurrentNode<Assignment> implements Kind.SyntacticBaggage {
  private static boolean asLeft(final Expression ¢, final Expression left) {
    return same(¢, left);
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

  private static ASTNode replace(final Assignment a, final InfixExpression x) {
    final Expression left = left(a);
    if (!sideEffects.free(left))
      return null;
    final Expression $ = eliminate(x, left);
    return $ == null ? null : subject.pair(left, $).to(infix2assign(operator(x)));
  }
  private static Expression eliminate(final InfixExpression x, final Expression left) {
    final List<Expression> es = extract.allOperands(x);
    final InfixExpression.Operator o = operator(x);
    final List<Expression> $ = !nonAssociative(x) ? associativeReplace(es, left) : nonAssociativeReplace(es, left);
    return $.size() == es.size() ? null : $.size() == 1 ? duplicate.of(first($)) : subject.operands($).to(o);
  }

  @Override public String description(final Assignment ¢) {
    return "Replace x = x " + step.operator(¢) + "a; to x " + step.operator(¢) + "= a;";
  }

  @Override public ASTNode replacement(final Assignment a) {
    assert a != null;
    final Operator o = a.getOperator();
    assert o != null;
    if (o != ASSIGN)
      return null;
    final InfixExpression right = az.infixExpression(right(a));
    if (right == null)
      return null;
    return replace(a, right);
  }
}
