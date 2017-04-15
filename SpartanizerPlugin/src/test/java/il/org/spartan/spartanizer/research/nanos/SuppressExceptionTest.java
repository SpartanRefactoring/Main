package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link SuppressException}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class SuppressExceptionTest {
  @Test public void a() {
    trimminKof(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) {}"//
    ) //
        .using(new SuppressException(), CatchClause.class)//
        .gives("{try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){void¢();};}")//
        .gives("try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){void¢();}")//
        .gives("try{A.a(b).c().d(e->f[g++]=h(e));}catch(B i){void¢();}")//
        .gives("try{A.a(b).c().d(λ->f[g++]=h(λ));}catch(B i){void¢();}")//
        .stays()//
    ;
  }

  @Test public void b() {
    trimminKof("try{ thing(); } catch(A a){}catch(B b){}")//
        .gives("try{thing();}catch(B|A a){}")//
        .using(new SuppressException(), CatchClause.class)//
        .gives("{try{{thing();}}catch(B|A a){void¢();};}")//
        .gives("try{{thing();}}catch(B|A a){void¢();}")//
        .gives("try{thing();}catch(B|A a){void¢();}")//
        .stays();
  }
}
