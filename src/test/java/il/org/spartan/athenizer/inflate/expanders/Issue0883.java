package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

// TODO Raviv: add @link to tested expander class (also in the opposite
// direction if not exists) and change test class name to Issue#
/** Unit Test for the ForBlock expander {@link ReturnTernaryExpander}, issue
 * #883 Also, Unit Test for the ForBlock expander
 * {@link AssignmentTernaryExpander}
 * @author Raviv Rachmiel
 * @author Dor Ma'ayan
 * @since 8-12-2016 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0883 {
  @Test public void test0() {
    expansionOf("return a==0? 2:3;").gives("if(a==0)return 2;else return 3;");
  }

  @Test public void test1() {
    expansionOf("a = a==0? 2:3;").gives("if(a==0)a=2;else a=3;");
  }

  @Test public void test2() {
    expansionOf("a = a==0? (b==2? 4: 5 ):3;").gives("if(a==0)a=(b==2?4:5);else a=3;");
  }

  @Test public void test3() {
    expansionOf("a = (a==0? (b==2? 4: 5 ):3);").gives("if(a==0)a=(b==2?4:5);else a=3;");
  }

  @Test public void test4() {
    expansionOf("a = a==0? 1:2;").gives("if(a==0)a=1;else a=2;");
  }

  @Test public void test5() {
    expansionOf("a = b==0? (a==0? 1:2) : 4;").gives("if(b==0)a=(a==0?1:2);else a=4;");
  }
}
