package il.org.spartan.refactoring.ast;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.engine.into.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.assemble.subject.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.java.*;

@SuppressWarnings({ "javadoc", "static-method" }) public class subjectTest {
  @Test public void assignment() {
    azzert.that(subject.pair(e("a"), e("b")).to(Assignment.Operator.ASSIGN), iz("a=b"));
    azzert.that(subject.pair(e("a"), e("b")).to(Assignment.Operator.PLUS_ASSIGN), iz("a+=b"));
    azzert.that(subject.pair(e("a"), e("b")).to(Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN), iz("a>>>=b"));
  }

  @Test public void conditionalExtract() {
    final Pair pair = subject.pair(e("a-B"), e("(c-d)"));
    assert pair != null;
    azzert.that(pair.toCondition(e("(x)")), iz("x ? a-B : c-d"));
  }

  @Test public void conditionalSimple() {
    final Pair pair = subject.pair(e("a-B"), e("(c-d)"));
    assert pair != null;
    azzert.that(pair.toCondition(e("x")), iz("x ? a-B : c-d"));
  }

  @Test public void divisionDoesntAssociate() {
    azzert.that(subject.pair(e("a*B"), e("c*d")).to(InfixExpression.Operator.DIVIDE), iz("a * B / (c * d)"));
  }

  @Test public void extractcoreLeft() {
    azzert.that(subject.pair(e("((a-B))"), e("c-d")).to(InfixExpression.Operator.PLUS), iz("a - B + c - d"));
  }

  @Test public void extractcoreRight() {
    azzert.that(subject.pair(e("a-B"), e("(c-d)")).to(InfixExpression.Operator.PLUS), iz("a - B + c - d"));
  }

  @Test public void makeIfNotStatement() {
    final Statement s = s("s();");
    azzert.that(s, iz("{s();}"));
    azzert.that(subject.pair(s, s("f();")).toNot(e("a")), iz("if(!a)s(); else f();"));
  }

  @Test public void makeIfStatement() {
    final Statement s = s("s();");
    azzert.that(s, iz("{s();}"));
    azzert.that(subject.pair(s, s("f();")).toIf(e("a")), iz("if(a)s(); else f();"));
  }

  @Test public void makeIfStatementOfNestedIf() {
    azzert.that(subject.pair(s("if (a) return b;"), s("if (c) return d;")).toIf(e("x")), iz("if(x) {if (a) return b; } else if (c) return d;"));
  }

  @Test public void multiplicationOfAddition() {
    azzert.that(subject.pair(e("a+B"), e("c+d")).to(InfixExpression.Operator.TIMES), iz("(a + B) * (c + d)"));
  }

  @Test public void multiplicationOfMultiplicatoin() {
    azzert.that(subject.pair(e("a*B"), e("c*d")).to(InfixExpression.Operator.TIMES), iz("a * B * c * d"));
  }

  @Test public void notPeels() {
    azzert.that(subject.operand(e("((a))")).to(PrefixExpression.Operator.NOT), iz("!a"));
  }

  @Test public void operandsNoParenthesisRest() {
    azzert.that(subject.operands(e("((a))"), e("b+c"), e("c+d")).to(InfixExpression.Operator.PLUS), iz("a+b+c+c+d"));
  }

  @Test public void operandsParenthesisLeft() {
    azzert.that(subject.operands(e("((a+b))"), e("b"), e("c")).to(InfixExpression.Operator.TIMES), iz("(a+b)*b*c"));
  }

  @Test public void operandsParenthesisRest() {
    azzert.that(subject.operands(e("((a))"), e("b+c"), e("c+d")).to(InfixExpression.Operator.TIMES), iz("a*(b+c)*(c+d)"));
  }

  @Test public void operandsParenthesisRight() {
    azzert.that(subject.operands(e("((a))"), e("b+c"), e("c")).to(InfixExpression.Operator.TIMES), iz("a*(b+c)*c"));
  }

  @Test public void operandsVanilla() {
    azzert.that(subject.operands(e("((a))"), e("b"), e("c")).to(InfixExpression.Operator.PLUS), iz("a+b+c"));
  }

  @Test public void postfix() {
    azzert.that(subject.operand(e("a")).to(PostfixExpression.Operator.INCREMENT), iz("a++"));
    azzert.that(subject.operand(e("a")).to(PostfixExpression.Operator.DECREMENT), iz("a--"));
  }

  @Test public void postfixPeels() {
    azzert.that(subject.operand(e("((a))")).to(PostfixExpression.Operator.INCREMENT), iz("a++"));
    azzert.that(subject.operand(e("((a))")).to(PostfixExpression.Operator.DECREMENT), iz("a--"));
  }

  @Test public void refitPreservesOrder() {
    final InfixExpression e = i("1 + 2 * 3");
    final List<Expression> operands = new ArrayList<>();
    operands.add(duplicate.of(e("3*4")));
    operands.add(duplicate.of(e("5")));
    final InfixExpression refit = subject.operands(operands).to(e.getOperator());
    azzert.that(refit, is(not(e)));
    azzert.that("" + refit, is("3 * 4 + 5"));
  }

  @Test @Ignore public void refitWithSort() {
    final InfixExpression e = i("1 + 2 * 3");
    final List<Expression> operands = hop.operands(flatten.of(e));
    azzert.that(operands.size(), is(2));
    azzert.that("" + operands.get(0), is("1"));
    azzert.that("" + operands.get(1), is("2 * 3"));
    assert ExpressionComparator.ADDITION.sort(operands);
    azzert.that("" + operands.get(0), is("2 * 3"));
    azzert.that("" + operands.get(1), is("1"));
    final InfixExpression refit = subject.operands(operands).to(e.getOperator());
    azzert.that(refit, is(not(e)));
    azzert.that("" + refit, is("2 * 3 + 1"));
  }

  @Test public void remainderDoesntAssociate() {
    azzert.that(subject.pair(e("a*B"), e("c*d")).to(InfixExpression.Operator.REMAINDER), iz("a * B % (c * d)"));
  }

  @Test public void subjectOperands() {
    final Expression e = into.e("2 + a < b");
    assert stringType.isNot(e);
    final InfixExpression plus = extract.firstPlus(e);
    assert stringType.isNot(plus);
    final List<Expression> operands = hop.operands(flatten.of(plus));
    azzert.that(operands.size(), is(2));
    final boolean b = ExpressionComparator.ADDITION.sort(operands);
    azzert.that(b, is(true));
    azzert.that(subject.operands(operands).to(plus.getOperator()), iz("a +2"));
  }

  @Test public void subjectOperandsDoesNotIntroduceList() {
    final List<Expression> operands = hop.operands(duplicate.of(i("a*b")));
    azzert.that(operands.size(), is(2));
    final InfixExpression e = i("1+2");
    final InfixExpression refit = subject.operands(operands).to(e.getOperator());
    azzert.that(refit.hasExtendedOperands(), is(false));
    azzert.that("" + refit, is("a + b"));
  }

  @Test public void subjectOperandsIsCorrect() {
    azzert.that("" + subject.operands(hop.operands(duplicate.of(i("a*b*c")))).to(i("1+2+3").getOperator()), is("a + b + c"));
  }

  @Test public void subjectOperandsNotNull() {
    assert subject.operands(hop.operands(duplicate.of(i("a+b+c")))).to(i("1+2+3").getOperator()) != null;
  }

  @Test public void subjectOperandsWithParenthesis() {
    final Expression e = into.e("(2 + a) * b");
    assert stringType.isNot(e);
    final InfixExpression plus = extract.firstPlus(e);
    assert stringType.isNot(plus);
    final List<Expression> operands = hop.operands(flatten.of(plus));
    azzert.that(operands.size(), is(2));
    final boolean b = ExpressionComparator.ADDITION.sort(operands);
    azzert.that(b, is(true));
    azzert.that(subject.operands(operands).to(plus.getOperator()), iz("a +2"));
  }

  @Test public void substractionsDoesntAssociate() {
    azzert.that(subject.pair(e("a-B"), e("c-d")).to(InfixExpression.Operator.MINUS), iz("a - B - (c - d)"));
  }

  @Test public void vanilla() {
    subject.operand(e("a")).to(PrefixExpression.Operator.NOT);
  }

  @Test public void vanillaCorrectResult() {
    azzert.that(subject.operand(e("a")).to(PrefixExpression.Operator.NOT), iz("!a"));
  }
}