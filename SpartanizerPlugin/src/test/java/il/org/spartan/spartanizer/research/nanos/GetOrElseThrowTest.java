package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/** Tests {@link GetOrElseThrow}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-29 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-method")
public class GetOrElseThrowTest {
  @Test public void a() {
    topDownTrimming("if(x == null) throw new Error(); return x;")//
        .using(IfStatement.class, new GetOrElseThrow())//
        .gives("notNull(x).get(x);")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("s1(); if(x == null) throw new Error(); return x; s2();")//
        .using(IfStatement.class, new GetOrElseThrow())//
        .gives("s1(); notNull(x).get(x); s2();")//
        .stays();
  }

  @Ignore @Test public void c() {
    topDownTrimming("s1(); if(x == null) throw new Error(); return x; s2();")//
        .using(IfStatement.class, new NotNullOrThrow(), new GetOrElseThrow())//
        .gives("s1(); notNull(x).get(x); s2();")//
        .stays();
  }
}
