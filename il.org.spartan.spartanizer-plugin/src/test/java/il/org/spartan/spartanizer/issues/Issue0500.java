package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.parse.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;

/** Tests of {@link ThisClass#thatFunction}
 * @author Yossi Gil
 * @since 2.6 */
// @forget //
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0500 {
  @Test public void a() {
    assert true;
  }
  @Test public void b() {
    assert true;
  }
  @Test public void c$etc() {
    assert true;
  }
  @Test public void chocolate01() {
    assert true;
  }
  @Test public void chocolate02() {
    assert true;
  }
  @Test public void chocolate03etc() {
    assert true;
  }
  @Test public void demoOfAzzert() {
    azzert.that(guessName.of("__"), is(guessName.ANONYMOUS));
    azzert.that(precedence.of(e("a+b")), is(5));
    azzert.that(abbreviate.it(t("List<Set<Integer>> __;")), equalTo("iss"));
    azzert.that(compute.peel(e("-1/-2*-3/-4*-5*-6/-7/-8/-9")), iz("1/2*3/4*5*6/7/8/9"));
    azzert.that(metrics.literals(i("3+4+5+6")), hasItem("6"));
  }
  /** Correct way of trimming does not change */
  @Test public void demoOfTrimming() {
    trimmingOf("a")//
        .stays();
  }
  @Ignore // TODO Yuval Simon
  @Test public void report1() {
    trimmingOf(
        "int a(B b){if(b instanceof C){C<?>c=(C<?>)b;D<E<F,C<G>>>d=e.f().g().h();while(d.i()){E<F,C<G>>j=d.k();F l=j.m();C<G>n=o(j.p(),new H(l));if(!n.q()&&c.r(n)){if(n.s()!=j.p().s())n.t();else d.a();return true;} }}return false;}")//
            .gives(
                "int a(B b){if(b instanceof C){C<?>c=(C<?>)b;for(D<E<F,C<G>>>d=e.f().g().h();d.i();){E<F,C<G>>j=d.k();F l=j.m();C<G>n=o(j.p(),new H(l));if(!n.q()&&c.r(n)){if(n.s()!=j.p().s())n.t();else d.a();return true;}}}return false;}")//
    ;
  }
  @Test public void report2() {
    trimmingOf("public void f(){T e=new Z(){}.g();a(e,new K(){}.s(M.class).g());")//
        .gives("public void f(){a(new Z(){}.g(),new K(){}.s(M.class).g());")//
        .stays();
  }
  @Test public void report3() {
    trimmingOf("void r(){Iterator<Entry<K,C<V>>>e=f();while (e.g())++a;}") //
        .gives("void r(){for(Iterator<Entry<K,C<V>>>e=f();e.g();)++a;}") //
        .gives("void r(){for(Iterator<Entry<K,C<V>>>¢=f();¢.g();)++a;}") //
        .stays();
  }
  /* @Test public void report4() {
   * trimmingOf("int f12(){return new O(){private int g() {O<E<C<K>>> a;}};}")
   * // .gives("int f12(){return new O(){int g() {O<E<C<K>>> a;}};}") //
   * .stays(); } */
  /* @Test public void report5() {
   * trimmingOf("int f12(){return new O(){final int g() {O<E<C<K>>> a;}};}") //
   * .gives("int f12(){return new O(){int g() {O<E<C<K>>> a;}};}") // .stays();
   * } */
  @Test public void vanilla01() {
    assert true;
  }
  @Test public void vanilla02() {
    assert true;
  }
  @Test public void vanilla03etc() {
    assert true;
  }
}
