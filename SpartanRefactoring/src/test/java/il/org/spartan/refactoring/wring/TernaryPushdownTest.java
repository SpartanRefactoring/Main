package il.org.spartan.refactoring.wring;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;
import il.org.spartan.refactoring.wring.AbstractWringTest.*;
import il.org.spartan.utils.*;

/** Unit tests for {@link Wrings#ADDITION_SORTER}.
 * @author Yossi Gil
 * @since 2014-07-13 */
@SuppressWarnings({ "javadoc", }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class TernaryPushdownTest {
  static final Wring<ConditionalExpression> WRING = new TernaryPushdown();

  @RunWith(Parameterized.class) //
  public static class OutOfScope extends AbstractWringTest.OutOfScope.Exprezzion<ConditionalExpression> {
    static String[][] cases = Utils.asArray(
        //
        new String[] { "Expression vs. Expression", " 6 - 7 < 2 + 1   " }, //
        new String[] { "Literal vs. Literal", "1 < 102333" }, //
        new String[] { "Actual example", "next < values().length" }, //
        new String[] { "No boolean", "a?b:c" }, //
        new String[] { "F X", "a ? false : c" }, //
        new String[] { "T X", "a ? true : c" }, //
        new String[] { "X F", "a ? b : false" }, //
        new String[] { "X T", "a ? b : true" }, //
        new String[] { "() F X", "a ?( false):true" }, //
        new String[] { "() T X", "a ? (((true ))): c" }, //
        new String[] { "() X F", "a ? b : (false)" }, //
        new String[] { "() X T", "a ? b : ((true))" }, //
        new String[] { "Actual example", "!inRange(m, e) ? true : inner.go(r, e)" }, //
        new String[] { "Method invocation first", "a?b():c" }, //
        new String[] { "Not same function invocation ", "a?b(x):d(x)" }, //
        new String[] { "Not same function invocation ", "a?x.f(x):x.d(x)" }, //
        new String[] { "identical method call", "a ? y.f(b) :y.f(b)" }, //
        new String[] { "identical function call", "a ? f(b) :f(b)" }, //
        new String[] { "identical assignment", "a ? (b=c) :(b=c)" }, //
        new String[] { "identical increment", "a ? b++ :b++" }, //
        new String[] { "identical addition", "a ? b+d :b+ d" }, //
        new String[] { "function call", "a ? f(b,c) : f(c)" }, //
        new String[] { "a method call", "a ? y.f(c,b) :y.f(c)" }, //
        new String[] { "a method call distinct receiver", "a ? x.f(c) : y.f(d)" }, //
        new String[] { "not on MINUS", "a ? -c :-d", }, //
        new String[] { "not on NOT", "a ? !c :!d", }, //
        new String[] { "not on MINUSMINUS 1", "a ? --c :--d", }, //
        new String[] { "not on MINUSMINUS 2", "a ? c-- :d--", }, //
        new String[] { "not on PLUSPLUS", "a ? x++ :y++", }, //
        new String[] { "not on PLUS", "a ? +x : +y", }, //
        new String[] { "Into constructor not same arity", "a ? new S(a,new Integer(4),b) : new S(new Ineger(3))" }, //
        new String[] { "field refernece",
            "externalImage ? R.string.webview_contextmenu_image_download_action : R.string.webview_contextmenu_image_save_action", }, //
        null);

    /** Generate test cases for this parameterized class.
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file. */
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
        new String[] { "almost identical function call", "a ? f(b) :f(c)", "f(a ? b : c)" }, //
        new String[] { "almost identical method call", "a ? y.f(b) :y.f(c)", "y.f(a ? b : c)" }, //
        new String[] { "almost identical two arguments function call 1/2", "a ? f(b,x) :f(c,x)", "f(a ? b : c,x)" }, //
        new String[] { "almost identical two arguments function call 2/2", "a ? f(x,b) :f(x,c)", "f(x,a ? b : c)" }, //
        new String[] { "almost identical assignment", "a ? (b=c) :(b=d)", "b = a ? c : d" }, //
        new String[] { "almost identical 2 addition", "a ? b+d :b+ c", "b+(a ? d : c)" }, //
        new String[] { "almost identical 3 addition", "a ? b+d +x:b+ c + x", "b+(a ? d : c) + x" }, //
        new String[] { "almost identical 4 addition last", "a ? b+d+e+y:b+d+e+x", "b+d+e+(a ? y : x)" }, //
        new String[] { "almost identical 4 addition second", "a ? b+x+e+f:b+y+e+f", "b+(a ? x : y)+e+f" }, //
        new String[] { "different target field refernce", "a ? 1 + x.a : 1 + y.a", "1+(a ? x.a : y.a)" }, //
        new String[] { "Into constructor 1/1 location", "a.equal(b) ? new S(new Integer(4)) : new S(new Ineger(3))",
            "new S(a.equal(b)? new Integer(4): new Ineger(3))" }, //
        new String[] { "Into constructor 1/3", "a.equal(b) ? new S(new Integer(4),a,b) : new S(new Ineger(3),a,b)",
            "new S(a.equal(b)? new Integer(4): new Ineger(3), a, b)" }, //
        new String[] { "Into constructor 2/3", "a.equal(b) ? new S(a,new Integer(4),b) : new S(a, new Ineger(3), b)",
            "new S(a,a.equal(b)? new Integer(4): new Ineger(3),b)" }, //
        new String[] { "Into constructor 3/3", "a.equal(b) ? new S(a,b,new Integer(4)) : new S(a,b,new Ineger(3))",
            "new S(a, b, a.equal(b)? new Integer(4): new Ineger(3))" }, //
        null);

    /** Generate test cases for this parameterized class.
     * @return a collection of cases, where each case is an array of three
     *         objects, the test case name, the input, and the file. */
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
