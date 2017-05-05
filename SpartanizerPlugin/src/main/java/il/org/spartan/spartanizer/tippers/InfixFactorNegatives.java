package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** TODO Matteo Orrù please add a description
 * @author Matteo Orrù
 * @since 2016 */
public final class InfixFactorNegatives extends CarefulTipper<InfixExpression>//
    implements TipperCategory.Sorting {
  private static final long serialVersionUID = -0x114771EA9E5872CAL;

  private static List<Expression> gather(final Expression x, final List<Expression> $) {
    if (x instanceof InfixExpression)
      return gather(az.infixExpression(x), $);
    $.add(x);
    return $;
  }

  private static List<Expression> gather(final InfixExpression ¢) {
    return gather(¢, an.empty.list());
  }

  private static List<Expression> gather(final InfixExpression x, final List<Expression> $) {
    if (x == null)
      return $;
    if (!in(x.getOperator(), TIMES, DIVIDE))
      $.add(x);
    else {
      gather(core(left(x)), $);
      gather(core(right(x)), $);
      if (x.hasExtendedOperands())
        gather(extendedOperands(x), $);
    }
    return $;
  }

  private static List<Expression> gather(final Iterable<Expression> xs, final List<Expression> $) {
    xs.forEach(λ -> gather(λ, $));
    return $;
  }

  @Override public String description(final InfixExpression ¢) {
    return "Use at most one arithmetical negation, for first factor of " + ¢.getOperator();
  }

  @Override public Tip tip(final InfixExpression x) {
    final List<Expression> $ = gather(x);
    if ($.size() < 2)
      return null;
    final int totalNegation = minus.level(x);
    return totalNegation == 0 || totalNegation == 1 && minus.level(left(x)) == 1 ? null : new Tip(description(x), getClass(), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression first = totalNegation % 2 == 0 ? null : the.headOf($);
        $.stream().filter(λ -> λ != first && minus.level(λ) > 0)
            .forEach(λ -> r.replace(λ, make.plant(copy.of(minus.peel(λ))).into(λ.getParent()), g));
        if (first != null)
          r.replace(first, make.plant(subject.operand(minus.peel(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
      }
    };
  }
}
