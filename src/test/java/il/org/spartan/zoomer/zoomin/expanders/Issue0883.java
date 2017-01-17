package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Unit test for {@link ReturnTernaryExpander} and
 * {@link AssignmentTernaryExpander}
 * @author Raviv Rachmiel
 * @author Dor Ma'ayan
 * @since 8-12-2016 */
@SuppressWarnings("static-method")
public class Issue0883 {
  @Test public void test0() {
    bloatingOf("return a==0? 2:3;")//
        .gives("if(a==0)return 2;"
            + " return 3;");
  }

  @Test public void test1() {
    bloatingOf("a = a==0? 2:3;")//
        .gives("if(a==0)a=2;else a=3;");
  }

  @Test public void test2() {
    bloatingOf("a = a==0? (b==2? 4: 5 ):3;")//
        .gives("if(a==0)a=(b==2?4:5);else a=3;");
  }

  @Test public void test3() {
    bloatingOf("a = (a==0? (b==2? 4: 5 ):3);")//
        .gives("if(a==0)a=(b==2?4:5);else a=3;");
  }

  @Test public void test4() {
    bloatingOf("a = a==0? 1:2;")//
        .gives("if(a==0)a=1;else a=2;");
  }

  @Test public void test5() {
    bloatingOf("a = b==0? (a==0? 1:2) : 4;")//
        .gives("if(b==0)a=(a==0?1:2);else a=4;");
  }
}
