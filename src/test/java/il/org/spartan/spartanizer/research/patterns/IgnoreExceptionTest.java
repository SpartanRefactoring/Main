package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class IgnoreExceptionTest {
  @Test public void a() {
    trimmingOf(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) {}"//
    ).using(CatchClause.class, new IgnoreException())//
        .gives("{try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){ignore();};}")//
        .gives("try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){ignore();}")//
        .gives("try{A.a(b).c().d(e->f[g++]=h(e));}catch(B i){ignore();}")//
        .stays()//
    ;
  }

  @Test public void b() {
    trimmingOf("try{ thing(); } catch(A a){}catch(B b){}")//
        .gives("try{thing();}catch(B|A a){}")//
        .using(CatchClause.class, new IgnoreException())//
        .gives("{try{{thing();}}catch(B|A a){ignore();};}")//
        .gives("try{{thing();}}catch(B|A a){ignore();}")//
        .gives("try{thing();}catch(B|A a){ignore();}")//
        .stays();
  }
}
