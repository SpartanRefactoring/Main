package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.CatchClause;
import org.junit.Test;

/** Tests {@link ReturnOnException}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-27 */
@SuppressWarnings("static-method")
public class ReturnOnExceptionTest {
  @Test public void a() {
    trimmingOf(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) {" + //
            "    return null;}"//
    ) //
        .using(new ReturnOnException(), CatchClause.class)//
        .gives("If.throwz(()->{{A.a(b).c().d(e->f[g++]=h(e));}}).returnDefault();")//
        .gives("If.throwz(()->{A.a(b).c().d(e->f[g++]=h(e));}).returnDefault();")//
        .gives("If.throwz(()->A.a(b).c().d(e->f[g++]=h(e))).returnDefault();") //
        .gives("If.throwz(()->A.a(b).c().d(λ->f[g++]=h(λ))).returnDefault();")//
        .stays()//
    ;
  }
  @Test public void b() {
    trimmingOf("try{ thing(); } catch(A a){ return null;}catch(B b){return 3;}")//
        .using(new ReturnOnException(), CatchClause.class)//
        .stays();
  }
  @Test public void c() {
    trimmingOf(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) {" + //
            "    return;}"//
    ) //
        .using(new ReturnOnException(), CatchClause.class)//
        .gives("If.throwz(()->{{A.a(b).c().d(e->f[g++]=h(e));}}).returns();")//
        .gives("If.throwz(()->{A.a(b).c().d(e->f[g++]=h(e));}).returns();") //
        .gives("If.throwz(()->A.a(b).c().d(e->f[g++]=h(e))).returns();") //
        .gives("If.throwz(()->A.a(b).c().d(λ->f[g++]=h(λ))).returns();") //
        .stays()//
    ;
  }
  @Test public void d() {
    trimmingOf("try{ thing(); } catch(A a){ return;}catch(B b){return;}")//
        .gives("try{thing();}catch(B|A a){return;}")//
        .using(new ReturnOnException(), CatchClause.class)//
        .gives("If.throwz(()->{{thing();}}).returns();")//
        .gives("If.throwz(()->{thing();}).returns();")//
        .gives("If.throwz(()->thing()).returns();")//
        .stays();
  }
  @Test public void e() {
    trimmingOf(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) {" + //
            "    return 0;}"//
    ) //
        .using(new ReturnOnException(), CatchClause.class)//
        .gives("If.throwz(()->{{A.a(b).c().d(e->f[g++]=h(e));}}).returnDefault();")//
        .gives("If.throwz(()->{A.a(b).c().d(e->f[g++]=h(e));}).returnDefault();")//
        .gives("If.throwz(()->A.a(b).c().d(e->f[g++]=h(e))).returnDefault();") //
        .gives("If.throwz(()->A.a(b).c().d(λ->f[g++]=h(λ))).returnDefault();")//
        .stays()//
    ;
  }
}
