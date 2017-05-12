package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static fluent.ly.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Replace {@code x = x # a } by {@code x #= a } where # can be any operator.
 * Tested in {@link Issue103}
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentToFromInfixIncludingTo extends ReplaceCurrentNode<Assignment>//
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = -0x5517BB99E5F22453L;

  private static List<Expression> dropAnyIfSame(final List<Expression> xs, final Expression left) {
    final List<Expression> $ = as.list(xs);
    for (final Expression ¢ : xs)
      if (eq(¢, left)) {
        $.remove(¢);
        return $;
      }
    return null;
  }

  private static List<Expression> dropFirstIfSame(final Expression ¢, final List<Expression> xs) {
    return !eq(¢, the.headOf(xs)) ? null : chop(new ArrayList<>(xs));
  }

  private static Expression reduce(final InfixExpression x, final Expression deleteMe) {
    final List<Expression> es = hop.operands(x), $ = !op.nonAssociative(x) ? dropAnyIfSame(es, deleteMe) : dropFirstIfSame(deleteMe, es);
    return $ == null ? null : $.size() == 1 ? copy.of(the.headOf($)) : subject.operands($).to(operator(x));
  }

  private static ASTNode replacement(final Expression to, final InfixExpression from) {
    if (iz.arrayAccess(to) || !sideEffects.free(to))
      return null;
    final Expression $ = reduce(from, to);
    return $ == null ? null : subject.pair(to, $).to(op.infix2assign(operator(from)));
  }

  @Override public String description(final Assignment ¢) {
    return "Replace " + to(¢) + " = + with " + to(¢) + "  =? ";
  }

  @Override public ASTNode replacement(final Assignment ¢) {
    return ¢.getOperator() != ASSIGN || az.infixExpression(from(¢)) == null ? null : replacement(to(¢), az.infixExpression(from(¢)));
  }
}
