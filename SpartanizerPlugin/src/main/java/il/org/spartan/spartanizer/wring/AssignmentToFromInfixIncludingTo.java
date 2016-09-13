package il.org.spartan.spartanizer.wring;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.ast.wizard.*;
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
public final class AssignmentToFromInfixIncludingTo extends ReplaceCurrentNode<Assignment> implements Kind.SyntacticBaggage {
  private static List<Expression> dropAnyIfSame(final List<Expression> xs, final Expression left) {
    final List<Expression> $ = new ArrayList<>(xs);
    for (final Expression ¢ : xs)
      if (same(¢, left)) {
        $.remove(¢);
        return $;
      }
    return null;
  }

  private static List<Expression> dropFirstIfSame(final Expression ¢, final List<Expression> es) {
    if (!same(¢, first(es)))
      return null;
    return chop(new ArrayList<>(es));
  }

  private static Expression reduce(final InfixExpression x, final Expression deleteMe) {
    final List<Expression> es = hop.operands(x);
    final List<Expression> $ = !nonAssociative(x) ? dropAnyIfSame(es, deleteMe) : dropFirstIfSame(deleteMe, es);
    return $ == null ? null : $.size() == 1 ? duplicate.of(first($)) : subject.operands($).to(operator(x));
  }

  private static ASTNode replacement(final Expression to, final InfixExpression from) {
    if (!sideEffects.free(to))
      return null;
    final Expression $ = reduce(from, to);
    return $ == null ? null : subject.pair(to, $).to(infix2assign(operator(from)));
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
    return az.infixExpression(from(a)) == null ? null : replacement(to(a), az.infixExpression(from(a)));
  }
}
