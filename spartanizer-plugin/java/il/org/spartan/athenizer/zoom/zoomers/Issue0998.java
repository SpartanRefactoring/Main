package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.zoomers.*;

/** Unit test for {@link ThrowTernaryBloater}
 * @author Doron Meshulam {@code doronmmm@hotmail.com}
 * @since 2016-12-26 */
@SuppressWarnings("static-method")
public class Issue0998 {
  @Test public void test0() {
    bloatingOf("throw a==0? 2:3;")//
        .gives("if(a==0)throw 2; else throw 3;");
  }
  @Test public void test1() {
    bloatingOf("throw ((a==0)==42)? f(x):g(x);")//
        .gives("if((a==0)==42)throw f(x); else throw g(x);");
  }
  @Test public void test2() {
    bloatingOf("throw a ? new A():new B();")//
        .gives("if(a)throw new A(); else throw new B();");
  }
}
