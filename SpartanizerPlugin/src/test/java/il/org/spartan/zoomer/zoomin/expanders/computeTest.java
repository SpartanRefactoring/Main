package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.meta.*;

/** Unit tests for {@link compute}
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-04-01 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore("Yossi Gil")
@SuppressWarnings("static-method")
public class computeTest extends MetaFixture {
  @Test public void test1a() {
    compute.updateSpots(into.s("return a *=2"));
    azzert.that(compute.updateSpots(into.s("return a *=2")).size(), is(1));
  }
}
