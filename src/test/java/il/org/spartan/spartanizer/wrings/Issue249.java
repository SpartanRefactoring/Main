package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Yossi Gil
 * @since 2016 */
// @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue249 {
  @Test public void a00() {
    azzert.that(metricUnderTest(null), is(0));
  }

  @Ignore @Test public void a01() {
    azzert.that(metricUnderTest(""), is(0));
  }

  @Ignore @Test public void a02() {
    azzert.that(metricUnderTest(";"), is(1));
  }

  @Test public void a03() {
    azzert.that(metricUnderTest("{}"), is(2));
  }

  @Test public void a04() {
    azzert.that(metricUnderTest("{;}"), is(4));
  }

  @Test public void a05() {
    azzert.that(metricUnderTest("{{}}"), is(4));
  }
  @Test public void a06() {
    final Statement s = into.s("{}");
    assert s != null;
    azzert.that(s, instanceOf(Block.class));
    final Block b = az.block(s);
    assert b != null;
    assert step.statements(b) != null; 
    azzert.that(step.statements(b).size(), is(0));
  }

  public int metricUnderTest(String javaStatements) {
    if (javaStatements == null)
      return metrics.horizontalComplexity(null);
    return metrics.horizontalComplexity(into.s(javaStatements));
  }
}
