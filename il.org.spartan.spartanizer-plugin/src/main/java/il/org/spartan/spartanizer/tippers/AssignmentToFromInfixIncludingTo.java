package il.org.spartan.spartanizer.tippers;

import static fluent.ly.lisp.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

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
import il.org.spartan.spartanizer.tipping.categories.*;

/** Replace {@code x = x # a } by {@code x #= a } where # can be any operator.
 * Tested in {@link Issue103}
 * @author Alex Kopzon
 * @since 2016 */
public final class AssignmentToFromInfixIncludingTo extends ReplaceCurrentNode<Assignment>//
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = -0x5517BB99E5F22453L;

  private static List<Expression> dropAnyIfSame(final List<Expression> xs, final Expression left) {
    final List<Expression> ret = as.list(xs);
    for (final Expression ¢ : xs)
      if (eq(¢, left)) {
        ret.remove(¢);
        return ret;
      }
    return null;
  }
  private static List<Expression> dropFirstIfSame(final Expression ¢, final List<Expression> xs) {
    return !eq(¢, the.firstOf(xs)) ? null : chop(new ArrayList<>(xs));
  }
  private static Expression reduce(final InfixExpression x, final Expression deleteMe) {
    final List<Expression> es = hop.operands(x), ret = !op.nonAssociative(x) ? dropAnyIfSame(es, deleteMe) : dropFirstIfSame(deleteMe, es);
    return ret == null ? null : ret.size() == 1 ? copy.of(the.firstOf(ret)) : subject.operands(ret).to(operator(x));
  }
  private static ASTNode replacement(final Expression to, final InfixExpression from) {
    if (iz.arrayAccess(to) || !sideEffects.free(to))
      return null;
    final Expression ret = reduce(from, to);
    return ret == null ? null : subject.pair(to, ret).to(op.infix2assign(operator(from)));
  }
  @Override public String description(final Assignment ¢) {
    return "Replace " + to(¢) + " = + with " + to(¢) + "  =? ";
  }
  @Override public ASTNode replacement(final Assignment ¢) {
    return ¢.getOperator() != ASSIGN || az.infixExpression(from(¢)) == null ? null : replacement(to(¢), az.infixExpression(from(¢)));
  }
}
