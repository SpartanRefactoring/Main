package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.meta.*;

/** Test class for name generation from Namespace (Environments)
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2017-01-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "javadoc" })
public class Issue1044 extends MetaFixture {
  private final Namespace fixtureClass = Environment.of(reflectedCompilationUnit()).getChild(1);
  private final Namespace firstBlock = fixtureClass.getChild(0);
  private final Namespace functionF = fixtureClass.getChild(1);
  private final Namespace classX = fixtureClass.getChild(2);

  @Test public void test1a() {
    azzert.that(firstBlock.generateName(type(az.classInstanceCreation(findFirst.expression(wizard.ast("new Integer(5)"))))), is("i4"));
  }

  @Test public void test1b() {
    azzert.that(firstBlock.generateName(type(az.classInstanceCreation(findFirst.expression(wizard.ast("new B();"))))), is("b1"));
  }

  @Test public void test2a() {
    azzert.that(functionF.generateName(type(az.classInstanceCreation(findFirst.expression(wizard.ast("new Integer(5);"))))), is("i1"));
  }

  @Test public void test2b() {
    azzert.that(functionF.generateName(type(az.classInstanceCreation(findFirst.expression(wizard.ast("new A();"))))), is("a2"));
  }

  @Test public void test3a() {
    azzert.that(classX.generateName(type(az.classInstanceCreation(findFirst.expression(wizard.ast("new X();"))))), is("x3"));
  }
}

/** This class' only purpose is for the testing above.
 * @author Doron Mehsulam <tt>doronmmm@hotmail.com</tt>
 * @since 2017-01-26 */
class NamespaceFixture {
  static {
    int i1 = 0, i2 = 0, i3 = 0;
    ++i1;
    ++i2;
    ++i3;
    System.out.println(i1 + i2 + i3);
  }

  int f(final int a1) {
    return a1 >>> hashCode();
  }

  interface X {
    int x1 = 7, x2 = 5;
  }
}
