package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.extract.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

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
public final class InfixFactorNegatives extends Wring<InfixExpression> implements Kind.NOP {
  private static List<Expression> gather(final Expression x, final List<Expression> $) {
    if (x instanceof InfixExpression)
      return gather(az.infixExpression(x), $);
    $.add(x);
    return $;
  }

  private static List<Expression> gather(final InfixExpression ¢) {
    return gather(¢, new ArrayList<Expression>());
  }

  private static List<Expression> gather(final InfixExpression x, final List<Expression> $) {
    if (x == null)
      return $;
    if (!in(x.getOperator(), TIMES, DIVIDE)) {
      $.add(x);
      return $;
    }
    gather(core(left(x)), $);
    gather(core(right(x)), $);
    if (x.hasExtendedOperands())
      gather(extendedOperands(x), $);
    return $;
  }

  private static List<Expression> gather(final List<Expression> xs, final List<Expression> $) {
    for (final Expression ¢ : xs)
      gather(¢, $);
    return $;
  }

  @Override public String description(final InfixExpression ¢) {
    return "Use at most one arithmetical negation, for first factor of " + ¢.getOperator();
  }

  @Override public Rewrite wring(final InfixExpression x, final ExclusionManager exclude) {
    final List<Expression> es = gather(x);
    if (es.size() < 2)
      return null;
    final int totalNegation = minus.level(x);
    if (totalNegation == 0 || totalNegation == 1 && minus.level(left(x)) == 1)
      return null;
    if (exclude != null)
      exclude.exclude(x);
    return new Rewrite(description(x), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression first = totalNegation % 2 == 0 ? null : first(es);
        for (final Expression ¢ : es)
          if (¢ != first && minus.level(¢) > 0)
            r.replace(¢, il.org.spartan.spartanizer.assemble.make.plant(duplicate.of(minus.peel(¢))).into(¢.getParent()), g);
        if (first != null)
          r.replace(first, il.org.spartan.spartanizer.assemble.make.plant(subject.operand(minus.peel(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
      }
    };
  }
}
