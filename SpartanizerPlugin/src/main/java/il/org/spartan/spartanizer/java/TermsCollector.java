package il.org.spartan.spartanizer.java;

import static il.org.spartan.spartanizer.ast.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.utils.*;

/** Collects the {@link Term} found in an {@link InfixExpression}, organizing
 * them in three output fields: {@link #plus}, {@link #minus} and {@link #all}.
 * @author Yossi Gil
 * @since 2016 */
public class TermsCollector {
  public static boolean isLeafTerm(final Expression x) {
    return !iz.infixPlus(x) && !iz.infixMinus(x);
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

  final TermsCollector collect(final InfixExpression x) {
    if (x != null && !isLeafTerm(x))
      collectPlusNonLeaf(x);
    return this;
  }

  Void collectPlusNonLeaf(final InfixExpression x) {
    assert x != null;
    assert !isLeafTerm(x);
    assert iz.infixPlus(x) || iz.infixMinus(x);
    return iz.infixPlus(x) ? collectPlusPrefixPlusExpression(x) //
        : collectPlusPrefixMinusExpression(x);
  }

  Void collectPlusPrefixMinusExpression(final InfixExpression x) {
    assert x != null;
    assert !isLeafTerm(x);
    assert iz.infixMinus(x);
    final List<Expression> es = hop.operands(x);
    addPositiveTerm(core(lisp.first(es)));
    return collectNegativeTerms(lisp.rest(es));
  }

  private Void addMinus(final Expression x) {
    assert x != null;
    all.add(Term.minus(x));
    negative.add(x);
    return null;
  }

  private Void addMinusTerm(final Expression x) {
    assert x != null;
    final Expression n = minus.peel(x);
    return minus.level(x) % 2 != 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
  }

  private Void addPlus(final Expression x) {
    assert x != null;
    positive.add(x);
    all.add(Term.plus(x));
    return null;
  }

  private Void addPlusTerm(final Expression x) {
    assert x != null;
    final Expression n = minus.peel(x);
    return minus.level(x) % 2 == 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
  }

  private Void addPositiveTerm(final Expression x) {
    return isLeafTerm(x) ? addPlusTerm(x) : collectPlusNonLeaf(az.infixExpression(x));
  }

  private Void collectMinusPrefix(final Expression x) {
    assert x != null;
    return isLeafTerm(x) ? addMinus(x) : collectMinusPrefix(az.infixExpression(x));
  }

  private Void collectMinusPrefix(final InfixExpression x) {
    assert x != null;
    assert !isLeafTerm(x);
    return iz.infixPlus(x) ? collectMinusPrefixPlusExpression(x) : collectMinusPrefixMinusExprssion(x);
  }

  private Void collectMinusPrefixMinusExprssion(final InfixExpression x) {
    assert x != null;
    final List<Expression> es = hop.operands(x);
    collectNegativeTerm(core(lisp.first(es)));
    return collectPositiveTerms(lisp.rest(es));
  }

  private Void collectMinusPrefixPlusExpression(final InfixExpression x) {
    assert x != null;
    assert !isLeafTerm(x);
    assert iz.infixPlus(x);
    return collectNegativeTerms(hop.operands(x));
  }

  private Void collectNegativeTerm(final Expression x) {
    assert x != null;
    return isLeafTerm(x) ? addMinusTerm(x) : collectMinusPrefix(az.infixExpression(x));
  }

  private Void collectNegativeTerms(final Iterable<Expression> xs) {
    assert xs != null;
    for (final Expression e : xs)
      collectNegativeTerm(core(e));
    return null;
  }

  private Void collectPlusPrefix(final Expression x) {
    assert x != null;
    return isLeafTerm(x) ? addPlus(x) : collectPlusNonLeaf(az.infixExpression(x));
  }

  private Void collectPlusPrefixPlusExpression(final InfixExpression x) {
    assert x != null;
    assert !isLeafTerm(x);
    assert iz.infixPlus(x);
    return collectPositiveTerms(hop.operands(x));
  }

  private Void collectPositiveTerms(final Iterable<Expression> xs) {
    assert xs != null;
    for (final Expression e : xs)
      addPositiveTerm(core(e));
    return null;
  }
}
