package il.org.spartan.spartanizer.java;

import static il.org.spartan.spartanizer.ast.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.utils.*;

public class FactorsCollector {
  public static boolean isLeafFactor(final Expression e) {
    return !iz.infixTimes(e) && !iz.infixDivide(e);
  }

  private final List<Expression> multipliers = new ArrayList<>();
  private final List<Expression> dividers = new ArrayList<>();
  private final List<Factor> all = new ArrayList<>();

  public FactorsCollector(final InfixExpression e) {
    collect(e);
  }

  FactorsCollector() {
    /* For internal use only */
  }

  public List<Factor> all() {
    return all;
  }

  public List<Expression> dividers() {
    return dividers;
  }

  public List<Expression> multipliers() {
    return multipliers;
  }

  final FactorsCollector collect(final InfixExpression e) {
    if (e != null && !isLeafFactor(e))
      collectTimesNonLeaf(e);
    return this;
  }

  Void collectTimesNonLeaf(final InfixExpression e) {
    assert e != null;
    assert !isLeafFactor(e);
    assert iz.infixTimes(e) || iz.infixDivide(e);
    return iz.infixTimes(e) ? collectTimesPrefixTimesExpression(e) //
        : collectTimesPrefixDivdeExpression(e);
  }

  Void collectTimesPrefixDivdeExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafFactor(e);
    assert iz.infixDivide(e);
    final List<Expression> es = hop.operands(e);
    addMultiplierFactor(core(lisp.first(es)));
    return collectDividersFactors(lisp.rest(es));
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

  private Void collectDividePrefixTimesExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafFactor(e);
    assert iz.infixTimes(e);
    return collectDividersFactors(hop.operands(e));
  }

  private Void collectDividerFactor(final Expression e) {
    assert e != null;
    return isLeafFactor(e) ? addDivideFactor(e) : collectDividePrefix(az.infixExpression(e));
  }

  private Void collectDividersFactors(final Iterable<Expression> es) {
    assert es != null;
    for (final Expression e : es)
      collectDividerFactor(core(e));
    return null;
  }

  private Void collectMultiplierFactors(final Iterable<Expression> es) {
    assert es != null;
    for (final Expression e : es)
      addMultiplierFactor(core(e));
    return null;
  }

  private Void collectTimesPrefix(final Expression x) {
    assert x != null;
    return isLeafFactor(x) ? addTimes(x) : collectTimesNonLeaf(az.infixExpression(x));
  }

  private Void collectTimesPrefixTimesExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafFactor(e);
    assert iz.infixTimes(e);
    return collectMultiplierFactors(hop.operands(e));
  }
}
