package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link SuppressException}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class SuppressExceptionTest {
  @Test public void a() {
    trimmingOf(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) {}"//
    ) //
        .using(CatchClause.class, new SuppressException())//
        .gives("{try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){ignore();};}")//
        .gives("try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){ignore();}")//
        .gives("try{A.a(b).c().d(e->f[g++]=h(e));}catch(B i){ignore();}")//
        .gives("try{A.a(b).c().d(λ->f[g++]=h(λ));}catch(B i){ignore();}")//
        .stays()//
    ;
  }

  @Test public void b() {
    trimmingOf("try{ thing(); } catch(A a){}catch(B b){}")//
        .gives("try{thing();}catch(B|A a){}")//
        .using(CatchClause.class, new SuppressException())//
        .gives("{try{{thing();}}catch(B|A a){ignore();};}")//
        .gives("try{{thing();}}catch(B|A a){ignore();}")//
        .gives("try{thing();}catch(B|A a){ignore();}")//
        .stays();
  }
}
