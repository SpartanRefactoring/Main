package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

// TODO Raviv: add @link to tested expander class (also in the opposite
// direction if not exists) and change test class name to Issue#
/** @author Raviv Rachmiel
 * @author Dor Ma'ayan
 * @since 8-12-2016 */
@SuppressWarnings("static-method")
public class TernaryExpanderTests {
  @Test public void test0() {
    expandingOf("return a==0? 2:3;").gives("if(a==0)return 2;else return 3;").stays();
  }

  @Test public void test1() {
    expandingOf("a = a==0? 2:3;").gives("if(a==0)a=2;else a=3;").stays();
  }

  @Test public void test2() {
    expandingOf("a = a==0? (b==2? 4: 5 ):3;").gives("if(a==0)a=(b==2?4:5);else a=3;").gives("if(a==0)if(b==2)a=4;else a=5;else a=3;").stays();
  }

  @Test public void test3() {
    expandingOf("a = (a==0? (b==2? 4: 5 ):3);").gives("if(a==0)a=(b==2?4:5);else a=3;").gives("if(a==0)if(b==2)a=4;else a=5;else a=3;").stays();
  }

  @Test public void test4() {
    expandingOf("a = a==0? 1:2;").gives("if(a==0)a=1;else a=2;").stays();
  }

  @Test public void test5() {
    expandingOf("a = b==0? (a==0? 1:2) : 4;").gives("if(b==0)a=(a==0?1:2);else a=4;");
  }
}
