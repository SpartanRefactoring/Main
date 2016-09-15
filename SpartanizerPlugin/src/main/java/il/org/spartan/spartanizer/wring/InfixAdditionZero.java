package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.extract.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.plugin.PreferencesResources.*;
import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** A {@link Wring} to convert an expression such as
 *
 * <pre>
 * 0 + X = X
 * </pre>
 *
 * or
 *
 * <pre>
 * X + 0 = X
 * </pre>
 *
 * to
 *
 * <pre>
 * X
 * </i>
 * or
 * <pre>
 * X + 0 + Y
 * </pre>
 *
 * to
 *
 * <pre>
 * X + Y
 * </pre>
 *
 * @author Matteo Orrù
 * @since 2016 */
public final class InfixAdditionZero extends Wring<InfixExpression> implements Kind.NOP {
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
    if (!in(x.getOperator(), PLUS, MINUS)) {
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

  @Override public String description() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public String description(final InfixExpression ¢) {
    return "remove 0 in X + 0 expressions from " + ¢;
  }

  @Override public Rewrite wring(final InfixExpression x, final ExclusionManager exclude) {
    final List<Expression> es = gather(x);
    if (es.size() < 2)
      return null;
    final int n = minus.level(es);
    if (n == 0 || n == 1 && minus.level(first(es)) == 1)
      return null;
    if (exclude != null)
      exclude.exclude(x);
    return new Rewrite(description(x), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression first = n % 2 == 0 ? null : es.get(0);
        for (final Expression ¢ : es)
          if (¢ != first && minus.level(¢) > 0)
            r.replace(¢, il.org.spartan.spartanizer.assemble.make.plant(duplicate.of(minus.peel(¢))).into(¢.getParent()), g);
        if (first != null)
          r.replace(first, il.org.spartan.spartanizer.assemble.make.plant(subject.operand(minus.peel(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
      }
    };
  }

  @Override public WringGroup wringGroup() {
    return WringGroup.Abbreviation;
  }
}
