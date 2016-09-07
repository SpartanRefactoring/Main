package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.assemble.plant.*;
import static il.org.spartan.spartanizer.ast.extract.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.plugin.PluginPreferencesResources.*;
import il.org.spartan.spartanizer.utils.*;

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
public final class InfixZeroAddition extends Wring<InfixExpression> {
  private static List<Expression> gather(final Expression e, final List<Expression> $) {
    if (e instanceof InfixExpression)
      return gather(az.infixExpression(e), $);
    $.add(e);
    return $;
  }

  private static List<Expression> gather(final InfixExpression e) {
    return gather(e, new ArrayList<Expression>());
  }

  private static List<Expression> gather(final InfixExpression e, final List<Expression> $) {
    if (e == null)
      return $;
    if (!in(e.getOperator(), PLUS, MINUS)) {
      $.add(e);
      return $;
    }
    gather(core(step.left(e)), $);
    gather(core(step.right(e)), $);
    if (e.hasExtendedOperands())
      gather(extendedOperands(e), $);
    return $;
  }

  private static List<Expression> gather(final List<Expression> es, final List<Expression> $) {
    for (final Expression e : es)
      gather(e, $);
    return $;
  }

  @Override public String description() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override public WringGroup wringGroup() {
    return WringGroup.Abbreviation;
  }

  @Override String description(final InfixExpression e) {
    return "remove 0 in X + 0 expressions from " + e;
  }

  @Override Rewrite make(final InfixExpression e, final ExclusionManager exclude) {
    final List<Expression> es = gather(e);
    if (es.size() < 2)
      return null;
    final int n = minus.level(es);
    if (n == 0 || n == 1 && minus.level(lisp.first(es)) == 1)
      return null;
    if (exclude != null)
      exclude.exclude(e);
    return new Rewrite(description(e), e) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression first = n % 2 == 0 ? null : es.get(0);
        for (final Expression ¢ : es)
          if (¢ != first && minus.level(¢) > 0)
            r.replace(¢, plant(duplicate.of(minus.peel(¢))).into(¢.getParent()), g);
        if (first != null)
          r.replace(first, plant(subject.operand(minus.peel(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
      }
    };
  }
}
