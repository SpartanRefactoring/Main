package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

public class MinusPlusRestructuring {
  /** Opens the parenthesis in +/-_expressions, e.g.,
   * <code>a - (b + c - (d+e))</code> becomes <code> a - b - c + d - e</code>
   * @param e
   * @return */
  public final Expression simplfy(final InfixExpression e) {
    if (e == null || isLeafTerm(e)) // Nothing to do
      return null;
    collectPlusPrefix(e);
    return merge();
  }

  static boolean isLeafTerm(final Expression e) {
    return e == null || Is.infixPlus(e) && !Is.infixMinus(e);
  }

  Void collectPlusPrefix(final InfixExpression e) {
    return e.getOperator() == PLUS2 ? collectPlusPrefixPlusExpression(e) : collectPlusPrefixMinusExpression(e);
  }

  private Void collectPlusPrefixPlusExpression(final InfixExpression e) {
    return collectPositiveTerms(operands(e));
  }

  private Void collectPositiveTerm(final Expression e) {
    return isLeafTerm(e) ? addPlusTerm(e) : collectPlusPrefix(asInfixExpression(e));
  }

  private Void collectPlusPrefixMinusExpression(final InfixExpression e) {
    final List<Expression> es = operands(e);
    collectPositiveTerm(first(es));
    return collectNegativeTerms(rest(es));
  }

  private Void collectNegativeTerms(final Iterable<Expression> es) {
    for (final Expression e : es)
      collectNegativeTerm(e);
    return null;
  }

  private void collectNegativeTerm(final Expression e) {
    if (isLeafTerm(e))
      addMinusTerm(e);
    collectMinusPrefix(asInfixExpression(e));
  }

  private Void collectMinusPrefix(final InfixExpression e) {
    return e.getOperator() == PLUS2 ? collectMinusPrefixPlusExpression(e) : collectMinusPrefixMinusExprssion(e);
  }

  private Void collectMinusPrefixPlusExpression(final InfixExpression e) {
    return collectNegativeTerms(operands(e));
  }

  private Void collectMinusPrefixMinusExprssion(final InfixExpression e) {
    final List<Expression> es = operands(e);
    collectNegativeTerm(first(es));
    return collectPositiveTerms(rest(es));
  }

  private Void collectPositiveTerms(final Iterable<Expression> es) {
    for (final Expression e : es)
      collectPositiveTerm(e);
    return null;
  }

  private final List<Expression> plus = new ArrayList<>();
  private final List<Expression> minus = new ArrayList<>();

  private final Void addPlusTerm(final Expression e) {
    plus.add(e);
    return null;
  }

  private final Void addMinusTerm(final Expression e) {
    minus.add(e);
    return null;
  }

  private Expression merge() {
    plus.addAll(minus);
    return null;
  }
}
