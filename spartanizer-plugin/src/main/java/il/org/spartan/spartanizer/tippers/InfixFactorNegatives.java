package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.in;
import static il.org.spartan.spartanizer.ast.navigate.extract.core;
import static il.org.spartan.spartanizer.ast.navigate.step.extendedOperands;
import static il.org.spartan.spartanizer.ast.navigate.step.left;
import static il.org.spartan.spartanizer.ast.navigate.step.right;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.DIVIDE;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.TIMES;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.compute;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.tipping.CarefulTipper;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** TODO Matteo Orrù please add a description
 * @author Matteo Orrù
 * @since 2016 */
public final class InfixFactorNegatives extends CarefulTipper<InfixExpression>//
    implements Category.Transformation.Sort {
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
    final int totalNegation = compute.level(x);
    return totalNegation == 0 || totalNegation == 1 && compute.level(left(x)) == 1 ? null : new Tip(description(x), getClass(), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression first = totalNegation % 2 == 0 ? null : the.firstOf($);
        $.stream().filter(λ -> λ != first && compute.level(λ) > 0)
            .forEach(λ -> r.replace(λ, make.plant(copy.of(compute.peel(λ))).into(λ.getParent()), g));
        if (first != null)
          r.replace(first, make.plant(subject.operand(compute.peel(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
      }
    };
  }
}
