package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Unit test for {@link ReturnTernaryExpander} and
 * {@link AssignmentTernaryExpander}
 * @author Raviv Rachmiel
 * @author Dor Ma'ayan
 * @since 8-12-2016 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0883 {
  @Test public void test0() {
    zoomingInto("return a==0? 2:3;").gives("if(a==0)return 2;else return 3;");
  }

  @Test public void test1() {
    zoomingInto("a = a==0? 2:3;").gives("if(a==0)a=2;else a=3;");
  }

  @Test public void test2() {
    zoomingInto("a = a==0? (b==2? 4: 5 ):3;").gives("if(a==0)a=(b==2?4:5);else a=3;");
  }

  @Test public void test3() {
    zoomingInto("a = (a==0? (b==2? 4: 5 ):3);").gives("if(a==0)a=(b==2?4:5);else a=3;");
  }

  @Test public void test4() {
    zoomingInto("a = a==0? 1:2;").gives("if(a==0)a=1;else a=2;");
  }

  @Test public void test5() {
    zoomingInto("a = b==0? (a==0? 1:2) : 4;").gives("if(b==0)a=(a==0?1:2);else a=4;");
  }
}
