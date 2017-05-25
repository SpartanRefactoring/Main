package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link IgnoringExceptions}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class IgnoringExceptionsTest {
  @Test public void a() {
    trimmingOf(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) {}"//
    ) //
        .using(new IgnoringExceptions(), CatchClause.class)//
        .gives("{try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){forget();};}")//
        .gives("try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){forget();}")//
        .gives("try{A.a(b).c().d(e->f[g++]=h(e));}catch(B i){forget();}")//
        .gives("try{A.a(b).c().d(λ->f[g++]=h(λ));}catch(B i){forget();}")//
        .stays()//
    ;
  }
  @Test public void b() {
    trimmingOf("try{ thing(); } catch(A a){}catch(B b){}")//
        .gives("try{thing();}catch(B|A a){}")//
        .using(new IgnoringExceptions(), CatchClause.class)//
        .gives("{try{{thing();}}catch(B|A a){forget();};}")//
        .gives("try{{thing();}}catch(B|A a){forget();}")//
        .gives("try{thing();}catch(B|A a){forget();}")//
        .stays();
  }
}
