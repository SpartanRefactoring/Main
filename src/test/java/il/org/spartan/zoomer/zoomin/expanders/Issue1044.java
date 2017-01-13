package il.org.spartan.zoomer.zoomin.expanders;

import org.eclipse.jdt.core.dom.*;
// import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.java.namespace.*;

/** Test class for name generation from Namespace (Environments) 
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2017-01-10 */

@SuppressWarnings({ "javadoc" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Issue1044 extends ReflectiveTester {
  private final Namespace fixtureClass = Environment.of(myCompilationUnit()).getChild(1);
  private final Namespace firstBlock = fixtureClass.getChild(0);
  private final Namespace functionF = fixtureClass.getChild(1);
  private final Namespace classX = fixtureClass.getChild(2);
  
  @Test public void test1a() {
    Expression e = findFirst.expression(wizard.ast("new Integer(5);"));
    String name = firstBlock.generateName(e);
    Assert.assertEquals("integer4", name);
  }
  @Test public void test1b() {
    Expression e = findFirst.expression(wizard.ast("new B();"));
    String name = firstBlock.generateName(e);
    Assert.assertEquals("b1", name);
  }
  @Test public void test2a() {
    Expression e = findFirst.expression(wizard.ast("new Integer(5);"));
    String name = functionF.generateName(e);
    Assert.assertEquals("integer1", name);
  }
  @Test public void test2b() {
    Expression e = findFirst.expression(wizard.ast("new A();"));
    String name = functionF.generateName(e);
    Assert.assertEquals("a2", name);
  }
  
  @Test public void test3() {
    Expression e = findFirst.expression(wizard.ast("new X();"));
    String name = classX.generateName(e);
    Assert.assertEquals("x3", name);
  }
}

class NamespaceFixture {
  {
    Integer integer1 = 2, integer3 = 4, integer2 = 5;
    class b1 {}
  }

  int f(final int a1) {
    Integer integer4 = 5;
    return a1 >>> hashCode();
  }

  interface X {
    int x1 = 7, x2 = 5;
  }
}
