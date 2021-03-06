package il.org.spartan.spartanizer.utils;

import static fluent.ly.azzert.is;
import static fluent.ly.azzert.iz;
import static fluent.ly.azzert.not;
import static il.org.spartan.spartanizer.engine.parse.e;
import static il.org.spartan.spartanizer.engine.parse.i;
import static il.org.spartan.spartanizer.engine.parse.s;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.DIVIDE;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.MINUS;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.REMAINDER;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.TIMES;

import java.util.List;

import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.Statement;
import org.junit.Test;

import fluent.ly.azzert;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.flatten;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.factory.subject.Pair;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.engine.ExpressionComparator;
import il.org.spartan.spartanizer.engine.parse;
import il.org.spartan.spartanizer.engine.type;

@SuppressWarnings({ "javadoc", "static-method" })
public final class SubjectTest {
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
    azzert.that(subject.pair(e("a*B"), e("c*d")).to(DIVIDE), iz("a * B / (c * d)"));
  }
  @Test public void extractcoreLeft() {
    azzert.that(subject.pair(e("((a-B))"), e("c-d")).to(op.PLUS2), iz("a - B + c - d"));
  }
  @Test public void extractcoreRight() {
    azzert.that(subject.pair(e("a-B"), e("(c-d)")).to(op.PLUS2), iz("a - B + c - d"));
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
    azzert.that(subject.pair(e("a+B"), e("c+d")).to(TIMES), iz("(a + B) * (c + d)"));
  }
  @Test public void multiplicationOfMultiplicatoin() {
    azzert.that(subject.pair(e("a*B"), e("c*d")).to(TIMES), iz("a * B * c * d"));
  }
  @Test public void notPeels() {
    azzert.that(subject.operand(e("((a))")).to(PrefixExpression.Operator.NOT), iz("!a"));
  }
  @Test public void operandsNoParenthesisRest() {
    azzert.that(subject.operands(e("((a-b))"), e("b-c"), e("c+d")).to(op.PLUS2), iz("a-b+b-c+c+d"));
  }
  @Test public void operandsParenthesisLeft() {
    azzert.that(subject.operands(e("((a+b))"), e("b"), e("c")).to(TIMES), iz("(a+b)*b*c"));
  }
  @Test public void operandsParenthesisRest() {
    azzert.that(subject.operands(e("((a))"), e("b+c"), e("c+d")).to(TIMES), iz("a*(b+c)*(c+d)"));
  }
  @Test public void operandsParenthesisRight() {
    azzert.that(subject.operands(e("((a))"), e("b+c"), e("c")).to(TIMES), iz("a*(b+c)*c"));
  }
  @Test public void operandsVanilla() {
    azzert.that(subject.operands(e("((a))"), e("b"), e("c")).to(op.PLUS2), iz("a+b+c"));
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
    final List<Expression> operands = an.empty.list();
    operands.add(copy.of(e("3*4")));
    operands.add(copy.of(e("5")));
    final InfixExpression refit = subject.operands(operands).to(e.getOperator());
    azzert.that(refit, is(not(e)));
    azzert.that(refit + "", is("3 * 4 + 5"));
  }
  @Test public void refitWithSort() {
    final InfixExpression e = i("1 + 2 * 3");
    final List<Expression> operands = hop.operands(flatten.of(e));
    azzert.that(operands.size(), is(2));
    azzert.that(the.firstOf(operands) + "", is("1"));
    azzert.that(the.secondOf(operands) + "", is("2 * 3"));
    assert ExpressionComparator.ADDITION.sort(operands);
    azzert.that(the.firstOf(operands) + "", is("2 * 3"));
    azzert.that(the.secondOf(operands) + "", is("1"));
    final InfixExpression refit = subject.operands(operands).to(e.getOperator());
    azzert.that(refit, is(not(e)));
    azzert.that(refit + "", is("2 * 3 + 1"));
  }
  @Test public void remainderDoesntAssociate() {
    azzert.that(subject.pair(e("a*B"), e("c*d")).to(REMAINDER), iz("a * B % (c * d)"));
  }
  @Test public void subjectOperands() {
    final Expression e = parse.e("2 + a <b");
    assert type.isNotString(e);
    final InfixExpression plus = findFirst.infixPlus(e);
    assert type.isNotString(plus);
    final List<Expression> operands = hop.operands(flatten.of(plus));
    azzert.that(operands.size(), is(2));
    assert ExpressionComparator.ADDITION.sort(operands);
    azzert.that(subject.operands(operands).to(plus.getOperator()), iz("a +2"));
  }
  @Test public void subjectOperandsDoesNotIntroduceList() {
    final List<Expression> operands = hop.operands(copy.of(i("a*b")));
    azzert.that(operands.size(), is(2));
    final InfixExpression refit = subject.operands(operands).to(i("1+2").getOperator());
    assert !refit.hasExtendedOperands();
    azzert.that(refit + "", is("a + b"));
  }
  @Test public void subjectOperandsIsCorrect() {
    azzert.that(subject.operands(hop.operands(copy.of(i("a*b*c")))).to(i("1+2+3").getOperator()) + "", is("a + b + c"));
  }
  @Test public void subjectOperandsNonNull() {
    assert subject.operands(hop.operands(copy.of(i("a+b+c")))).to(i("1+2+3").getOperator()) != null;
  }
  @Test public void subjectOperandsWithParenthesis() {
    final Expression e = parse.e("(2 + a) * b");
    assert type.isNotString(e);
    final InfixExpression plus = findFirst.infixPlus(e);
    assert type.isNotString(plus);
    final List<Expression> operands = hop.operands(flatten.of(plus));
    azzert.that(operands.size(), is(2));
    assert ExpressionComparator.ADDITION.sort(operands);
    azzert.that(subject.operands(operands).to(plus.getOperator()), iz("a +2"));
  }
  @Test public void subtractionsDoesntAssociate() {
    azzert.that(subject.pair(e("a-B"), e("c-d")).to(MINUS), iz("a - B - (c - d)"));
  }
  @Test public void vanilla() {
    subject.operand(e("a")).to(PrefixExpression.Operator.NOT);
  }
  @Test public void vanillaCorrectResult() {
    azzert.that(subject.operand(e("a")).to(PrefixExpression.Operator.NOT), iz("!a"));
  }
}
