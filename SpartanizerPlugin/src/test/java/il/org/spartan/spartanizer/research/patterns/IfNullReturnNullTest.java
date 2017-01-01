package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class IfNullReturnNullTest {
  @Test public void a() {
    trimmingOf(//
        "try {" + //
            "    A.a(b).c().d(e -> f[g++]=h(e));" + //
            "  }" + //
            " catch (  B i) {" + //
            "    return null;"//
    ).withTipper(TryStatement.class, new IfThrowsReturnNull())//
        .gives("If.throwz(()->{{A.a(b).c().d(e->f[g++]=h(e));}}).returnNull();}")//
        .gives("If.throwz(()->{A.a(b).c().d(e->f[g++]=h(e));}).returnNull();}")//
        .gives("If.throwz(()->A.a(b).c().d(e->f[g++]=h(e))).returnNull();}")//
        .stays()//
    ;
  }
}
