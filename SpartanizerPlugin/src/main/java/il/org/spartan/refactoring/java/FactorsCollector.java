package il.org.spartan.refactoring.java;

//Under construction issue #101

import static il.org.spartan.refactoring.utils.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

public class FactorsCollector {
  public static boolean isLeafFactor(final Expression e) {
    return !iz.infixTimes(e) && !iz.infixDivide(e);
  }

  private final List<Expression> positive = new ArrayList<>();
  private final List<Expression> negative = new ArrayList<>();
  private final List<Factor> all = new ArrayList<>();

  public FactorsCollector(final InfixExpression e) {
    collect(e);
  }

  FactorsCollector() {
    /* For internal use only */
  }

  final FactorsCollector collect(final InfixExpression e) {
    if (e != null && !isLeafFactor(e))
      collectPlusNonLeaf(e);
    return this;
  }

  Void collectPlusNonLeaf(final InfixExpression e) {
    assert e != null;
    assert !isLeafFactor(e);
    assert iz.infixPlus(e) || iz.infixMinus(e);
    return iz.infixPlus(e) ? collectPlusPrefixPlusExpression(e) //
        : collectPlusPrefixMinusExpression(e);
  }

  Void collectPlusPrefixMinusExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafFactor(e);
    assert iz.infixMinus(e);
    final List<Expression> es = hop.operands(e);
    addPositiveFactor(core(lisp.first(es)));
    return collectNegativeFactors(lisp.rest(es));
  }

  private Void addMinus(final Expression e) {
    assert e != null;
    all.add(Factor.divide(e));
    negative.add(e);
    return null;
  }

  private Void addMinusFactor(final Expression e) {
    assert e != null;
    final Expression n = minus.peel(e);
    return minus.level(e) % 2 != 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
  }

  private Void addPlus(final Expression e) {
    assert e != null;
    positive.add(e);
    all.add(Factor.times(e));
    return null;
  }

  private Void addPlusFactor(final Expression e) {
    assert e != null;
    final Expression n = minus.peel(e);
    return minus.level(e) % 2 == 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
  }

  private Void addPositiveFactor(final Expression e) {
    return isLeafFactor(e) ? addPlusFactor(e) : collectPlusNonLeaf(az.infixExpression(e));
  }

  private Void collectMinusPrefix(final Expression e) {
    assert e != null;
    return isLeafFactor(e) ? addMinus(e) : collectMinusPrefix(az.infixExpression(e));
  }

  private Void collectMinusPrefix(final InfixExpression e) {
    assert e != null;
    assert !isLeafFactor(e);
    return iz.infixPlus(e) ? collectMinusPrefixPlusExpression(e) : collectMinusPrefixMinusExprssion(e);
  }

  private Void collectMinusPrefixMinusExprssion(final InfixExpression e) {
    assert e != null;
    final List<Expression> es = hop.operands(e);
    collectNegativeFactor(core(lisp.first(es)));
    return collectPositiveFactors(lisp.rest(es));
  }

  private Void collectMinusPrefixPlusExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafFactor(e);
    assert iz.infixPlus(e);
    return collectNegativeFactors(hop.operands(e));
  }

  private Void collectNegativeFactor(final Expression e) {
    assert e != null;
    return isLeafFactor(e) ? addMinusFactor(e) : collectMinusPrefix(az.infixExpression(e));
  }

  private Void collectNegativeFactors(final Iterable<Expression> es) {
    assert es != null;
    for (final Expression e : es)
      collectNegativeFactor(core(e));
    return null;
  }

  private Void collectPlusPrefix(final Expression e) {
    assert e != null;
    return isLeafFactor(e) ? addPlus(e) : collectPlusNonLeaf(az.infixExpression(e));
  }

  private Void collectPlusPrefixPlusExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafFactor(e);
    assert iz.infixPlus(e);
    return collectPositiveFactors(hop.operands(e));
  }

  private Void collectPositiveFactors(final Iterable<Expression> es) {
    assert es != null;
    for (final Expression e : es)
      addPositiveFactor(core(e));
    return null;
  }

  public List<Expression> dividers() {
    return negative;
  }

  public List<Expression> multipliers() {
    return positive;
  }

  public List<Factor> all() {
    return all;
  }
}
