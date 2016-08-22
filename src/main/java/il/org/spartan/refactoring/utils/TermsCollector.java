package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

public class TermsCollector {
  static boolean isLeafTerm(final Expression e) {
    return !Is.infixPlus(e) && !Is.infixMinus(e);
  }

  public final List<Expression> plus = new ArrayList<>();
  public final List<Expression> minus = new ArrayList<>();

  public TermsCollector(final InfixExpression e) {
    collect(e);
  }

  TermsCollector() {
    /* For internal use only */
  }

  final TermsCollector collect(final InfixExpression e) {
    if (!isLeafTerm(e))
      collectPlusPrefix(e);
    return this;
  }

  Void collectPlusPrefix(final InfixExpression e) {
    return Is.infixPlus(e) ? collectPlusPrefixPlusExpression(e) : collectPlusPrefixMinusExpression(e);
  }

  Void collectPlusPrefixMinusExpression(final InfixExpression e) {
    final List<Expression> es = operands(e);
    collectPositiveTerm(core(first(es)));
    return collectNegativeTerms(rest(es));
  }

  private Void addMinus(final Expression e) {
    minus.add(e);
    return null;
  }

  private final Void addMinusTerm(final Expression e) {
    final Expression n = peelNegation(e);
    return negationLevel(e) % 2 != 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
  }

  private Void addPlus(final Expression e) {
    plus.add(e);
    return null;
  }

  private final Void addPlusTerm(final Expression e) {
    final Expression n = peelNegation(e);
    return negationLevel(e) % 2 == 0 ? collectPlusPrefix(n) : collectMinusPrefix(n);
  }

  private Void collectMinusPrefix(final Expression e) {
    return !Is.is(e, INFIX_EXPRESSION) ? addMinus(e) : collectMinusPrefix(asInfixExpression(e));
  }

  private Void collectMinusPrefix(final InfixExpression e) {
    return e == null ? null : Is.infixPlus(e) ? collectMinusPrefixPlusExpression(e) : collectMinusPrefixMinusExprssion(e);
  }

  private Void collectMinusPrefixMinusExprssion(final InfixExpression e) {
    final List<Expression> es = operands(e);
    collectNegativeTerm(core(first(es)));
    return collectPositiveTerms(rest(es));
  }

  private Void collectMinusPrefixPlusExpression(final InfixExpression e) {
    return collectNegativeTerms(operands(e));
  }

  private void collectNegativeTerm(final Expression e) {
    if (isLeafTerm(e))
      addMinusTerm(e);
    collectMinusPrefix(asInfixExpression(e));
  }

  private Void collectNegativeTerms(final Iterable<Expression> es) {
    for (final Expression e : es)
      collectNegativeTerm(core(e));
    return null;
  }

  private Void collectPlusPrefix(final Expression e) {
    return !Is.is(e, INFIX_EXPRESSION) ? addPlus(e) : collectPlusPrefix(asInfixExpression(e));
  }

  private Void collectPlusPrefixPlusExpression(final InfixExpression e) {
    return collectPositiveTerms(operands(e));
  }

  private Void collectPositiveTerm(final Expression e) {
    return isLeafTerm(e) ? addPlusTerm(e) : collectPlusPrefix(asInfixExpression(e));
  }

  private Void collectPositiveTerms(final Iterable<Expression> es) {
    for (final Expression e : es)
      collectPositiveTerm(core(e));
    return null;
  }
}
