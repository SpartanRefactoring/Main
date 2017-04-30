package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Between Nano. Checking if some expression lays between two values
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-02 */
public final class Between extends NotImplementedNanoPattern<InfixExpression> {
  private static final long serialVersionUID = 0x33BD3E02A01AE415L;
  private static final Collection<UserDefinedTipper<InfixExpression>> inEqualities = as.list(//
      patternTipper("$X1 < $X2", "", ""), //
      patternTipper("$X1 <= $X2", "", ""), //
      patternTipper("$X2 > $X1", "", ""), //
      patternTipper("$X2 >= $X1", "", "")//
  );

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Go fluent: Between pattern";
  }

  @Override public boolean canTip(final InfixExpression $) {
    final List<Expression> os = extendedOperands($);
    return os.isEmpty() ? between(left($), right($)) : IntStream.range(0, os.size() - 1).anyMatch(λ -> between(os.get(λ), os.get(λ + 1)));
  }

  private static boolean between(final Expression x1, final Expression x2) {
    return between(az.infixExpression(x1), az.infixExpression(x2));
  }

  private static boolean between(final InfixExpression x1, final InfixExpression x2) {
    return anyTips(inEqualities, x1) //
        && anyTips(inEqualities, x2) //
        && ((firstTipper(inEqualities, x1).getMatching(x1, "$X1") + "").equals(firstTipper(inEqualities, x2).getMatching(x2, "$X2") + "")
            || (firstTipper(inEqualities, x1).getMatching(x1, "$X2") + "").equals(firstTipper(inEqualities, x2).getMatching(x2, "$X1") + ""));
  }

  @Override public Tip pattern(@SuppressWarnings("unused") final InfixExpression $) {
    return null;
  }

  protected static MethodInvocation replacement(final Expression x1, final Expression x2) {
    return replacement(az.infixExpression(x1), az.infixExpression(x2));
  }

  private static MethodInvocation replacement(final InfixExpression x1, final InfixExpression x2) {
    return (firstTipper(inEqualities, x1).getMatching(x1, "$X1") + "").equals(firstTipper(inEqualities, x2).getMatching(x2, "$X2") + "")
        ? replacementAux(firstTipper(inEqualities, x1).getMatching(x1, "$X1"), firstTipper(inEqualities, x2).getMatching(x2, "$X2"))
        : replacementAux(firstTipper(inEqualities, x2).getMatching(x2, "$X1"), firstTipper(inEqualities, x1).getMatching(x1, "$X2"));
  }

  private static MethodInvocation replacementAux(final ASTNode x1, final ASTNode x2) {
    return replacementAux(az.infixExpression(x1), az.infixExpression(x2));
  }

  private static MethodInvocation replacementAux(final InfixExpression lower, final InfixExpression upper) {
    return az.methodInvocation(make.ast("between(" + firstTipper(inEqualities, lower).getMatching(lower, "$X1") + ", "
        + firstTipper(inEqualities, upper).getMatching(upper, "$X2") + ")"));
  }

  @Override public String technicalName() {
    return "XBetweenY₁AndY₂";
  }
}
