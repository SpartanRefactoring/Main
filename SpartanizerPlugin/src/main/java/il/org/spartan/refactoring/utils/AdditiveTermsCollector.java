package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;

public class AdditiveTermsCollector {
  /** Opens the parenthesis in +/-_expressions, e.g., <code>a-(b+c-(d+e))</code>
   * becomes <code>a-b-c+d-e</code>
   * @param e
   * @return */
  public final Expression collect(final InfixExpression e) {
    if (e == null || isLeafTerm(e)) // Nothing to do
      return null;
    collectPlusPrefix(e);
    return null;
  }

  static boolean isLeafTerm(final Expression e) {
    assert e != null;
    return !Is.infixPlus(e) && !Is.infixMinus(e);
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

  Void collectPlusPrefixMinusExpression(final InfixExpression e) {
    final List<Expression> es = operands(e);
    collectPositiveTerm(core(first(es)));
    return collectNegativeTerms(rest(es));
  }

  private Void collectNegativeTerms(final Iterable<Expression> es) {
    for (final Expression e : es)
      collectNegativeTerm(core(e));
    return null;
  }

  private void collectNegativeTerm(final Expression e) {
    if (isLeafTerm(e))
      addMinusTerm(e);
    collectMinusPrefix(asInfixExpression(e));
  }

  private Void collectMinusPrefix(final InfixExpression e) {
    return e == null ? null : e.getOperator() == PLUS2 ? collectMinusPrefixPlusExpression(e) : collectMinusPrefixMinusExprssion(e);
  }

  private Void collectMinusPrefixPlusExpression(final InfixExpression e) {
    return collectNegativeTerms(operands(e));
  }

  private Void collectMinusPrefixMinusExprssion(final InfixExpression e) {
    final List<Expression> es = operands(e);
    collectNegativeTerm(core(first(es)));
    return collectPositiveTerms(rest(es));
  }

  private Void collectPositiveTerms(final Iterable<Expression> es) {
    for (final Expression e : es)
      collectPositiveTerm(core(e));
    return null;
  }

  private final Void addPlusTerm(final Expression e) {
    plus.add(e);
    return null;
  }

  private final Void addMinusTerm(final Expression e) {
    minus.add(e);
    return null;
  }

  public final List<Expression> plus = new ArrayList<>();
  public final List<Expression> minus = new ArrayList<>();

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void seriesA_1() {
      azzert.aye(isLeafTerm(e("i")))//
          .andAye(isLeafTerm(e("i*j"))) //
          .andAye(isLeafTerm(e("(x)")));
    }

    @Test public void seriesA_2() {
      azzert.aye(isLeafTerm(e("(i*j)")));
      azzert.aye(isLeafTerm(e("(i+j)")));
      azzert.nay(isLeafTerm(e("i+j")));
      azzert.nay(isLeafTerm(e("i-j")));
    }

    @Test public void seriesA_3() {
      azzert.nay(Is.infixPlus(e("(i+j)")));
      azzert.aye(Is.infixPlus(core(e("(i+j)"))));
      azzert.nay(Is.infixMinus(e("(i-j)")));
      azzert.aye(Is.infixMinus(core(e("(i-j)"))));
    }

    @Test public void seriesA_4() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a + b +c");
      c.collect(i);
      azzert.that(c.plus.size(), is(3));
      azzert.that(c.minus.size(), is(0));
    }

    @Test public void seriesA_5() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a-c");
      azzert.aye(i.getOperator() == MINUS2);
      azzert.that(left(i), iz("a"));
      azzert.that(right(i), iz("c"));
      c.collect(i);
      azzert.that(c.plus.size(), is(1));
      azzert.that(c.minus.size(), is(1));
    }

    @Test public void seriesA_5a() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a-c");
      azzert.aye(i.getOperator() == MINUS2);
      azzert.that(left(i), iz("a"));
      azzert.that(right(i), iz("c"));
      c.collectPlusPrefix(i);
      azzert.that(c.plus.size(), is(1));
      azzert.that(c.minus.size(), is(1));
    }

    @Test public void seriesA_6() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a + b -c");
      azzert.aye(i.getOperator() == MINUS2);
      azzert.aye(asInfixExpression(left(i)).getOperator() == PLUS2);
      c.collect(i);
      azzert.that(c.plus.size(), is(2));
      azzert.that(c.minus.size(), is(1));
    }

    @Test public void seriesA_7() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a + (b -c)");
      c.collect(i);
      azzert.that(c.plus.size(), is(2));
      azzert.that(c.minus.size(), is(1));
    }

    @Test public void seriesA_8() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a + (b +(d + c))");
      c.collect(i);
      azzert.that(c.plus.size(), is(4));
      azzert.that(c.minus.size(), is(0));
    }

    @Test public void seriesB_1() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a - (b - c - (d - e - f - g))");
      c.collect(i);
      azzert.that(c.plus, iz("[a,c,d]"));
      azzert.that(c.minus, iz("[b,e,f,g]"));
    }

    @Test public void seriesB_2() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a - (b - c - d - e )");
      c.collect(i);
      azzert.that(c.plus.size(), is(4));
      azzert.that(c.minus.size(), is(1));
    }

    @Test public void seriesB_3() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a - (b - c)");
      c.collect(i);
      azzert.that(c.plus.size(), is(2));
    }

    @Test public void seriesB_4() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a - (b - c)");
      c.collect(i);
      azzert.that(c.minus.size(), is(1));
    }

    @Test public void seriesB_5() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a - (b - c)");
      c.collect(i);
      azzert.that(c.minus, iz("[b]"));
    }

    @Test public void seriesB_6() {
      final AdditiveTermsCollector c = new AdditiveTermsCollector();
      final InfixExpression i = i("a - (b - c)");
      c.collectPlusPrefixMinusExpression(i);
      azzert.that(c.minus, iz("[b]"));
    }
  }
}
