package il.org.spartan.spartanizer.java;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Collects the {@link Term} found in an {@link InfixExpression}, organizing
 * them in three output fields: {@link #plus}, {@link #minus} and {@link #all}.
 * @author Yossi Gil
 * @since 2016 */
public final class TermsCollector {
  public static boolean isLeafTerm(final Expression ¢) {
    return !iz.infixPlus(¢) && !iz.infixMinus(¢);
  }

  private final List<Expression> positive = an.empty.list();
  private final List<Expression> negative = an.empty.list();
  private final List<Term> all = an.empty.list();

  public TermsCollector(final InfixExpression e) {
    collect(e);
  }

  TermsCollector() {
    /* For internal use only */
  }

  public List<Term> all() {
    return all;
  }

  public List<Expression> minus() {
    return negative;
  }

  public List<Expression> plus() {
    return positive;
  }

  TermsCollector collect(final InfixExpression ¢) {
    if (¢ != null && !isLeafTerm(¢))
      collectPlusNonLeaf(¢);
    return this;
  }

  Void collectPlusNonLeaf(final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafTerm(¢);
    assert iz.infixPlus(¢) || iz.infixMinus(¢);
    return iz.infixPlus(¢) ? collectPlusPrefixPlusExpression(¢) //
        : collectPlusPrefixMinusExpression(¢);
  }

  Void collectPlusPrefixMinusExpression(final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafTerm(¢);
    assert iz.infixMinus(¢);
    final List<Expression> $ = hop.operands(¢);
    addPositiveTerm(core(the.headOf($)));
    return collectNegativeTerms(the.tailOf($));
  }

  private Void addMinus(final Expression ¢) {
    assert ¢ != null;
    all.add(Term.minus(¢));
    negative.add(¢);
    return null;
  }

  private Void addMinusTerm(final Expression ¢) {
    assert ¢ != null;
    final Expression $ = minus.peel(¢);
    return minus.level(¢) % 2 != 0 ? collectPlusPrefix($) : collectMinusPrefix($);
  }

  private Void addPlus(final Expression ¢) {
    assert ¢ != null;
    positive.add(¢);
    all.add(Term.plus(¢));
    return null;
  }

  private Void addPlusTerm(final Expression ¢) {
    assert ¢ != null;
    final Expression $ = minus.peel(¢);
    return minus.level(¢) % 2 == 0 ? collectPlusPrefix($) : collectMinusPrefix($);
  }

  private Void addPositiveTerm(final Expression ¢) {
    return isLeafTerm(¢) ? addPlusTerm(¢) : collectPlusNonLeaf(az.infixExpression(¢));
  }

  private Void collectMinusPrefix(final Expression ¢) {
    assert ¢ != null;
    return isLeafTerm(¢) ? addMinus(¢) : collectMinusPrefix(az.infixExpression(¢));
  }

  private Void collectMinusPrefix(final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafTerm(¢);
    return iz.infixPlus(¢) ? collectMinusPrefixPlusExpression(¢) : collectMinusPrefixMinusExprssion(¢);
  }

  private Void collectMinusPrefixMinusExprssion(final InfixExpression ¢) {
    assert ¢ != null;
    final List<Expression> $ = hop.operands(¢);
    collectNegativeTerm(core(the.headOf($)));
    return collectPositiveTerms(the.tailOf($));
  }

  private Void collectMinusPrefixPlusExpression(final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafTerm(¢);
    assert iz.infixPlus(¢);
    return collectNegativeTerms(hop.operands(¢));
  }

  private Void collectNegativeTerm(final Expression ¢) {
    assert ¢ != null;
    return isLeafTerm(¢) ? addMinusTerm(¢) : collectMinusPrefix(az.infixExpression(¢));
  }

  private Void collectNegativeTerms(final Iterable<Expression> xs) {
    assert xs != null;
    xs.forEach(λ -> collectNegativeTerm(core(λ)));
    return null;
  }

  private Void collectPlusPrefix(final Expression ¢) {
    assert ¢ != null;
    return isLeafTerm(¢) ? addPlus(¢) : collectPlusNonLeaf(az.infixExpression(¢));
  }

  private Void collectPlusPrefixPlusExpression(final InfixExpression ¢) {
    assert ¢ != null;
    assert !isLeafTerm(¢);
    assert iz.infixPlus(¢);
    return collectPositiveTerms(hop.operands(¢));
  }

  private Void collectPositiveTerms(final Iterable<Expression> xs) {
    assert xs != null;
    xs.forEach(λ -> addPositiveTerm(core(λ)));
    return null;
  }
}
