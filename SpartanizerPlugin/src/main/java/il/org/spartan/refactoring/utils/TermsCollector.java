package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

public class TermsCollector {
  public static boolean isLeafTerm(final Expression e) {
    return !Is.infixPlus(e) && !Is.infixMinus(e);
  }

  private final List<Expression> plus = new ArrayList<>();
  private final List<Expression> minus = new ArrayList<>();
  private final List<SignedExpression> all = new ArrayList<>();

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
    assert Is.infixPlus(e) || Is.infixMinus(e);
    return Is.infixPlus(e) ? collectPlusPrefixPlusExpression(e) //
        : collectPlusPrefixMinusExpression(e);
  }

  Void collectPlusPrefixMinusExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafTerm(e);
    assert Is.infixMinus(e);
    final List<Expression> es = operands(e);
    addPositiveTerm(core(first(es)));
    return collectNegativeTerms(rest(es));
  }

  private Void addMinus(final Expression e) {
    assert e != null;
    all.add(SignedExpression.minus(e));
    minus.add(e);
    return null;
  }

  private final Void addMinusTerm(final Expression e) {
    assert e != null;
    final Expression n = peelNegation(e);
    return negationLevel(e) % 2 != 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
  }

  private Void addPlus(final Expression e) {
    assert e != null;
    plus.add(e);
    all.add(SignedExpression.plus(e));
    return null;
  }

  private final Void addPlusTerm(final Expression e) {
    assert e != null;
    final Expression n = peelNegation(e);
    return negationLevel(e) % 2 == 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
  }

  private Void addPositiveTerm(final Expression e) {
    return isLeafTerm(e) ? addPlusTerm(e) : collectPlusNonLeaf(asInfixExpression(e));
  }

  private Void collectMinusPrefix(final Expression e) {
    assert e != null;
    return isLeafTerm(e) ? addMinus(e) : collectMinusPrefix(asInfixExpression(e));
  }

  private Void collectMinusPrefix(final InfixExpression e) {
    assert e != null;
    assert !isLeafTerm(e);
    return Is.infixPlus(e) ? collectMinusPrefixPlusExpression(e) : collectMinusPrefixMinusExprssion(e);
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
    assert Is.infixPlus(e);
    return collectNegativeTerms(operands(e));
  }

  private Void collectNegativeTerm(final Expression e) {
    assert e != null;
    return isLeafTerm(e) ? addMinusTerm(e) : collectMinusPrefix(asInfixExpression(e));
  }

  private Void collectNegativeTerms(final Iterable<Expression> es) {
    assert es != null;
    for (final Expression e : es)
      collectNegativeTerm(core(e));
    return null;
  }

  private Void collectPlusPrefix(final Expression e) {
    assert e != null;
    return isLeafTerm(e) ? addPlus(e) : collectPlusNonLeaf(asInfixExpression(e));
  }

  private Void collectPlusPrefixPlusExpression(final InfixExpression e) {
    assert e != null;
    assert !isLeafTerm(e);
    assert Is.infixPlus(e);
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

  public List<SignedExpression> all() {
    return all;
  }
}

class SignedExpression {
  private final boolean negative;
  public final Expression expression;

  SignedExpression(final boolean minus, final Expression expression) {
    this.negative = minus;
    this.expression = expression;
  }

  static SignedExpression plus(Expression e) {
    return new SignedExpression(false, e);
  }

  static SignedExpression minus(Expression e) {
    return new SignedExpression(true, e);
  }

  boolean negative() {
    return negative;
  }

  Expression asExpression() {
    if (!negative)
      return expression;
    PrefixExpression $ = expression.getAST().newPrefixExpression();
    $.setOperand(expression);
    $.setOperator(MINUS1);
    return $;
  }

  public boolean positive() {
    return !negative;
  }
}
