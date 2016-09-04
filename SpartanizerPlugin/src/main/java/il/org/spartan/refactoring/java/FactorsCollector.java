package il.org.spartan.refactoring.java;

import static il.org.spartan.refactoring.ast.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;

public class FactorsCollector {
  public static boolean isLeafFactor(final Expression x) {
    return !iz.infixTimes(x) && !iz.infixDivide(x);
  }

  private final List<Expression> multipliers = new ArrayList<>();
  private final List<Expression> dividers = new ArrayList<>();
  private final List<Factor> all = new ArrayList<>();

  FactorsCollector() {
    /* For internal use only */
  }

  public FactorsCollector(final InfixExpression e) {
    collect(e);
  }

  private Void addDivide(final Expression x) {
    assert x != null;
    final Expression ¢ = minus.level(x) % 2 == 0 ? minus.peel(x) : subject.operand(minus.peel(x)).to(wizard.MINUS1);
    all.add(Factor.divide(¢));
    dividers.add(¢);
    return null;
  }

  private Void addDivideFactor(final Expression x) {
    assert x != null;
    return collectDividePrefix(x);
  }

  private Void addMultiplierFactor(final Expression x) {
    return isLeafFactor(x) ? addTimesFactor(x) : collectTimesNonLeaf(az.infixExpression(x));
  }

  private Void addTimes(final Expression x) {
    assert x != null;
    final Expression ¢ = minus.level(x) % 2 == 0 ? minus.peel(x) : subject.operand(minus.peel(x)).to(wizard.MINUS1);
    multipliers.add(¢);
    all.add(Factor.times(¢));
    return null;
  }

  private Void addTimesFactor(final Expression x) {
    assert x != null;
    return collectTimesPrefix(x);
  }

  public List<Factor> all() {
    return all;
  }

  final FactorsCollector collect(final InfixExpression x) {
    if (x != null && !isLeafFactor(x))
      collectTimesNonLeaf(x);
    return this;
  }

  private Void collectDividePrefix(final Expression x) {
    assert x != null;
    return isLeafFactor(x) ? addDivide(x) : collectDividePrefix(az.infixExpression(x));
  }

  private Void collectDividePrefix(final InfixExpression x) {
    assert x != null;
    assert !isLeafFactor(x);
    return iz.infixTimes(x) ? collectDividePrefixTimesExpression(x) : collectDividePrefixDivideExprssion(x);
  }

  private Void collectDividePrefixDivideExprssion(final InfixExpression x) {
    assert x != null;
    final List<Expression> es = hop.operands(x);
    collectDividerFactor(core(lisp.first(es)));
    return collectMultiplierFactors(lisp.rest(es));
  }

  private Void collectDividePrefixTimesExpression(final InfixExpression x) {
    assert x != null;
    assert !isLeafFactor(x);
    assert iz.infixTimes(x);
    return collectDividersFactors(hop.operands(x));
  }

  private Void collectDividerFactor(final Expression x) {
    assert x != null;
    return isLeafFactor(x) ? addDivideFactor(x) : collectDividePrefix(az.infixExpression(x));
  }

  private Void collectDividersFactors(final Iterable<Expression> xs) {
    assert xs != null;
    for (final Expression e : xs)
      collectDividerFactor(core(e));
    return null;
  }

  private Void collectMultiplierFactors(final Iterable<Expression> xs) {
    assert xs != null;
    for (final Expression e : xs)
      addMultiplierFactor(core(e));
    return null;
  }

  Void collectTimesNonLeaf(final InfixExpression x) {
    assert x != null;
    assert !isLeafFactor(x);
    assert iz.infixTimes(x) || iz.infixDivide(x);
    return iz.infixTimes(x) ? collectTimesPrefixTimesExpression(x) //
        : collectTimesPrefixDivdeExpression(x);
  }

  private Void collectTimesPrefix(final Expression x) {
    assert x != null;
    return isLeafFactor(x) ? addTimes(x) : collectTimesNonLeaf(az.infixExpression(x));
  }

  Void collectTimesPrefixDivdeExpression(final InfixExpression x) {
    assert x != null;
    assert !isLeafFactor(x);
    assert iz.infixDivide(x);
    final List<Expression> es = hop.operands(x);
    addMultiplierFactor(core(lisp.first(es)));
    return collectDividersFactors(lisp.rest(es));
  }

  private Void collectTimesPrefixTimesExpression(final InfixExpression x) {
    assert x != null;
    assert !isLeafFactor(x);
    assert iz.infixTimes(x);
    return collectMultiplierFactors(hop.operands(x));
  }

  public List<Expression> dividers() {
    return dividers;
  }

  public List<Expression> multipliers() {
    return multipliers;
  }
}
