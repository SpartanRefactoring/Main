package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.utils.Subject.*;

@SuppressWarnings({ "javadoc", "static-method" }) public class SubjectTest {
  @Test public void assignment() {
    azzert.that(Subject.pair(e("a"), e("b")).to(Assignment.Operator.ASSIGN), iz("a=b"));
    azzert.that(Subject.pair(e("a"), e("b")).to(Assignment.Operator.PLUS_ASSIGN), iz("a+=b"));
    azzert.that(Subject.pair(e("a"), e("b")).to(Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN), iz("a>>>=b"));
  }
  @Test public void conditionalExtract() {
    final Pair pair = Subject.pair(e("a-B"), e("(c-d)"));
    azzert.notNull(pair);
    azzert.that(pair.toCondition(e("(x)")), iz("x ? a-B : c-d"));
  }
  @Test public void conditionalSimple() {
    final Pair pair = Subject.pair(e("a-B"), e("(c-d)"));
    azzert.notNull(pair);
    azzert.that(pair.toCondition(e("x")), iz("x ? a-B : c-d"));
  }
  @Test public void divisionDoesntAssociate() {
    azzert.that(Subject.pair(e("a*B"), e("c*d")).to(InfixExpression.Operator.DIVIDE), iz("(a * B) / (c * d)"));
  }
  @Test public void extractcoreLeft() {
    azzert.that(Subject.pair(e("((a-B))"), e("c-d")).to(InfixExpression.Operator.PLUS), iz("a - B + c - d"));
  }
  @Test public void extractcoreRight() {
    azzert.that(Subject.pair(e("a-B"), e("(c-d)")).to(InfixExpression.Operator.PLUS), iz("a - B + c - d"));
  }
  @Test public void makeIfNotStatement() {
    final Statement s = s("s();");
    azzert.that(s, iz("{s();}"));
    azzert.that(Subject.pair(s, s("f();")).toNot(e("a")), iz("if(!a)s(); else f();"));
  }
  @Test public void makeIfStatement() {
    final Statement s = s("s();");
    azzert.that(s, iz("{s();}"));
    azzert.that(Subject.pair(s, s("f();")).toIf(e("a")), iz("if(a)s(); else f();"));
  }
  @Test public void makeIfStatementOfNestedIf() {
    azzert.that(Subject.pair(s("if (a) return b;"), s("if (c) return d;")).toIf(e("x")),
        iz("if(x) {if (a) return b; } else if (c) return d;"));
  }
  @Test public void multiplicationOfAddition() {
    azzert.that(Subject.pair(e("a+B"), e("c+d")).to(InfixExpression.Operator.TIMES), iz("(a + B) * (c + d)"));
  }
  @Test public void multiplicationOfMultiplicatoin() {
    azzert.that(Subject.pair(e("a*B"), e("c*d")).to(InfixExpression.Operator.TIMES), iz("a * B * c * d"));
  }
  @Test public void notPeels() {
    azzert.that(Subject.operand(e("((a))")).to(PrefixExpression.Operator.NOT), iz("!a"));
  }
  @Test public void operandsNoParenthesisRest() {
    azzert.that(Subject.operands(e("((a))"), e("b+c"), e("c+d")).to(InfixExpression.Operator.PLUS), iz("a+b+c+c+d"));
  }
  @Test public void operandsParenthesisLeft() {
    azzert.that(Subject.operands(e("((a+b))"), e("b"), e("c")).to(InfixExpression.Operator.TIMES), iz("(a+b)*b*c"));
  }
  @Test public void operandsParenthesisRest() {
    azzert.that(Subject.operands(e("((a))"), e("b+c"), e("c+d")).to(InfixExpression.Operator.TIMES), iz("a*(b+c)*(c+d)"));
  }
  @Test public void operandsParenthesisRight() {
    azzert.that(Subject.operands(e("((a))"), e("b+c"), e("c")).to(InfixExpression.Operator.TIMES), iz("a*(b+c)*c"));
  }
  @Test public void operandsVanilla() {
    azzert.that(Subject.operands(e("((a))"), e("b"), e("c")).to(InfixExpression.Operator.PLUS), iz("a+b+c"));
  }
  @Test public void postfix() {
    azzert.that(Subject.operand(e("a")).to(PostfixExpression.Operator.INCREMENT), iz("a++"));
    azzert.that(Subject.operand(e("a")).to(PostfixExpression.Operator.DECREMENT), iz("a--"));
  }
  @Test public void postfixPeels() {
    azzert.that(Subject.operand(e("((a))")).to(PostfixExpression.Operator.INCREMENT), iz("a++"));
    azzert.that(Subject.operand(e("((a))")).to(PostfixExpression.Operator.DECREMENT), iz("a--"));
  }
  @Test public void refitPreservesOrder() {
    final InfixExpression e = i("1 + 2 * 3");
    final List<Expression> operands = new ArrayList<>();
    operands.add(duplicate(e("3*4")));
    operands.add(duplicate(e("5")));
    final InfixExpression refit = Subject.operands(operands).to(e.getOperator());
    azzert.that(refit, is(not(e)));
    azzert.that(refit.toString(), is("3 * 4 + 5"));
  }
  @Test public void refitWithSort() {
    final InfixExpression e = i("1 + 2 * 3");
    final List<Expression> operands = extract.operands(flatten(e));
    azzert.that(operands.size(), is(2));
    azzert.that(operands.get(0).toString(), is("1"));
    azzert.that(operands.get(1).toString(), is("2 * 3"));
     azzert.aye(ExpressionComparator.ADDITION.sort(operands));
    azzert.that(operands.get(0).toString(), is("2 * 3"));
    azzert.that(operands.get(1).toString(), is("1"));
    final InfixExpression refit = Subject.operands(operands).to(e.getOperator());
    azzert.that(refit, is(not(e)));
    azzert.that(refit.toString(), is("2 * 3 + 1"));
  }
  @Test public void remainderDoesntAssociate() {
    azzert.that(Subject.pair(e("a*B"), e("c*d")).to(InfixExpression.Operator.REMAINDER), iz("(a * B) % (c * d)"));
  }
  @Test public void subjectOperands() {
    final Expression e = Into.e("2 + a < b");
     azzert.aye(Is.notString(e));
    final InfixExpression plus = extract.firstPlus(e);
     azzert.aye(Is.notString(plus));
    final List<Expression> operands = extract.operands(flatten(plus));
    azzert.that(operands.size(), is(2));
    final boolean b = ExpressionComparator.ADDITION.sort(operands);
    azzert.that(b, is(true));
    azzert.that(Subject.operands(operands).to(plus.getOperator()), iz("a +2"));
  }
  @Test public void subjectOperandsDoesNotIntroduceList() {
    final List<Expression> operands = extract.operands(Funcs.duplicate(i("a*b")));
    azzert.that(operands.size(), is(2));
    final InfixExpression e = i("1+2");
    final InfixExpression refit = Subject.operands(operands).to(e.getOperator());
    azzert.that(refit.hasExtendedOperands(), is(false));
    azzert.that(refit.toString(), is("a + b"));
  }
  @Test public void subjectOperandsIsCorrect() {
    azzert.that(Subject.operands(extract.operands(Funcs.duplicate(i("a*b*c")))).to(i("1+2+3").getOperator()).toString(),
        is("a + b + c"));
  }
  @Test public void subjectOperandsNotNull() {
    azzert.notNull(Subject.operands(extract.operands(Funcs.duplicate(i("a+b+c")))).to(i("1+2+3").getOperator()));
  }
  @Test public void subjectOperandsWithParenthesis() {
    final Expression e = Into.e("(2 + a) * b");
     azzert.aye(Is.notString(e));
    final InfixExpression plus = extract.firstPlus(e);
     azzert.aye(Is.notString(plus));
    final List<Expression> operands = extract.operands(flatten(plus));
    azzert.that(operands.size(), is(2));
    final boolean b = ExpressionComparator.ADDITION.sort(operands);
    azzert.that(b, is(true));
    azzert.that(Subject.operands(operands).to(plus.getOperator()), iz("a +2"));
  }
  @Test public void substractionsDoesntAssociate() {
    azzert.that(Subject.pair(e("a-B"), e("c-d")).to(InfixExpression.Operator.MINUS), iz("(a - B) - (c - d)"));
  }
  @Test public void vanilla() {
    Subject.operand(e("a")).to(PrefixExpression.Operator.NOT);
  }
  @Test public void vanillaCorrectResult() {
    azzert.that(Subject.operand(e("a")).to(PrefixExpression.Operator.NOT), iz("!a"));
  }
}
