package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.ast.extract.*;
import static il.org.spartan.refactoring.ast.step.*;
import static il.org.spartan.refactoring.engine.Plant.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.builder.*;
import il.org.spartan.refactoring.create.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;

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
  @Override String description(final InfixExpression e) {
    return "remove 0 in X + 0 expressions from " + e;
  }

  @Override public WringGroup wringGroup() {
    return WringGroup.Abbreviation;
  }

  @Override Rewrite make(final InfixExpression e, final ExclusionManager exclude) {
    final List<Expression> es = gather(e);
    if (es.size() < 2)
      return null;
    final int totalNegation = minus.level(es);
    switch (totalNegation) {
      default:
        break;
      case 0:
        return null;
      case 1:
        if (minus.level(es.get(0)) == 1)
          return null;
    }
    if (exclude != null)
      exclude.exclude(e);
    return new Rewrite(description(e), e) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression first = totalNegation % 2 == 0 ? null : es.get(0);
        for (final Expression ¢ : es)
          if (¢ != first && minus.level(¢) > 0)
            r.replace(¢, plant(wizard.duplicate(minus.peel(¢))).into(¢.getParent()), g);
        if (first != null)
          r.replace(first, plant(subject.operand(minus.peel(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
      }
    };
  }

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
}
