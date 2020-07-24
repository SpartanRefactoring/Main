package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.extendedOperands;
import static il.org.spartan.spartanizer.research.TipperFactory.patternTipper;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.as;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.research.UserDefinedTipper;
import il.org.spartan.spartanizer.research.nanos.common.NotImplementedNanoPattern;
import il.org.spartan.spartanizer.tipping.Tip;

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
  private Expression left;
  private Expression right;

  @Override public String description(@SuppressWarnings("unused") final InfixExpression __) {
    return "Go fluent: Between pattern";
  }
  @Override public boolean canTip(final InfixExpression $) {
    final List<Expression> os = extract.allOperands($);
    return IntStream.range(0, os.size() - 1).anyMatch(λ -> {
      left = os.get(λ);
      right = os.get(λ + 1);
      return between(os.get(λ), os.get(λ + 1));
    });
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
  @Override public Tip pattern(final InfixExpression x) {
    final List<Expression> ret = extract.allOperands(x);
    ret.set(ret.indexOf(left), replacement(left, right));
    ret.remove(right);
    final int size = ret.size();
    if (size == 1)
      return replaceBy(the.onlyOneOf(ret));
    if (size == 2)
      return replaceBy(subject.pair(the.firstOf(ret), the.secondOf(ret)).to(step.operator(x)));
    final InfixExpression $ = copy.of(x);
    $.setLeftOperand(copy.of(ret.get(0)));
    $.setRightOperand(copy.of(ret.get(1)));
    $.extendedOperands().clear();
    extendedOperands($).addAll(ret.subList(2, size));
    return replaceBy($);
  }
  private Tip replaceBy(final Expression replacement) {
    return new Tip("Use a between expression", this.getClass(), null) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(current, replacement, g);
      }
    };
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
