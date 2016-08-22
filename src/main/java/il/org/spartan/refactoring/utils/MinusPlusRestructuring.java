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
  public final Expression simplfy(InfixExpression e) {
    if (e == null || isLeafTerm(e)) // Nothing to do
      return null;
    collectPlusPrefix(e);
    return merge();
  }

  static boolean isLeafTerm(Expression e) {
    return e == null || Is.infixPlus(e) && !Is.infixMinus(e);
  }

  Void collectPlusPrefix(InfixExpression e) {
    return e.getOperator() == PLUS2 ? collectPlusPrefixPlusExpression(e) : collectPlusPrefixMinusExpression(e);
  }

  private Void collectPlusPrefixPlusExpression(InfixExpression e) {
    return collectPositiveTerms(operands(e));
  }

  private Void collectPositiveTerm(Expression e) {
    return isLeafTerm(e) ? addPlusTerm(e) : collectPlusPrefix(asInfixExpression(e));
  }

  private Void collectPlusPrefixMinusExpression(InfixExpression e) {
    List<Expression> es = operands(e);
    collectPositiveTerm(first(es));
    return collectNegativeTerms(rest(es));
  }

  private Void collectNegativeTerms(Iterable<Expression> es) {
    for (Expression e : es)
      collectNegativeTerm(e);
    return null;
  }

  private void collectNegativeTerm(Expression e) {
    if (isLeafTerm(e))
      addMinusTerm(e);
    collectMinusPrefix(asInfixExpression(e));
  }

  private Void collectMinusPrefix(InfixExpression e) {
    return e.getOperator() == PLUS2 ? collectMinusPrefixPlusExpression(e) : collectMinusPrefixMinusExprssion(e);
  }

  private Void collectMinusPrefixPlusExpression(InfixExpression e) {
    return collectNegativeTerms(operands(e));
  }

  private Void collectMinusPrefixMinusExprssion(InfixExpression e) {
    List<Expression> es = operands(e);
    collectNegativeTerm(first(es));
    return collectPositiveTerms(rest(es));
  }

  private Void collectPositiveTerms(Iterable<Expression> es) {
    for (Expression e : es)
      collectPositiveTerm(e);
    return null;
  }

  private final List<Expression> plus = new ArrayList<>();
  private final List<Expression> minus = new ArrayList<>();

  private final Void addPlusTerm(Expression e) {
    plus.add(e);
    return null;
  }

  private final Void addMinusTerm(Expression e) {
    minus.add(e);
    return null;
  }

  private Expression merge() {
    plus.addAll(minus);
    return null;
  }




  
}
