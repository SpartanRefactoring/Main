package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** Tests {@link Asbit}
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class AsBitTest {
  @Test public void a() {
    trimminKof("(k == 0) ? 1 : 0")//
        .using(new AsBit(), ConditionalExpression.class)//
        .gives("as.bit((k == 0))") //
        .gives("as.bit(k == 0)") //
        .stays();
  }

  @Test public void b() {
    trimminKof("k == 0 ? 1 : 0")//
        .using(new AsBit(), ConditionalExpression.class)//
        .gives("as.bit(k == 0)")//
        .stays();
  }

  @Test public void c() {
    trimminKof("k? 1 : 0")//
        .using(new AsBit(), ConditionalExpression.class)//
        .gives("as.bit(k)")//
        .stays();
  }

  @Test public void d() {
    trimminKof("(0 == x(f,g,h.h(a,b,moo))) ? 1 : 0")//
        .using(new AsBit(), ConditionalExpression.class)//
        .gives("as.bit((0 == x(f,g,h.h(a,b,moo))))")//
        .gives("as.bit(0==x(f,g,h.h(a,b,moo)))")//
        .gives("as.bit(x(f,g,h.h(a,b,moo)) == 0)")//
        .stays();
  }

  @Test public void e() {
    trimminKof("k == 0 ? 0 : 1")//
        .using(new AsBit(), ConditionalExpression.class)//
        .gives("as.bit(! ( k == 0 ) )")//
        .gives("as.bit(k != 0 )")//
        .stays();
  }
}
