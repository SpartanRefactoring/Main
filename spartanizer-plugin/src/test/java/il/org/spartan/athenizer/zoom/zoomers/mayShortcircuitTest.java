package il.org.spartan.athenizer.zoom.zoomers;

import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.ast.navigate.step.type;

import org.eclipse.jdt.core.dom.Expression;
import org.junit.Ignore;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.navigate.findFirst;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.java.namespace.Environment;
import il.org.spartan.spartanizer.java.namespace.Namespace;
import il.org.spartan.spartanizer.meta.MetaFixture;

/** @author Yossi Gil
 * @since 2017-01-28 */
@Ignore("Yossi Gil")
@SuppressWarnings("javadoc")
public class mayShortcircuitTest extends MetaFixture {
  private final Namespace fixtureClass = Environment.of(reflectedCompilationUnit()).getChild(1);
  private final Namespace firstBlock = fixtureClass.getChild(0);
  private final Namespace functionF = fixtureClass.getChild(1);
  private final Namespace classX = fixtureClass.getChild(2);

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
    azzert.that(classX.generateName(type(az.classInstanceCreation(findFirst.instanceOf(Expression.class).in(make.ast("new X();"))))), is("x3"));
  }
}
