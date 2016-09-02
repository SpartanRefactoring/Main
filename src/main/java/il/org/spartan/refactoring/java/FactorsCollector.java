package il.org.spartan.refactoring.java;

import static il.org.spartan.refactoring.ast.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;

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

  private Void addDivide(final Expression e) {
    assert e != null;
    final Expression ¢ = minus.level(e) % 2 == 0 ? minus.peel(e) : subject.operand(minus.peel(e)).to(wizard.MINUS1);
    all.add(Factor.divide(¢));
    dividers.add(¢);
    return null;
  }

  private Void addDivideFactor(final Expression e) {
    assert e != null;
    return collectDividePrefix(e);
  }

  private Void addTimes(final Expression e) {
    assert e != null;
    final Expression ¢ = minus.level(e) % 2 == 0 ? minus.peel(e) : subject.operand(minus.peel(e)).to(wizard.MINUS1);
    multipliers.add(¢);
    all.add(Factor.times(¢));
    return null;
  }

  private Void addTimesFactor(final Expression e) {
    assert e != null;
    return collectTimesPrefix(e);
  }

  private Void addMultiplierFactor(final Expression e) {
    return isLeafFactor(e) ? addTimesFactor(e) : collectTimesNonLeaf(az.infixExpression(e));
  }

  private Void collectDividePrefix(final Expression e) {
    assert e != null;
    return isLeafFactor(e) ? addDivide(e) : collectDividePrefix(az.infixExpression(e));
  }

  private Void collectDividePrefix(final InfixExpression e) {
    assert e != null;
    assert !isLeafFactor(e);
    return iz.infixTimes(e) ? collectDividePrefixTimesExpression(e) : collectDividePrefixDivideExprssion(e);
  }

  private Void collectDividePrefixDivideExprssion(final InfixExpression e) {
    assert e != null;
    final List<Expression> es = hop.operands(e);
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

  private Void collectTimesPrefix(final Expression e) {
    assert e != null;
    return isLeafFactor(e) ? addTimes(e) : collectTimesNonLeaf(az.infixExpression(e));
  }

  private Void collectTimesPrefixTimesExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafFactor(e);
    assert iz.infixTimes(e);
    return collectMultiplierFactors(hop.operands(e));
  }

  private Void collectMultiplierFactors(final Iterable<Expression> es) {
    assert es != null;
    for (final Expression e : es)
      addMultiplierFactor(core(e));
    return null;
  }

  public List<Expression> dividers() {
    return dividers;
  }

  public List<Expression> multipliers() {
    return multipliers;
  }

  public List<Factor> all() {
    return all;
  }
}
