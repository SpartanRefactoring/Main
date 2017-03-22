package il.org.spartan.spartanizer.java;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Reorganizer terms in a canonical way
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
public enum TermsReorganizer {
  ;
  @Nullable public static Expression simplify(final InfixExpression ¢) {
    return build(new TermsCollector(¢));
  }

  @Nullable private static Expression build(@NotNull final List<Expression> plus, @NotNull final List<Expression> minus) {
    return buildMinus(buildPlus(plus), minus);
  }

  @Nullable private static Expression build(@NotNull final TermsCollector ¢) {
    return build(¢.plus(), ¢.minus());
  }

  @Nullable private static Expression buildMinus(@Nullable final Expression first, @NotNull final List<Expression> rest) {
    if (first == null)
      return buildMinus(rest);
    if (rest.isEmpty())
      return first;
    rest.add(0, first);
    return subject.operands(rest).to(wizard.MINUS2);
  }

  private static Expression buildMinus(@NotNull final List<Expression> ¢) {
    final Expression $ = subject.operand(first(¢)).to(wizard.MINUS1);
    if (¢.size() == 1)
      return $;
    ¢.remove(0);
    ¢.add(0, $);
    return subject.operands(¢).to(wizard.MINUS2);
  }

  private static Expression buildPlus(@NotNull final List<Expression> ¢) {
    switch (¢.size()) {
      case 0:
        return null;
      case 1:
        return first(¢);
      case 2:
        return subject.pair(first(¢), second(¢)).to(wizard.PLUS2);
      default:
        return subject.operands(¢).to(wizard.PLUS2);
    }
  }
}
