package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/** Tests @link GetOrElseThrow
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-29 */
@SuppressWarnings("static-method")
public class GetOrElseThrowTest {
  @Test public void a() {
    trimmingOf("if(x == null) throw new Error(); return x;")//
        .using(IfStatement.class, new GetOrElseThrow())//
        .gives("notNull(x).get(x);")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("s1(); if(x == null) throw new Error(); return x; s2();")//
        .using(IfStatement.class, new GetOrElseThrow())//
        .gives("s1(); notNull(x).get(x); s2();")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("s1(); if(x == null) throw new Error(); return x; s2();")//
        .using(IfStatement.class, new NotNullOrThrow(), new GetOrElseThrow())//
        .gives("s1(); notNull(x).get(x); s2();")//
        .stays();
  }
}
