package il.org.spartan.spartanizer.tippers;

import static fluent.ly.is.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.plugin.preferences.revision.PreferencesResources.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;

/** A {@link Tipper} to convert an expression such as {@code
 * 0 + X = X
 * } or {@code
 * X + 0 = X
 * } or {@code
 * X + 0 + Y
 * } to {@code
 * X + Y
 * }
 * @author Matteo Orrù
 * @since 2016 */
public final class InfixAdditionZero extends EagerTipper<InfixExpression>//
    implements TipperCategory.NOP.onNumbers {
  private static final long serialVersionUID = 0x3A8FC386BA92697AL;

  private static List<Expression> gather(final Expression x, final List<Expression> $) {
    if (iz.infixExpression(x))
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
    if (!in(operator(x), PLUS, MINUS))
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

  @Override public String description() {
    return null;
  }

  @Override public String description(final InfixExpression ¢) {
    return "Remove noop of adding 0 in " + Trivia.gist(¢);
  }

  @Override public Tip tip(final InfixExpression x) {
    final List<Expression> $ = gather(x);
    if ($.size() < 2)
      return null;
    final int n = minus.level($);
    return n == 0 || n == 1 && minus.level(the.headOf($)) == 1 ? null : new Tip(description(x), getClass(), x) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        final Expression first = n % 2 == 0 ? null : the.headOf($);
        $.stream().filter(λ -> λ != first && minus.level(λ) > 0)
            .forEach(λ -> r.replace(λ, make.plant(copy.of(minus.peel(λ))).into(λ.getParent()), g));
        if (first != null)
          r.replace(first, make.plant(subject.operand(minus.peel(first)).to(PrefixExpression.Operator.MINUS)).into(first.getParent()), g);
      }
    };
  }

  @Override public TipperGroup tipperGroup() {
    return TipperGroup.Abbreviation;
  }
}
