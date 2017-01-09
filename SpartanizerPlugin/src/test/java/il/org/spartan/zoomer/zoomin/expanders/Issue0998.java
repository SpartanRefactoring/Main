package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Unit test for {@link ThrowTernaryExpander}
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-26 */
@SuppressWarnings("static-method")
public class Issue0998 {
  @Test public void test0() {
    zoomingInto("throw a==0? 2:3;").gives("if(a==0)throw 2; else throw 3;");
  }

  @Test public void test1() {
    zoomingInto("throw ((a==0)==42)? f(x):g(x);").gives("if((a==0)==42)throw f(x); else throw g(x);");
  }

  @Test public void test2() {
    zoomingInto("throw a ? new A():new B();").gives("if(a)throw new A(); else throw new B();");
  }
}
