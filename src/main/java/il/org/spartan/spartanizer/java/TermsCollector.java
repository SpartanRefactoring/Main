package il.org.spartan.spartanizer.java;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Collects the {@link Term} found in an {@link InfixExpression}, organizing
 * them in three output fields: {@link #plus}, {@link #minus} and {@link #all}.
 * @author Yossi Gil
 * @since 2016 */
public final class TermsCollector {
  public static boolean isLeafTerm(final Expression ¢) {
    return !iz.infixPlus(¢) && !iz.infixMinus(¢);
  }

  private final List<Expression> positive = new ArrayList<>();
  private final List<Expression> negative = new ArrayList<>();
  private final List<Term> all = new ArrayList<>();

  public TermsCollector(final InfixExpression e) {
    collect(e);
  }

  TermsCollector() {
    /* For internal use only */
  }

  @NotNull public List<Term> all() {
    return all;
  }

  @NotNull public List<Expression> minus() {
    return negative;
  }

  @NotNull public List<Expression> plus() {
    return positive;
  }

  @NotNull TermsCollector collect(@Nullable final InfixExpression ¢) {
    if (¢ != null && !isLeafTerm(¢))
      collectPlusNonLeaf(¢);
    return this;
  }

  @Nullable Void collectPlusNonLeaf(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafTerm(¢);
    assert iz.infixPlus(¢) || iz.infixMinus(¢);
    return iz.infixPlus(¢) ? collectPlusPrefixPlusExpression(¢) //
        : collectPlusPrefixMinusExpression(¢);
  }

  @Nullable Void collectPlusPrefixMinusExpression(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafTerm(¢);
    assert iz.infixMinus(¢);
    final List<Expression> $ = hop.operands(¢);
    addPositiveTerm(core(first($)));
    return collectNegativeTerms(rest($));
  }

  private Void addMinus(@NotNull final Expression ¢) {
    assert ¢ != null;
    all.add(Term.minus(¢));
    negative.add(¢);
    return null;
  }

  @Nullable private Void addMinusTerm(@NotNull final Expression ¢) {
    assert ¢ != null;
    final Expression $ = minus.peel(¢);
    return minus.level(¢) % 2 != 0 ? collectPlusPrefix($) : collectMinusPrefix($);
  }

  private Void addPlus(@NotNull final Expression ¢) {
    assert ¢ != null;
    positive.add(¢);
    all.add(Term.plus(¢));
    return null;
  }

  @Nullable private Void addPlusTerm(@NotNull final Expression ¢) {
    assert ¢ != null;
    final Expression $ = minus.peel(¢);
    return minus.level(¢) % 2 == 0 ? collectPlusPrefix($) : collectMinusPrefix($);
  }

  @Nullable private Void addPositiveTerm(@NotNull final Expression ¢) {
    return isLeafTerm(¢) ? addPlusTerm(¢) : collectPlusNonLeaf(az.infixExpression(¢));
  }

  @Nullable private Void collectMinusPrefix(@NotNull final Expression ¢) {
    assert ¢ != null;
    return isLeafTerm(¢) ? addMinus(¢) : collectMinusPrefix(az.infixExpression(¢));
  }

  @Nullable private Void collectMinusPrefix(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafTerm(¢);
    return iz.infixPlus(¢) ? collectMinusPrefixPlusExpression(¢) : collectMinusPrefixMinusExprssion(¢);
  }

  @Nullable private Void collectMinusPrefixMinusExprssion(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    final List<Expression> $ = hop.operands(¢);
    collectNegativeTerm(core(first($)));
    return collectPositiveTerms(rest($));
  }

  @Nullable private Void collectMinusPrefixPlusExpression(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafTerm(¢);
    assert iz.infixPlus(¢);
    return collectNegativeTerms(hop.operands(¢));
  }

  @Nullable private Void collectNegativeTerm(@NotNull final Expression ¢) {
    assert ¢ != null;
    return isLeafTerm(¢) ? addMinusTerm(¢) : collectMinusPrefix(az.infixExpression(¢));
  }

  private Void collectNegativeTerms(@NotNull final Iterable<Expression> xs) {
    assert xs != null;
    xs.forEach(λ -> collectNegativeTerm(core(λ)));
    return null;
  }

  @Nullable private Void collectPlusPrefix(@NotNull final Expression ¢) {
    assert ¢ != null;
    return isLeafTerm(¢) ? addPlus(¢) : collectPlusNonLeaf(az.infixExpression(¢));
  }

  @Nullable private Void collectPlusPrefixPlusExpression(@NotNull final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafTerm(¢);
    assert iz.infixPlus(¢);
    return collectPositiveTerms(hop.operands(¢));
  }

  private Void collectPositiveTerms(@NotNull final Iterable<Expression> xs) {
    assert xs != null;
    xs.forEach(λ -> addPositiveTerm(core(λ)));
    return null;
  }
}
