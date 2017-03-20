package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.azzert.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Checking that a bug in <code> wizard.ast() </code> was fixed
 * @author Dor Ma'ayan
 * @since 14-11-2016 */
@SuppressWarnings("static-method")
public class Issue0836 {
  @Test public void test0() {
    azzert.that(statements(az.block(wizard.ast("{int a;return a;}"))).size(), is(2));
  }

  @Test public void test1() {
    azzert.that(statements(az.block(wizard.ast("{}"))).size(), is(0));
  }

  @Test public void test2() {
    azzert.that(statements(az.block(wizard.ast("{{int a;}}"))).size(), is(1));
  }

  @Test public void test3() {
    azzert.that(statements(az.block(wizard.ast("{if(a==4){int a;}return true;}"))).size(), is(2));
  }
}
