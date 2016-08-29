package il.org.spartan.refactoring.java;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

public class TermsCollector {
  public static boolean isLeafTerm(final Expression e) {
    return !iz.infixPlus(e) && !iz.infixMinus(e);
  }

  private final List<Expression> plus = new ArrayList<>();
  private final List<Expression> minus = new ArrayList<>();
  private final List<Term> all = new ArrayList<>();

  public TermsCollector(final InfixExpression e) {
    collect(e);
  }

  TermsCollector() {
    /* For internal use only */
  }

  final TermsCollector collect(final InfixExpression e) {
    if (e != null && !isLeafTerm(e))
      collectPlusNonLeaf(e);
    return this;
  }

  Void collectPlusNonLeaf(final InfixExpression e) {
    assert e != null;
    assert !isLeafTerm(e);
    assert iz.infixPlus(e) || iz.infixMinus(e);
    return iz.infixPlus(e) ? collectPlusPrefixPlusExpression(e) //
        : collectPlusPrefixMinusExpression(e);
  }

  Void collectPlusPrefixMinusExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafTerm(e);
    assert iz.infixMinus(e);
    final List<Expression> es = operands(e);
    addPositiveTerm(core(first(es)));
    return collectNegativeTerms(rest(es));
  }

  private Void addMinus(final Expression e) {
    assert e != null;
    all.add(Term.minus(e));
    minus.add(e);
    return null;
  }

  private Void addMinusTerm(final Expression e) {
    assert e != null;
    final Expression n = peelNegation(e);
    return negation.level(e) % 2 != 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
  }

  private Void addPlus(final Expression e) {
    assert e != null;
    plus.add(e);
    all.add(Term.plus(e));
    return null;
  }

  private Void addPlusTerm(final Expression e) {
    assert e != null;
    final Expression n = peelNegation(e);
    return negation.level(e) % 2 == 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
  }

  private Void addPositiveTerm(final Expression e) {
    return isLeafTerm(e) ? addPlusTerm(e) : collectPlusNonLeaf(az.infixExpression(e));
  }

  private Void collectMinusPrefix(final Expression e) {
    assert e != null;
    return isLeafTerm(e) ? addMinus(e) : collectMinusPrefix(az.infixExpression(e));
  }

  private Void collectMinusPrefix(final InfixExpression e) {
    assert e != null;
    assert !isLeafTerm(e);
    return iz.infixPlus(e) ? collectMinusPrefixPlusExpression(e) : collectMinusPrefixMinusExprssion(e);
  }

  private Void collectMinusPrefixMinusExprssion(final InfixExpression e) {
    assert e != null;
    final List<Expression> es = operands(e);
    collectNegativeTerm(core(first(es)));
    return collectPositiveTerms(rest(es));
  }

  private Void collectMinusPrefixPlusExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafTerm(e);
    assert iz.infixPlus(e);
    return collectNegativeTerms(operands(e));
  }

  private Void collectNegativeTerm(final Expression e) {
    assert e != null;
    return isLeafTerm(e) ? addMinusTerm(e) : collectMinusPrefix(az.infixExpression(e));
  }

  private Void collectNegativeTerms(final Iterable<Expression> es) {
    assert es != null;
    for (final Expression e : es)
      collectNegativeTerm(core(e));
    return null;
  }

  private Void collectPlusPrefix(final Expression e) {
    assert e != null;
    return isLeafTerm(e) ? addPlus(e) : collectPlusNonLeaf(az.infixExpression(e));
  }

  private Void collectPlusPrefixPlusExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafTerm(e);
    assert iz.infixPlus(e);
    return collectPositiveTerms(operands(e));
  }

  private Void collectPositiveTerms(final Iterable<Expression> es) {
    assert es != null;
    for (final Expression e : es)
      addPositiveTerm(core(e));
    return null;
  }

  public List<Expression> minus() {
    return minus;
  }

  public List<Expression> plus() {
    return plus;
  }

  public List<Term> all() {
    return all;
  }
}
