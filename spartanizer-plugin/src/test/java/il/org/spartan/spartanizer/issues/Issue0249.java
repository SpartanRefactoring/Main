package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.instanceOf;
import static fluent.ly.azzert.is;
import static fluent.ly.azzert.iz;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;
import org.junit.Ignore;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.engine.parse;

/** Unit tests for the GitHub issue thus numbered
 * @author Yossi Gil
 * @since 2016 */
@Ignore
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0249 {
  @Test public void a00() {
    azzert.that(metricUnderTest(null), is(0));
  }
  @Test public void a01() {
    azzert.that(metricUnderTest(""), is(0));
  }
  @Test public void a02() {
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
    final Statement s = parse.s("{}");
    assert s != null;
    azzert.that(s, instanceOf(Block.class));
    final Block b = az.block(s);
    assert b != null;
    assert statements(b) != null;
    azzert.that(statements(b).size(), is(0));
  }
  @Test public void a07() {
    azzert.that(statements(az.block(parse.s("{}"))).size(), is(0));
  }
  @Test public void a08() {
    azzert.that(statements(az.block(parse.s("{}"))), iz("[]"));
  }
  @Test public void a09() {
    azzert.that(az.block(parse.s("{}")), iz("{}"));
  }
  public int metricUnderTest(final String javaStatements) {
    return Metrics.horizontalComplexity(javaStatements == null ? null : parse.s(javaStatements));
  }
}
