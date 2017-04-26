package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link GetOrElseThrow}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-29 */
@SuppressWarnings("static-method")
public class GetOrElseThrowTest {
  @Test public void a() {
    trimmingOf("if(x == null) throw new Error(); return x;")//
        .using(new GetOrElseThrow(), IfStatement.class)//
        .gives("notNull(x).get(x);")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("s1(); if(x == null) throw new Error(); return x; s2();")//
        .using(new GetOrElseThrow(), IfStatement.class)//
        .gives("s1(); notNull(x).get(x); s2();")//
        .stays();
  }

  @Ignore @Test public void c() {
    trimmingOf("s1(); if(x == null) throw new Error(); return x; s2();")//
        .using(IfStatement.class, new ThrowOnNull(), new GetOrElseThrow())//
        .gives("s1(); notNull(x).get(x); s2();")//
        .stays();
  }
}
