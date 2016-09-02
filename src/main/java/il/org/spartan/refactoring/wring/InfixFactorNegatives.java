package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.assemble.Plant.*;
import static il.org.spartan.refactoring.ast.extract.*;
import static il.org.spartan.refactoring.ast.step.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.utils.*;

/** convert an expression such as
 *
 * <pre>
 * 1 * i
 * </pre>
 *
 * or
 *
 * <pre>
 * i * 1
 * </pre>
 *
 * to
 *
 * <pre>
 * i
 * </i>
 * or
 * <pre>
 * i * 1 * jasLiteral
 * </pre>
 *
 * to
 *
 * <pre>
 * i * j
 * </pre>
 *
 * @author Matteo Orrù
 * @since 2016 */
public final class InfixFactorNegatives extends Wring<InfixExpression> implements Kind.NoImpact {
  private static List<Expression> gather(final Expression x, final List<Expression> $) {
    if (x instanceof InfixExpression)
      return gather(az.infixExpression(x), $);
    $.add(x);
    return $;
  }

  private static List<Expression> gather(final InfixExpression x) {
    return gather(x, new ArrayList<Expression>());
  }

  private static List<Expression> gather(final InfixExpression x, final List<Expression> $) {
    if (x == null)
      return $;
    if (!in(x.getOperator(), TIMES, DIVIDE)) {
      $.add(x);
      return $;
    }
    gather(core(step.left(x)), $);
    gather(core(step.right(x)), $);
    if (x.hasExtendedOperands())
      gather(extendedOperands(x), $);
    return $;
  }

  private static List<Expression> gather(final List<Expression> es, final List<Expression> $) {
    for (final Expression e : es)
      gather(e, $);
    return $;
  }

  @Override String description(final InfixExpression e) {
    return "Use at most one arithmetical negation, for first factor of " + e.getOperator();
  }

  @Override Rewrite make(final InfixExpression x, final ExclusionManager exclude) {
    final List<Expression> es = gather(x);
    if (es.size() < 2)
      return null;
    final int totalNegation = minus.level(x);
    if (totalNegation == 0 || totalNegation == 1 && minus.level(step.left(x)) == 1)
      return null;
    if (exclude != null)
      exclude.exclude(x);
    return new Rewrite(description(x), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression first = totalNegation % 2 == 0 ? null : lisp.first(es);
        for (final Expression ¢ : es)
          if (¢ != first && minus.level(¢) > 0)
            r.replace(¢, plant(duplicate.of(minus.peel(¢))).into(¢.getParent()), g);
        if (first != null)
          r.replace(first, plant(subject.operand(minus.peel(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
      }
    };
  }
}
