package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/**
 * @author  Dor Ma'ayan
 * @since  14-11-2016 
 */
@Ignore
@SuppressWarnings("static-method")
public class Issue836 {
  @Test public void test0() {
    azzert.that(az.block(wizard.ast("{int a;return a;}")).statements().size(), is(2));
  }

  @Test public void test1() {
    azzert.that(az.block(wizard.ast("{}")).statements().size(), is(0));
  }

  @Test public void test2() {
    azzert.that(az.block(wizard.ast("{{int a;}}")).statements().size(), is(1));
  }

  @Test public void test3() {
    azzert.that(az.block(wizard.ast("{if(a==4){int a;}return true;}")).statements().size(), is(2));
  }
}
