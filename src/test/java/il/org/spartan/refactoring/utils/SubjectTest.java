package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.azzert.is;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import static il.org.spartan.refactoring.utils.Funcs.*;

import il.org.spartan.refactoring.utils.Subject.*;

@SuppressWarnings({ "javadoc", "static-method" }) public class SubjectTest {
  @Test public void assignment() {
    that(Subject.pair(e("a"), e("b")).to(Assignment.Operator.ASSIGN), iz("a=b"));
    that(Subject.pair(e("a"), e("b")).to(Assignment.Operator.PLUS_ASSIGN), iz("a+=b"));
    that(Subject.pair(e("a"), e("b")).to(Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN), iz("a>>>=b"));
  }
  @Test public void conditionalExtract() {
    final Pair pair = Subject.pair(e("a-B"), e("(c-d)"));
    that(pair, notNullValue());
    that(pair.toCondition(e("(x)")), iz("x ? a-B : c-d"));
  }
  @Test public void conditionalSimple() {
    final Pair pair = Subject.pair(e("a-B"), e("(c-d)"));
    that(pair, notNullValue());
    that(pair.toCondition(e("x")), iz("x ? a-B : c-d"));
  }
  @Test public void divisionDoesntAssociate() {
    that(Subject.pair(e("a*B"), e("c*d")).to(InfixExpression.Operator.DIVIDE), iz("(a * B) / (c * d)"));
  }
  @Test public void extractcoreLeft() {
    that(Subject.pair(e("((a-B))"), e("c-d")).to(InfixExpression.Operator.PLUS), iz("a - B + c - d"));
  }
  @Test public void extractcoreRight() {
    that(Subject.pair(e("a-B"), e("(c-d)")).to(InfixExpression.Operator.PLUS), iz("a - B + c - d"));
  }
  @Test public void makeIfNotStatement() {
    final Statement s = s("s();");
    that(s, iz("{s();}"));
    that(Subject.pair(s, s("f();")).toNot(e("a")), iz("if(!a)s(); else f();"));
  }
  @Test public void makeIfStatement() {
    final Statement s = s("s();");
    that(s, iz("{s();}"));
    that(Subject.pair(s, s("f();")).toIf(e("a")), iz("if(a)s(); else f();"));
  }
  @Test public void makeIfStatementOfNestedIf() {
    that(Subject.pair(s("if (a) return b;"), s("if (c) return d;")).toIf(e("x")),
        iz("if(x) {if (a) return b; } else if (c) return d;"));
  }
  @Test public void multiplicationOfAddition() {
    that(Subject.pair(e("a+B"), e("c+d")).to(InfixExpression.Operator.TIMES), iz("(a + B) * (c + d)"));
  }
  @Test public void multiplicationOfMultiplicatoin() {
    that(Subject.pair(e("a*B"), e("c*d")).to(InfixExpression.Operator.TIMES), iz("a * B * c * d"));
  }
  @Test public void notPeels() {
    that(Subject.operand(e("((a))")).to(PrefixExpression.Operator.NOT), iz("!a"));
  }
  @Test public void operandsNoParenthesisRest() {
    that(Subject.operands(e("((a))"), e("b+c"), e("c+d")).to(InfixExpression.Operator.PLUS), iz("a+b+c+c+d"));
  }
  @Test public void operandsParenthesisLeft() {
    that(Subject.operands(e("((a+b))"), e("b"), e("c")).to(InfixExpression.Operator.TIMES), iz("(a+b)*b*c"));
  }
  @Test public void operandsParenthesisRest() {
    that(Subject.operands(e("((a))"), e("b+c"), e("c+d")).to(InfixExpression.Operator.TIMES), iz("a*(b+c)*(c+d)"));
  }
  @Test public void operandsParenthesisRight() {
    that(Subject.operands(e("((a))"), e("b+c"), e("c")).to(InfixExpression.Operator.TIMES), iz("a*(b+c)*c"));
  }
  @Test public void operandsVanilla() {
    that(Subject.operands(e("((a))"), e("b"), e("c")).to(InfixExpression.Operator.PLUS), iz("a+b+c"));
  }
  @Test public void postfix() {
    that(Subject.operand(e("a")).to(PostfixExpression.Operator.INCREMENT), iz("a++"));
    that(Subject.operand(e("a")).to(PostfixExpression.Operator.DECREMENT), iz("a--"));
  }
  @Test public void postfixPeels() {
    that(Subject.operand(e("((a))")).to(PostfixExpression.Operator.INCREMENT), iz("a++"));
    that(Subject.operand(e("((a))")).to(PostfixExpression.Operator.DECREMENT), iz("a--"));
  }
  @Test public void refitPreservesOrder() {
    final InfixExpression e = i("1 + 2 * 3");
    final List<Expression> operands = new ArrayList<>();
    operands.add(duplicate(e("3*4")));
    operands.add(duplicate(e("5")));
    final InfixExpression refit = Subject.operands(operands).to(e.getOperator());
    that(refit, is(not(e)));
    that(refit.toString(), is("3 * 4 + 5"));
  }
  @Test public void refitWithSort() {
    final InfixExpression e = i("1 + 2 * 3");
    final List<Expression> operands = extract.operands(flatten(e));
    that(operands.size(), is(2));
    that(operands.get(0).toString(), is("1"));
    that(operands.get(1).toString(), is("2 * 3"));
    that(ExpressionComparator.ADDITION.sort(operands), is(true));
    that(operands.get(0).toString(), is("2 * 3"));
    that(operands.get(1).toString(), is("1"));
    final InfixExpression refit = Subject.operands(operands).to(e.getOperator());
    that(refit, is(not(e)));
    that(refit.toString(), is("2 * 3 + 1"));
  }
  @Test public void remainderDoesntAssociate() {
    that(Subject.pair(e("a*B"), e("c*d")).to(InfixExpression.Operator.REMAINDER), iz("(a * B) % (c * d)"));
  }
  @Test public void subjectOperands() {
    final Expression e = Into.e("2 + a < b");
    that(Is.notString(e), is(true));
    final InfixExpression plus = extract.firstPlus(e);
    that(Is.notString(plus), is(true));
    final List<Expression> operands = extract.operands(flatten(plus));
    that(operands.size(), is(2));
    final boolean b = ExpressionComparator.ADDITION.sort(operands);
    that(b, is(true));
    that(Subject.operands(operands).to(plus.getOperator()), iz("a +2"));
  }
  @Test public void subjectOperandsDoesNotIntroduceList() {
    final List<Expression> operands = extract.operands(Funcs.duplicate(i("a*b")));
    that(operands.size(), is(2));
    final InfixExpression e = i("1+2");
    final InfixExpression refit = Subject.operands(operands).to(e.getOperator());
    that(refit.hasExtendedOperands(), is(false));
    that(refit.toString(), is("a + b"));
  }
  @Test public void subjectOperandsIsCorrect() {
    that(Subject.operands(extract.operands(Funcs.duplicate(i("a*b*c")))).to(i("1+2+3").getOperator()).toString(), is("a + b + c"));
  }
  @Test public void subjectOperandsNotNull() {
    that(Subject.operands(extract.operands(Funcs.duplicate(i("a+b+c")))).to(i("1+2+3").getOperator()), notNullValue());
  }
  @Test public void subjectOperandsWithParenthesis() {
    final Expression e = Into.e("(2 + a) * b");
    that(Is.notString(e), is(true));
    final InfixExpression plus = extract.firstPlus(e);
    that(Is.notString(plus), is(true));
    final List<Expression> operands = extract.operands(flatten(plus));
    that(operands.size(), is(2));
    final boolean b = ExpressionComparator.ADDITION.sort(operands);
    that(b, is(true));
    that(Subject.operands(operands).to(plus.getOperator()), iz("a +2"));
  }
  @Test public void substractionsDoesntAssociate() {
    that(Subject.pair(e("a-B"), e("c-d")).to(InfixExpression.Operator.MINUS), iz("(a - B) - (c - d)"));
  }
  @Test public void vanilla() {
    Subject.operand(e("a")).to(PrefixExpression.Operator.NOT);
  }
  @Test public void vanillaCorrectResult() {
    that(Subject.operand(e("a")).to(PrefixExpression.Operator.NOT), iz("!a"));
  }
}
