package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace <code>x = x # a </code> by <code> x #= a </code> where # can be any
 * operator. Tested in {@link Issue103}
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentToFromInfixIncludingTo extends ReplaceCurrentNode<Assignment> implements TipperCategory.SyntacticBaggage {
  private static List<Expression> dropAnyIfSame(final List<Expression> xs, final Expression left) {
    final List<Expression> $ = new ArrayList<>(xs);
    for (final Expression ¢ : xs)
      if (same(¢, left)) {
        $.remove(¢);
        return $;
      }
    return null;
  }

  private static List<Expression> dropFirstIfSame(final Expression ¢, final List<Expression> xs) {
    return !same(¢, first(xs)) ? null : chop(new ArrayList<>(xs));
  }

  private static Expression reduce(final InfixExpression x, final Expression deleteMe) {
    final List<Expression> es = hop.operands(x), $ = !nonAssociative(x) ? dropAnyIfSame(es, deleteMe) : dropFirstIfSame(deleteMe, es);
    return $ == null ? null : $.size() == 1 ? copy.of(first($)) : subject.operands($).to(operator(x));
  }

  private static ASTNode replacement(final Expression to, final InfixExpression from) {
    if (iz.arrayAccess(to) || !sideEffects.free(to))
      return null;
    final Expression $ = reduce(from, to);
    return $ == null ? null : subject.pair(to, $).to(infix2assign(operator(from)));
  }

  @Override public String description(final Assignment ¢) {
    return "Replace x = x " + operator(¢) + "a; to x " + operator(¢) + "= a;";
  }

  @Override public ASTNode replacement(final Assignment ¢) {
    assert ¢ != null;
    final Operator $ = ¢.getOperator();
    assert $ != null;
    return $ != ASSIGN || az.infixExpression(from(¢)) == null ? null : replacement(to(¢), az.infixExpression(from(¢)));
  }
}
