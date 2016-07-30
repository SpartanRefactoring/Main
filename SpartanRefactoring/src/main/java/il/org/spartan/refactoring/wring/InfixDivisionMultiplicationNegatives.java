package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Extract.core;
import static il.org.spartan.refactoring.utils.expose.*;
import static il.org.spartan.refactoring.utils.Funcs.asInfixExpression;
import static il.org.spartan.refactoring.utils.Funcs.duplicate;
import static il.org.spartan.refactoring.utils.Funcs.left;
import static il.org.spartan.refactoring.utils.Funcs.negationLevel;
import static il.org.spartan.refactoring.utils.Funcs.peelNegation;
import static il.org.spartan.refactoring.utils.Funcs.right;
import static il.org.spartan.utils.Utils.in;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.DIVIDE;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.TIMES;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.MINUS;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Plant;
import il.org.spartan.refactoring.utils.Rewrite;
import il.org.spartan.refactoring.utils.Subject;

/**
 * A {@link Wring} that sorts the arguments of a {@link Operator#DIVIDE}
 * expression.
 *
 * @author Yossi Gil
 * @since 2015-09-05
 */
public final class InfixDivisionMultiplicationNegatives extends Wring<InfixExpression> {
  private static int countNegations(final List<Expression> es) {
    int $ = 0;
    for (final Expression e : es)
      $ += negationLevel(e);
    return $;
  }
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
        for (final Expression e : es)
          if (e != first && negationLevel(e) > 0)
            r.replace(e, new Plant(duplicate(peelNegation(e))).into(e.getParent()), g);
        if (first != null)
          r.replace(first, new Plant(Subject.operand(peelNegation(first)).to(MINUS)).into(first.getParent()), g);
      }
    };
  }
  @Override WringGroup wringGroup() {
	return WringGroup.REORDER_EXPRESSIONS;
  }
}