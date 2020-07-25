package il.org.spartan.athenizer.zoom.zoomers;

import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.ast.navigate.step.type;

import org.eclipse.jdt.core.dom.Expression;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.java.namespace.Environment;
import il.org.spartan.spartanizer.java.namespace.Namespace;
import il.org.spartan.spartanizer.meta.MetaFixture;

/** Test class for name generation from Namespace (Environments)
 * @author Doron Meshulam {@code doronmmm@hotmail.com}
 * @since 2017-01-10 */
@SuppressWarnings("javadoc")
public class Issue1044 extends MetaFixture {
  private final Namespace fixtureClass = Environment.of(reflectedCompilationUnit()).getChild(1);
  private final Namespace firstBlock = fixtureClass.getChild(0);
  private final Namespace functionF = fixtureClass.getChild(1);
  private final Namespace clazz = fixtureClass.getChild(2);

  @Test public void test1a() {
    azzert.that(firstBlock.generateName(type(az.classInstanceCreation(findFirst.instanceOf(Expression.class).in(make.ast("new Integer(5)"))))), is("i4"));
  }
  @Test public void test1b() {
    azzert.that(firstBlock.generateName(type(az.classInstanceCreation(findFirst.instanceOf(Expression.class).in(make.ast("new B();"))))), is("b1"));
  }
  @Test public void test2a() {
    azzert.that(functionF.generateName(type(az.classInstanceCreation(findFirst.instanceOf(Expression.class).in(make.ast("new Integer(5);"))))), is("i1"));
  }
  @Test public void test2b() {
    azzert.that(functionF.generateName(type(az.classInstanceCreation(findFirst.instanceOf(Expression.class).in(make.ast("new A();"))))), is("a2"));
  }
  @Test public void test3a() {
    azzert.that(clazz.generateName(type(az.classInstanceCreation(findFirst.instanceOf(Expression.class).in(make.ast("new X();"))))), is("x3"));
  }
}

/** This class' only purpose is for the testing above.
 * @author Doron Mehsulam {@code doronmmm@hotmail.com}
 * @since 2017-01-26 [[SuppressWarningsSpartan]] */
@SuppressWarnings("TooBroadScope")
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
