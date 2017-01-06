package il.org.spartan.athenizer.inflate.expanders;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

import org.junit.*;

/** Unit test for {@link ThrowTernaryExpander}
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2016-12-26 */
@SuppressWarnings("static-method")
public class Issue0998 {
  @Test public void test0() {
    expansionOf("throw a==0? 2:3;").gives("if(a==0)throw 2; else throw 3;");
  }

  @Test public void test1() {
    expansionOf("throw ((a==0)==42)? f(x):g(x);").gives("if((a==0)==42)throw f(x); else throw g(x);");
  }

  @Test public void test2() {
    expansionOf("throw a ? new A():new B();").gives("if(a)throw new A(); else throw new B();");
  }
}
