package il.org.spartan.spartanizer.java;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

/** Collects the {@link Term} found in an {@link InfixExpression}, organizing
 * them in three output fields: {@link #plus}, {@link #minus} and {@link #all}.
 * @author Yossi Gil
 * @since 2016 */
public class TermsCollector {
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

  public List<Term> all() {
    return all;
  }

  public List<Expression> minus() {
    return negative;
  }

  public List<Expression> plus() {
    return positive;
  }

  final TermsCollector collect(final InfixExpression ¢) {
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

  Void collectPlusPrefixMinusExpression(final InfixExpression x) {
    assert x != null;
    assert !isLeafTerm(x);
    assert iz.infixMinus(x);
    final List<Expression> es = hop.operands(x);
    addPositiveTerm(core(first(es)));
    return collectNegativeTerms(rest(es));
  }

  private Void addMinus(final Expression ¢) {
    assert ¢ != null;
    all.add(Term.minus(¢));
    negative.add(¢);
    return null;
  }

  private Void addMinusTerm(final Expression x) {
    assert x != null;
    final Expression n = minus.peel(x);
    return minus.level(x) % 2 != 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
  }

  private Void addPlus(final Expression ¢) {
    assert ¢ != null;
    positive.add(¢);
    all.add(Term.plus(¢));
    return null;
  }

  private Void addPlusTerm(final Expression x) {
    assert x != null;
    final Expression n = minus.peel(x);
    return minus.level(x) % 2 == 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
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

  private Void collectMinusPrefixMinusExprssion(final InfixExpression x) {
    assert x != null;
    final List<Expression> es = hop.operands(x);
    collectNegativeTerm(core(first(es)));
    return collectPositiveTerms(rest(es));
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
    for (final Expression e : xs)
      collectNegativeTerm(core(e));
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
    for (final Expression e : xs)
      addPositiveTerm(core(e));
    return null;
  }
}
