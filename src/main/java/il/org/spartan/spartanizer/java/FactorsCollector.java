/* TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 *
 * @since Sep 7, 2016 */
package il.org.spartan.spartanizer.java;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// TOOD Niv: Who wrote this class?
final class FactorsCollector {
  static boolean isLeafFactor(final Expression ¢) {
    return !iz.infixTimes(¢) && !iz.infixDivide(¢);
  }

  private final List<Expression> multipliers = new ArrayList<>();
  private final List<Expression> dividers = new ArrayList<>();
  private final List<Factor> all = new ArrayList<>();

  FactorsCollector(final InfixExpression e) {
    collect(e);
  }

  FactorsCollector() {
    /* For internal use only */
  }

  @NotNull
  public List<Factor> all() {
    return all;
  }

  @NotNull
  public List<Expression> dividers() {
    return dividers;
  }

  @NotNull
  public List<Expression> multipliers() {
    return multipliers;
  }

  @NotNull FactorsCollector collect(@Nullable final InfixExpression ¢) {
    if (¢ != null && !isLeafFactor(¢))
      collectTimesNonLeaf(¢);
    return this;
  }

  @Nullable Void collectTimesNonLeaf(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafFactor(¢);
    assert iz.infixTimes(¢) || iz.infixDivide(¢);
    return iz.infixTimes(¢) ? collectTimesPrefixTimesExpression(¢) //
        : collectTimesPrefixDivdeExpression(¢);
  }

  @Nullable
  private Void collectTimesPrefixDivdeExpression(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafFactor(¢);
    assert iz.infixDivide(¢);
    @Nullable final List<Expression> $ = hop.operands(¢);
    addMultiplierFactor(core(first($)));
    return collectDividersFactors(rest($));
  }

  private Void addDivide(@NotNull final Expression x) {
    assert x != null;
    @NotNull final Expression ¢ = minus.level(x) % 2 == 0 ? minus.peel(x) : subject.operand(minus.peel(x)).to(wizard.MINUS1);
    all.add(Factor.divide(¢));
    dividers.add(¢);
    return null;
  }

  @Nullable
  private Void addDivideFactor(@NotNull final Expression ¢) {
    assert ¢ != null;
    return collectDividePrefix(¢);
  }

  @Nullable
  private Void addMultiplierFactor(@NotNull final Expression ¢) {
    return isLeafFactor(¢) ? addTimesFactor(¢) : collectTimesNonLeaf(az.infixExpression(¢));
  }

  private Void addTimes(@NotNull final Expression x) {
    assert x != null;
    @NotNull final Expression ¢ = minus.level(x) % 2 == 0 ? minus.peel(x) : subject.operand(minus.peel(x)).to(wizard.MINUS1);
    multipliers.add(¢);
    all.add(Factor.times(¢));
    return null;
  }

  @Nullable
  private Void addTimesFactor(@NotNull final Expression ¢) {
    assert ¢ != null;
    return collectTimesPrefix(¢);
  }

  @Nullable
  private Void collectDividePrefix(@NotNull final Expression ¢) {
    assert ¢ != null;
    return isLeafFactor(¢) ? addDivide(¢) : collectDividePrefix(az.infixExpression(¢));
  }

  @Nullable
  private Void collectDividePrefix(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafFactor(¢);
    return iz.infixTimes(¢) ? collectDividePrefixTimesExpression(¢) : collectDividePrefixDivideExprssion(¢);
  }

  @Nullable
  private Void collectDividePrefixDivideExprssion(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    @Nullable final List<Expression> $ = hop.operands(¢);
    collectDividerFactor(core(first($)));
    return collectMultiplierFactors(rest($));
  }

  @Nullable
  private Void collectDividePrefixTimesExpression(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafFactor(¢);
    assert iz.infixTimes(¢);
    return collectDividersFactors(hop.operands(¢));
  }

  @Nullable
  private Void collectDividerFactor(@NotNull final Expression ¢) {
    assert ¢ != null;
    return isLeafFactor(¢) ? addDivideFactor(¢) : collectDividePrefix(az.infixExpression(¢));
  }

  private Void collectDividersFactors(@NotNull final Iterable<Expression> xs) {
    assert xs != null;
    xs.forEach(λ -> collectDividerFactor(core(λ)));
    return null;
  }

  private Void collectMultiplierFactors(@NotNull final Iterable<Expression> xs) {
    assert xs != null;
    xs.forEach(λ -> addMultiplierFactor(core(λ)));
    return null;
  }

  @Nullable
  private Void collectTimesPrefix(@NotNull final Expression ¢) {
    assert ¢ != null;
    return isLeafFactor(¢) ? addTimes(¢) : collectTimesNonLeaf(az.infixExpression(¢));
  }

  @Nullable
  private Void collectTimesPrefixTimesExpression(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafFactor(¢);
    assert iz.infixTimes(¢);
    return collectMultiplierFactors(hop.operands(¢));
  }
}
