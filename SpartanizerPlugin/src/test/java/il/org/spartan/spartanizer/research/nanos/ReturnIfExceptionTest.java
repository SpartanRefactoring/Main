package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link ReturnIfException}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-27 */
@SuppressWarnings("static-method")
public class ReturnIfExceptionTest {
  @Test public void a() {
    trimmingOf(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) {" + //
            "    return null;}"//
    ) //
        .using(CatchClause.class, new ReturnIfException())//
        .gives("If.throwz(()->{{A.a(b).c().d(e->f[g++]=h(e));}}).returnNull();")//
        .gives("If.throwz(()->{A.a(b).c().d(e->f[g++]=h(e));}).returnNull();")//
        .gives("If.throwz(()->A.a(b).c().d(λ->f[g++]=h(λ))).returnNull();")//
        .stays()//
    ;
  }

  @Test public void b() {
    trimmingOf("try{ thing(); } catch(A a){ return null;}catch(B b){return 3;}")//
        .using(CatchClause.class, new ReturnIfException())//
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
        .using(CatchClause.class, new ReturnIfException())//
        .gives("If.throwz(()->{{A.a(b).c().d(e->f[g++]=h(e));}}).returns();")//
        .gives("If.throwz(()->{A.a(b).c().d(e->f[g++]=h(e));}).returns();")//
        .gives("If.throwz(()->A.a(b).c().d(λ->f[g++]=h(λ))).returns();")//
        .stays()//
    ;
  }

  @Test public void d() {
    trimmingOf("try{ thing(); } catch(A a){ return;}catch(B b){return;}")//
        .gives("try{thing();}catch(B|A a){return;}")//
        .using(CatchClause.class, new ReturnIfException())//
        .gives("If.throwz(()->{{thing();}}).returns();")//
        .gives("If.throwz(()->{thing();}).returns();")//
        .gives("If.throwz(()->thing()).returns();")//
        .stays();
  }
}
