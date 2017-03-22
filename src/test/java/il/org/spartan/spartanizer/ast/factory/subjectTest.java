/* TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 *
 * @since Oct 19, 2016 */
package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.subject.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

@SuppressWarnings({ "javadoc", "static-method" })
public final class subjectTest {
  @Test public void assignment() {
    azzert.that(subject.pair(e("a"), e("b")).to(Assignment.Operator.ASSIGN), iz("a=b"));
    azzert.that(subject.pair(e("a"), e("b")).to(Assignment.Operator.PLUS_ASSIGN), iz("a+=b"));
    azzert.that(subject.pair(e("a"), e("b")).to(Assignment.Operator.RIGHT_SHIFT_UNSIGNED_ASSIGN), iz("a>>>=b"));
  }

  @Test public void conditionalExtract() {
    @NotNull final Pair pair = subject.pair(e("a-B"), e("(c-d)"));
    assert pair != null;
    azzert.that(pair.toCondition(e("(x)")), iz("x ? a-B : c-d"));
  }

  @Test public void conditionalSimple() {
    @NotNull final Pair pair = subject.pair(e("a-B"), e("(c-d)"));
    assert pair != null;
    azzert.that(pair.toCondition(e("x")), iz("x ? a-B : c-d"));
  }

  @Test public void divisionDoesntAssociate() {
    azzert.that(subject.pair(e("a*B"), e("c*d")).to(DIVIDE), iz("a * B / (c * d)"));
  }

  @Test public void extractcoreLeft() {
    azzert.that(subject.pair(e("((a-B))"), e("c-d")).to(PLUS2), iz("a - B + c - d"));
  }

  @Test public void extractcoreRight() {
    azzert.that(subject.pair(e("a-B"), e("(c-d)")).to(PLUS2), iz("a - B + c - d"));
  }

  @Test public void makeIfNotStatement() {
    @NotNull final Statement s = s("s();");
    azzert.that(s, iz("{s();}"));
    azzert.that(subject.pair(s, s("f();")).toNot(e("a")), iz("if(!a)s(); else f();"));
  }

  @Test public void makeIfStatement() {
    @NotNull final Statement s = s("s();");
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
    azzert.that(subject.operands(e("((a-b))"), e("b-c"), e("c+d")).to(PLUS2), iz("a-b+b-c+c+d"));
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
    azzert.that(subject.operands(e("((a))"), e("b"), e("c")).to(PLUS2), iz("a+b+c"));
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
    @NotNull final InfixExpression e = i("1 + 2 * 3");
    @NotNull final List<Expression> operands = new ArrayList<>();
    operands.add(copy.of(e("3*4")));
    operands.add(copy.of(e("5")));
    final InfixExpression refit = subject.operands(operands).to(e.getOperator());
    azzert.that(refit, is(not(e)));
    azzert.that(refit + "", is("3 * 4 + 5"));
  }

  @Test public void refitWithSort() {
    @NotNull final InfixExpression e = i("1 + 2 * 3");
    @Nullable final List<Expression> operands = hop.operands(flatten.of(e));
    azzert.that(operands.size(), is(2));
    azzert.that(first(operands) + "", is("1"));
    azzert.that(second(operands) + "", is("2 * 3"));
    assert ExpressionComparator.ADDITION.sort(operands);
    azzert.that(first(operands) + "", is("2 * 3"));
    azzert.that(second(operands) + "", is("1"));
    final InfixExpression refit = subject.operands(operands).to(e.getOperator());
    azzert.that(refit, is(not(e)));
    azzert.that(refit + "", is("2 * 3 + 1"));
  }

  @Test public void remainderDoesntAssociate() {
    azzert.that(subject.pair(e("a*B"), e("c*d")).to(REMAINDER), iz("a * B % (c * d)"));
  }

  @Test public void subjectOperands() {
    @NotNull final Expression e = into.e("2 + a <b");
    assert type.isNotString(e);
    final InfixExpression plus = findFirst.infixPlus(e);
    assert type.isNotString(plus);
    @Nullable final List<Expression> operands = hop.operands(flatten.of(plus));
    azzert.that(operands.size(), is(2));
    assert ExpressionComparator.ADDITION.sort(operands);
    azzert.that(subject.operands(operands).to(plus.getOperator()), iz("a +2"));
  }

  @Test public void subjectOperandsDoesNotIntroduceList() {
    @Nullable final List<Expression> operands = hop.operands(copy.of(i("a*b")));
    azzert.that(operands.size(), is(2));
    final InfixExpression refit = subject.operands(operands).to(i("1+2").getOperator());
    assert !refit.hasExtendedOperands();
    azzert.that(refit + "", is("a + b"));
  }

  @Test public void subjectOperandsIsCorrect() {
    azzert.that(subject.operands(hop.operands(copy.of(i("a*b*c")))).to(i("1+2+3").getOperator()) + "", is("a + b + c"));
  }

  @Test public void subjectOperandsNotNull() {
    assert subject.operands(hop.operands(copy.of(i("a+b+c")))).to(i("1+2+3").getOperator()) != null;
  }

  @Test public void subjectOperandsWithParenthesis() {
    @NotNull final Expression e = into.e("(2 + a) * b");
    assert type.isNotString(e);
    final InfixExpression plus = findFirst.infixPlus(e);
    assert type.isNotString(plus);
    @Nullable final List<Expression> operands = hop.operands(flatten.of(plus));
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
