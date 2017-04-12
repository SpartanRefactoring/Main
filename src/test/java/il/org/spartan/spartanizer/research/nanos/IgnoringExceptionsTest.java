package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** Tests {@link IgnoringExceptions}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class IgnoringExceptionsTest {
  @Test public void a() {
    trimminKof(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) {}"//
    ) //
        .using(new IgnoringExceptions(), CatchClause.class)//
        .gives("{try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){ignore();};}")//
        .gives("try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){ignore();}")//
        .gives("try{A.a(b).c().d(e->f[g++]=h(e));}catch(B i){ignore();}")//
        .gives("try{A.a(b).c().d(λ->f[g++]=h(λ));}catch(B i){ignore();}")//
        .stays()//
    ;
  }

  @Test public void b() {
    trimminKof("try{ thing(); } catch(A a){}catch(B b){}")//
        .gives("try{thing();}catch(B|A a){}")//
        .using(new IgnoringExceptions(), CatchClause.class)//
        .gives("{try{{thing();}}catch(B|A a){ignore();};}")//
        .gives("try{{thing();}}catch(B|A a){ignore();}")//
        .gives("try{thing();}catch(B|A a){ignore();}")//
        .stays();
  }
}
