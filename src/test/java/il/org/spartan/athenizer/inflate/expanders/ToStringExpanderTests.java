package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

//TODO Dor: add link to expander class ({@link...), also in the opposite direction if not exists. also change test class name to Issue#
/** Test class for issue #971
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-20 */
@SuppressWarnings("static-method")
public class ToStringExpanderTests {
  @Test public void test0() {
    expandingOf("a+\"\"").gives("a.toString()");
  }
//
//  @Test public void test1() {
//    expanderCheck("\"\"+t()", "t().toString()", new toStringExpander());
//  }
//
//  @Test public void test2() {
//    expanderCheckStays("\"abcd\"+t()", new toStringExpander());
//  }
//
//  @Test public void test3() {
//    expanderCheckStays("\"\"+3", new toStringExpander());
//  }
//
//  @Test public void test4() {
//    expanderCheckStays("true+\"\"", new toStringExpander());
//  }
//
//  @Test public void test5() {
//    expanderCheckStays("\"\"+\"hi\"", new toStringExpander());
//  }
}
