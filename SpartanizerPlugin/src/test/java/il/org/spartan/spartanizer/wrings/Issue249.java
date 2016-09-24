package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue249 {
  @Test public void a00() {
    azzert.that(metricUnderTest(null), is(0));
  }

  @Test public void a01() {
    azzert.that(metricUnderTest(""), is(0));
  }

  @Test public void a02() {
    azzert.that(metricUnderTest(";"), is(1));
  }

  public int metricUnderTest(String javaStatements) {
    return metrics.horizontalComplexity(into.s(javaStatements));
  }
}
