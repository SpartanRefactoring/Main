package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.expose.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static il.org.spartan.utils.Utils.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;
import il.org.spartan.refactoring.utils.*;

/** A {@link Wring} to convert an expression such as
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
public final class InfixDivisionMultiplicationNegatives extends Wring<InfixExpression> {
  private static List<Expression> gather(final Expression e, final List<Expression> $) {
    if (e instanceof InfixExpression)
      return gather(asInfixExpression(e), $);
    $.add(e);
    return $;
  }
  private static List<Expression> gather(final InfixExpression e) {
    return gather(e, new ArrayList<Expression>());
  }
  private static List<Expression> gather(final InfixExpression e, final List<Expression> $) {
    if (e == null)
      return $;
    if (!in(e.getOperator(), TIMES, DIVIDE)) {
      $.add(e);
      return $;
    }
    gather(core(left(e)), $);
    gather(core(right(e)), $);
    if (e.hasExtendedOperands())
      gather(extendedOperands(e), $);
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
  @Override Rewrite make(final InfixExpression e, final ExclusionManager exclude) {
    final List<Expression> es = gather(e);
    if (es.size() < 2)
      return null;
    final int totalNegation = countNegations(es);
    switch (totalNegation) {
      default:
        break;
      case 0:
        return null;
      case 1:
        if (negationLevel(es.get(0)) == 1)
          return null;
    }
    if (exclude != null)
      exclude.exclude(e);
    return new Rewrite(description(e), e) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression first = totalNegation % 2 == 0 ? null : es.get(0);
        for (final Expression ¢ : es)
          if (¢ != first && negationLevel(¢) > 0)
            r.replace(¢, new Plant(duplicate(peelNegation(¢))).into(¢.getParent()), g);
        if (first != null)
          r.replace(first, new Plant(subject.operand(peelNegation(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
      }
    };
  }
  @Override WringGroup wringGroup() {
    return WringGroup.REORDER_EXPRESSIONS;
  }
}