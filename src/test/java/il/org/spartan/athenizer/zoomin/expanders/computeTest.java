package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.meta.*;
import nano.ly.*;

/** Unit tests for {@link compute}
 * @author Yossi Gil
 * @since 2017-04-01 */
@SuppressWarnings("static-method")
public class computeTest extends MetaFixture {
  @Test public void updatedSpots() {
    compute.updateSpots(into.s("return a *=2"));
    azzert.that(compute.updateSpots(into.s("a;")).size(), is(0));
    azzert.that(compute.updateSpots(into.e("a")).size(), is(0));
    azzert.that(compute.updateSpots(into.e("a++")).size(), is(1));
    azzert.that(compute.updateSpots(into.s("a++;")).size(), is(1));
    azzert.that(compute.updateSpots(into.e("a--")).size(), is(1));
    azzert.that(compute.updateSpots(into.e("a =2")).size(), is(1));
    azzert.that(compute.updateSpots(into.s("--a;")).size(), is(1));
    azzert.that(compute.updateSpots(into.s("a++;")).size(), is(1));
    azzert.that(compute.updateSpots(into.s("return a =2;")).size(), is(1));
    azzert.that(compute.updateSpots(into.s("return a *=2;")).size(), is(1));
    final List<ASTNode> updateSpots = compute.updateSpots(into.s("return local +=2;"));
    azzert.that(updateSpots.size(), is(1));
    azzert.that(the.onlyOneOf(updateSpots) + "", is("local"));
    assert updateSpots.stream().anyMatch(λ -> wizard.eq(λ, the.onlyOneOf(updateSpots).getAST().newSimpleName("local")));
  }

  @Test public void a() {
    azzert.that(compute.useSpots(into.e("0")).size(), is(0));
  }

  @Test public void b() {
    azzert.that(compute.useSpots(into.e("a")).size(), is(1));
  }

  @Test public void c() {
    azzert.that(compute.useSpots(into.e("a+0")).size(), is(1));
  }

  @Test public void d() {
    azzert.that(compute.useSpots(into.e("f()")).size(), is(0));
  }

  @Test public void e() {
    azzert.that(compute.useSpots(into.e("this.f(a)")).size(), is(1));
  }

  @Test public void f() {
    final List<String> usedNames = compute.useSpots(into.e("azzert.that(Extract.usedNames(into.e(X)).size(), is(1))"));
    azzert.that(usedNames + "", usedNames.size(), is(0));
  }
}
