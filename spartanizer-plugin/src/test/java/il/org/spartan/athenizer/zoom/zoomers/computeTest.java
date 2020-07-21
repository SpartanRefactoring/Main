package il.org.spartan.athenizer.zoom.zoomers;

import static fluent.ly.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.meta.*;

/** Unit tests for {@link compute}
 * @author Yossi Gil
 * @since 2017-04-01 */
@SuppressWarnings("static-method")
public class computeTest extends MetaFixture {
  @Test public void updatedSpots() {
    compute.updateSpots(parse.s("return a *=2"));
    azzert.that(compute.updateSpots(parse.s("a;")).size(), is(0));
    azzert.that(compute.updateSpots(parse.e("a")).size(), is(0));
    azzert.that(compute.updateSpots(parse.e("a++")).size(), is(1));
    azzert.that(compute.updateSpots(parse.s("a++;")).size(), is(1));
    azzert.that(compute.updateSpots(parse.e("a--")).size(), is(1));
    azzert.that(compute.updateSpots(parse.e("a =2")).size(), is(1));
    azzert.that(compute.updateSpots(parse.s("--a;")).size(), is(1));
    azzert.that(compute.updateSpots(parse.s("a++;")).size(), is(1));
    azzert.that(compute.updateSpots(parse.s("return a =2;")).size(), is(1));
    azzert.that(compute.updateSpots(parse.s("return a *=2;")).size(), is(1));
    final List<ASTNode> updateSpots = compute.updateSpots(parse.s("return local +=2;"));
    azzert.that(updateSpots.size(), is(1));
    azzert.that(the.onlyOneOf(updateSpots) + "", is("local"));
    assert updateSpots.stream().anyMatch(λ -> wizard.eq(λ, the.onlyOneOf(updateSpots).getAST().newSimpleName("local")));
  }
  @Test public void a() {
    azzert.that(compute.useSpots(parse.e("0")).size(), is(0));
  }
  @Test public void b() {
    azzert.that(compute.useSpots(parse.e("a")).size(), is(1));
  }
  @Test public void c() {
    azzert.that(compute.useSpots(parse.e("a+0")).size(), is(1));
  }
  @Test public void d() {
    azzert.that(compute.useSpots(parse.e("f()")).size(), is(0));
  }
  @Test public void e() {
    azzert.that(compute.useSpots(parse.e("this.f(a)")).size(), is(1));
  }
  @Test public void f() {
    final List<String> usedNames = compute.useSpots(parse.e("azzert.that(Extract.usedNames(into.e(X)).size(), is(1))"));
    azzert.that(usedNames + "", usedNames.size(), is(0));
  }
}
