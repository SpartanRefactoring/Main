package il.org.spartan.refactoring.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

/**
 * A {@link Wring} that sorts the arguments of a {@link Operator#DIVIDE}
 * expression.
 *
 * @author Yossi Gil
 * @since 2015-09-05
 */
public final class InfixDivisionMultiplicationNegatives extends Wring<InfixExpression> implements Kind.ReorganizeExpression {
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
      gather(expose.extendedOperands(e), $);
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
    if (totalNegation == 0 || totalNegation == 1 && negationLevel(es.get(0)) == 1)
      return null;
    if (exclude != null)
      exclude.exclude(e);
    return new Rewrite(description(e), e) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression first = totalNegation % 2 == 0 ? null : es.get(0);
        for (final Expression e : es)
          if (e != first && negationLevel(e) > 0)
            r.replace(e, new Plant(duplicate(peelNegation(e))).into(e.getParent()), g);
        if (first != null)
          r.replace(first,
              new Plant(Subject.operand(peelNegation(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
      }
    };
  }
}