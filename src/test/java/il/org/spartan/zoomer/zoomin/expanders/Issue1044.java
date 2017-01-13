package il.org.spartan.zoomer.zoomin.expanders;

// import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
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
    Assert.assertEquals("i4", firstBlock.generateName(step.type(az.classInstanceCreation(findFirst.expression(wizard.ast("new Integer(5)"))))));
  }

  @Test public void test1b() {
    Assert.assertEquals("b1", firstBlock.generateName(step.type(az.classInstanceCreation(findFirst.expression(wizard.ast("new B();"))))));
  }

  @Test public void test2a() {
    Assert.assertEquals("i1", functionF.generateName(step.type(az.classInstanceCreation(findFirst.expression(wizard.ast("new Integer(5);"))))));
  }

  @Test public void test2b() {
    Assert.assertEquals("a2", functionF.generateName(step.type(az.classInstanceCreation(findFirst.expression(wizard.ast("new A();"))))));
  }

  @Test public void test3a() {
    Assert.assertEquals("x3", classX.generateName(step.type(az.classInstanceCreation(findFirst.expression(wizard.ast("new X();"))))));
  }
}

/** [[SuppressWarningsSpartan]] */
@SuppressWarnings("all")
class NamespaceFixture {
  {
  }

  int f(final int a1) {
    return a1 >>> hashCode();
  }

  interface X {
    int x1 = 7, x2 = 5;
  }
}
