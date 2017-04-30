package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO orimarco {@code marcovitch.ori@gmail.com} please add a description
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class PercolateExceptionTest {
  @Test public void a() {
    trimmingOf(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) { throw i;}"//
    ) //
        .using(new PercolateException(), CatchClause.class)//
        .gives("{try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){percolate(i);};}")//
        .gives("try{{A.a(b).c().d(e->f[g++]=h(e));}}catch(B i){percolate(i);}")//
        .gives("try{A.a(b).c().d(e->f[g++]=h(e));}catch(B i){percolate(i);}")//
        .gives("try{A.a(b).c().d(λ->f[g++]=h(λ));}catch(B i){percolate(i);}")//
        .stays()//
    ;
  }

  @Test public void b() {
    trimmingOf("try{ thing(); } catch(A ¢){ throw ¢;}catch(B ¢){ throw ¢;}")//
        .gives("try{thing();}catch(B|A ¢){throw ¢;}")//
        .using(new PercolateException(), CatchClause.class)//
        .gives("{try{{thing();}}catch(B|A ¢){percolate(¢);};}")//
        .gives("try{{thing();}}catch(B|A ¢){percolate(¢);}")//
        .gives("try{thing();}catch(B|A ¢){percolate(¢);}")//
        .stays();
  }
}
