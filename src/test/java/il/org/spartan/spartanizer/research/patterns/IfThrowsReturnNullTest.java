package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-27 */
@SuppressWarnings("static-method")
public class IfThrowsReturnNullTest {
  @Test public void a() {
    trimmingOf(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) {" + //
            "    return null;}"//
    ).using(CatchClause.class, new IfThrowsReturnNull())//
        .gives("If.throwz(()->{{A.a(b).c().d(e->f[g++]=h(e));}}).returnNull();")//
        .gives("If.throwz(()->{A.a(b).c().d(e->f[g++]=h(e));}).returnNull();")//
        .gives("If.throwz(()->A.a(b).c().d(e->f[g++]=h(e))).returnNull();")//
        .stays()//
    ;
  }

  @Test public void b() {
    trimmingOf("try{ thing(); } catch(A a){ return null;}catch(B b){return 3;}")//
        .using(CatchClause.class, new IfThrowsReturnNull())//
        .stays();
  }
}
