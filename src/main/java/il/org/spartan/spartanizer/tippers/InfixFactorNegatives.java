package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO: Matteo Orrù please add a description
 * @author Matteo Orrù
 * @since 2016 */
public final class InfixFactorNegatives extends CarefulTipper<InfixExpression>//
    implements TipperCategory.Sorting {
  private static final long serialVersionUID = -1245089074484114122L;

  private static List<Expression> gather(final Expression x, final List<Expression> $) {
    if (x instanceof InfixExpression)
      return gather(az.infixExpression(x), $);
    $.add(x);
    return $;
  }

  private static List<Expression> gather(final InfixExpression ¢) {
    return gather(¢, new ArrayList<>());
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

  private static List<Expression> gather(final Iterable<Expression> xs, final List<Expression> $) {
    xs.forEach(λ -> gather(λ, $));
    return $;
  }

  @Override public String description(final InfixExpression ¢) {
    return "Use at most one arithmetical negation, for first factor of " + ¢.getOperator();
  }

  @Override public Tip tip(final InfixExpression x, final ExclusionManager exclude) {
    final List<Expression> $ = gather(x);
    if ($.size() < 2)
      return null;
    final int totalNegation = eliminate.level(x);
    if (totalNegation == 0 || totalNegation == 1 && eliminate.level(left(x)) == 1)
      return null;
    if (exclude != null)
      exclude.exclude(x);
    return new Tip(description(x), x, getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression first = totalNegation % 2 == 0 ? null : first($);
        $.stream().filter(λ -> λ != first && eliminate.level(λ) > 0)
            .forEach(λ -> r.replace(λ, make.plant(copy.of(eliminate.peel(λ))).into(λ.getParent()), g));
        if (first != null)
          r.replace(first, make.plant(subject.operand(eliminate.peel(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
      }
    };
  }
}
