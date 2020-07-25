package il.org.spartan.spartanizer.ast.engine;

import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.safety.az;

/** Checking that a bug in <code> wizard.ast() </code> was fixed
 * @author Dor Ma'ayan
 * @since 14-11-2016 */
@SuppressWarnings("static-method")
public class Issue0836 {
  @Test public void test0() {
    azzert.that(statements(az.block(make.ast("{int a;return a;}"))).size(), is(2));
  }
  @Test public void test1() {
    azzert.that(statements(az.block(make.ast("{}"))).size(), is(0));
  }
  @Test public void test2() {
    azzert.that(statements(az.block(make.ast("{{int a;}}"))).size(), is(1));
  }
  @Test public void test3() {
    azzert.that(statements(az.block(make.ast("{if(a==4){int a;}return true;}"))).size(), is(2));
  }
}
