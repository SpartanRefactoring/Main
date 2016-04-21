package il.org.spartan.refactoring.wring;

import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.hamcrest.MatcherAssert.iz;
import static il.org.spartan.hamcrest.OrderingComparison.greaterThan;
import static il.org.spartan.refactoring.utils.Funcs.logicalNot;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

import java.util.Collection;

import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import il.org.spartan.refactoring.utils.Extract;
import il.org.spartan.refactoring.utils.Into;
import il.org.spartan.refactoring.utils.Is;
import il.org.spartan.refactoring.utils.Subject;
import il.org.spartan.refactoring.wring.AbstractWringTest.OutOfScope;
import il.org.spartan.utils.Utils;

/**
 * Unit tests for {@link Wrings#ADDITION_SORTER}.
 *
 * @author Yossi Gil
 * @since 2014-07-13
 */
@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class TernaryShortestFirstTest {
  static final Wring<ConditionalExpression> WRING = new TernaryShortestFirst();

  @Test public void cyclicBug() {
    final ConditionalExpression e = Into
        .c("length(not(notConditional)) + length(then) < length(notConditional) + length(elze) ? null : $");
    assertThat(e, notNullValue());
    final Expression elze = Extract.core(e.getElseExpression());
    final Expression then = Extract.core(e.getThenExpression());
    final Expression $ = Subject.pair(elze, then).toCondition(logicalNot(e.getExpression()));
    assertFalse(then.toString(), Is.conditional(then));
    assertFalse(elze.toString(), Is.conditional(elze));
    assertThat($.toString().length(), greaterThan(0));
    assertThat($, iz("length(not(notConditional)) + length(then) >= length(notConditional) + length(elze) ? $ : null"));
  }
  @Test public void trace1() {
    final ConditionalExpression e = Into.c("a?f(b,c,d):a");
    assertThat(e, notNullValue());
    assertThat(Subject.pair(Extract.core(e.getElseExpression()), Extract.core(e.getThenExpression()))
        .toCondition(logicalNot(e.getExpression())), iz("!a?a:f(b,c,d)"));
  }
  @Test public void trace2() {
    final ConditionalExpression e = Into.c("!f(o) ? null : x.f(a).to(e.g())");
    assertThat(e, notNullValue());
    final Expression elze = Extract.core(e.getElseExpression());
    final Expression then = Extract.core(e.getThenExpression());
    final Expression $ = Subject.pair(elze, then).toCondition(logicalNot(e.getExpression()));
    assertFalse(then.toString(), Is.conditional(then));
    assertFalse(elze.toString(), Is.conditional(elze));
    assertThat($.toString().length(), greaterThan(0));
    assertThat($, iz("f(o) ? x.f(a).to(e.g()) : null"));
  }

  @RunWith(Parameterized.class) //
  public static class OutOfScope extends AbstractWringTest.OutOfScope.Exprezzion<ConditionalExpression> {
    static String[][] cases = Utils.asArray(//
        new String[] { "Strange cyclic buc",
            "length(not(notConditional))+length(then)>=length(notConditional)+length(elze)?$:null", }, //
        new String[] { "Actual simplified 3", "!f(o) ? null : x.f(a).to(e.g())" }, //
        new String[] { "Actual simplified 2", "!f(o) ? null : Subject.operands(operands).to(e.getOperator())" }, //
        new String[] { "Actual simplified 1", "!f(operands) ? null : Subject.operands(operands).to(e.getOperator())" }, //
        new String[] { "Actual", "!tryToSort(operands) ? null : Subject.operands(operands).to(e.getOperator())" }, //
        new String[] { "No boolean", "a?b:c" }, //
        new String[] { "() T X", "a ? c : (((true )))" }, //
        new String[] { "Actual example", "!inRange(m, e) ? true : inner.go(r, e)" }, //
        new String[] { "Not same function invocation ", "a?b(x):d(x)" }, //
        new String[] { "identical method call", "a ? y.f(b) :y.f(b)" }, //
        new String[] { "identical function call", "a ? f(b) :f(b)" }, //
        new String[] { "identical assignment", "a ? (b=c) :(b=c)" }, //
        new String[] { "identical increment", "a ? b++ :b++" }, //
        new String[] { "identical addition", "a ? b+d :b+ d" }, //
        new String[] { "a method call", "a ? y.f(c,b) :y.f(e,e,f)" }, //
        new String[] { "a method call distinct receiver", "a ? x.f(c) : y.f(d)" }, //
        new String[] { "not on MINUS", "a ? -c :-d", }, //
        new String[] { "not on NOT", "a ? !c :!d", }, //
        new String[] { "not on MINUSMINUS 1", "a ? --c :--d", }, //
        new String[] { "not on MINUSMINUS 2", "a ? c-- :d--", }, //
        new String[] { "not on PLUSPLUS", "a ? x++ :y++", }, //
        new String[] { "not on PLUS", "a ? +x : +y", }, //
        new String[] { "Into constructor not same arity", "a ? new S(a,new Integer(4),b) : new S(new Ineger(3,a,v,y))" }, //
        new String[] { "field refernece", "externalImage ? a : R.string.webview_contextmenu_image_download_action", }, //
        new String[] { "almost identical method call", "a ? y.f(b) :y.f(c)", }, //
        new String[] { "almost identical two arguments function call 1/2", "a ? f(b,x) :f(c,x)", }, //
        new String[] { "almost identical assignment", "a ? (b=c) :(b=d)", }, //
        new String[] { "almost identical 2 addition", "a ? b+d :b+ c", }, //
        new String[] { "almost identical 3 addition", "a ? b+d +x:b+ c + x", }, //
        new String[] { "almost identical 4 addition last", "a ? b+d+e+y:b+d+e+x", }, //
        new String[] { "almost identical 4 addition second", "a ? b+x+e+f:b+y+e+f", }, //
        new String[] { "different target field refernce", "a ? 1 + x.a : 1 + y.a" }, //
        null);

    /**
     * Generate test cases for this parameterized class.
     *
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file.
     */
    @Parameters(name = DESCRIPTION) //
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    /** Instantiates the enclosing class ({@link OutOfScope}) */
    public OutOfScope() {
      super(WRING);
    }
  }

  @RunWith(Parameterized.class) //
  @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
  public static class Wringed extends AbstractWringTest.WringedExpression.Conditional {
    private static String[][] cases = Utils.asArray(//
        new String[] { "Vanilla", "a?f(a,b,c):f(b)", "!a?f(b):f(a,b,c)" }, //
        new String[] { "Bug of being cyclice", //
            "length(not(notConditional)) + length(then) < length(notConditional) + length(elze) ? null : $", //
            "length(not(notConditional))+length(then)>=length(notConditional)+length(elze)?$:null",//
        }, //
        null);

    /**
     * Generate test cases for this parameterized class.
     *
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file.
     */
    @Parameters(name = DESCRIPTION) //
    public static Collection<Object[]> cases() {
      return collect(cases);
    }
    /** Instantiates the enclosing class ({@link WringedExpression}) */
    public Wringed() {
      super(WRING);
    }
  }
}
