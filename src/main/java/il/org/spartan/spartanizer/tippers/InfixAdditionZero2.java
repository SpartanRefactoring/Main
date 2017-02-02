package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A {@link Tipper} to convert an expression such as {@code
 * 0 + X = X
 * } or {@code
 * X + 0 = X
 * } to <code> X </i> or {@code
 * X + 0 + Y
 * } to {@code
 * X + Y
 * }
 * @author Matteo Orrù
 * @since 2016 */
public final class InfixAdditionZero2 extends ReplaceCurrentNode<InfixExpression>//
    implements TipperCategory.NOP.onNumbers {
  @Nullable
  @Override @SuppressWarnings("boxing") public ASTNode replacement(@NotNull final InfixExpression x) {
    gather(x, new ArrayList<>());
    x.getOperator();
    extract.allOperands(x);
    extract.allOperators(x);
    final List<Expression> ops = extract.allOperands(x),
        ops2 = range.to(ops.size()).stream().filter(λ -> !iz.literal0(ops.get(λ))).map(ops::get).collect(Collectors.toList());
    InfixExpression $ = null;
    for (final Integer ¢ : range.from(0).to(ops2.size() - 1))
      $ = subject.pair($ != null ? $ : ops2.get(¢), ops2.get(¢ + 1)).to(Operator.PLUS);
    return ops2.size() != 1 ? $ : first(ops2);
  }

  private static boolean containsZeroOperand(@NotNull final InfixExpression ¢) {
    return extract.allOperands(¢).stream().anyMatch(iz::literal0);
  }

  private static boolean containsPlusOperator(@NotNull final InfixExpression x) {
    return extract.allOperators(x).stream().anyMatch(λ -> λ == Operator.PLUS);
  }

  @Nullable
  @SuppressWarnings("boxing") public static ASTNode replacement2(@NotNull final InfixExpression x) {
    final List<Expression> ops = extract.allOperands(x),
        ops2 = range.from(0).to(ops.size()).stream().filter(λ -> !iz.literal0(ops.get(λ))).map(ops::get).collect(Collectors.toList());
    InfixExpression $ = null;
    for (final Integer ¢ : range.from(0).to(ops2.size() - 1))
      $ = subject.pair($ != null ? $ : ops2.get(¢), ops2.get(¢ + 1)).to(Operator.PLUS);
    return ops2.size() != 1 ? $ : first(ops2);
  }

  @Override public boolean prerequisite(@Nullable final InfixExpression $) {
    return $ != null && iz.infixPlus($) && containsZeroOperand($) && containsPlusOperator($);
  }

  @NotNull
  private static List<Expression> gather(final Expression x, @NotNull final List<Expression> $) {
    if (x instanceof InfixExpression)
      return gather(az.infixExpression(x), $);
    $.add(x);
    return $;
  }

  @NotNull
  private static List<Expression> gather(@Nullable final InfixExpression x, @NotNull final List<Expression> $) {
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

  @NotNull
  private static List<Expression> gather(@NotNull final List<Expression> xs, @NotNull final List<Expression> $) {
    xs.forEach(λ -> gather(λ, $));
    return $;
  }

  @Override public String description() {
    return "Remove 0+ in expressions like ";
  }

  @NotNull
  @Override public String description(final InfixExpression ¢) {
    return description() + ¢;
  }
}
