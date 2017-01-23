package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO:  Ori Marcovitch
 please add a description 
 @author Ori Marcovitch
 * @since 2016 
 */

@SuppressWarnings("static-method")
public class AsBitTest {
  @Test public void a() {
    trimmingOf("(k == 0) ? 1 : 0")//
        .using(ConditionalExpression.class, new AsBit())//
        .gives("as.bit((k == 0))") //
        .gives("as.bit(k == 0)") //
        .stays();
  }

  @Test public void b() {
    trimmingOf("k == 0 ? 1 : 0")//
        .using(ConditionalExpression.class, new AsBit())//
        .gives("as.bit(k == 0)")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("k? 1 : 0")//
        .using(ConditionalExpression.class, new AsBit())//
        .gives("as.bit(k)")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("(0 == x(f,g,h.h(a,b,moo))) ? 1 : 0")//
        .using(ConditionalExpression.class, new AsBit())//
        .gives("as.bit((0 == x(f,g,h.h(a,b,moo))))")//
        .gives("as.bit(0==x(f,g,h.h(a,b,moo)))")//
        .gives("as.bit(x(f,g,h.h(a,b,moo)) == 0)")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("k == 0 ? 0 : 1")//
        .using(ConditionalExpression.class, new AsBit())//
        .gives("as.bit(!(k==0))")//
        .gives("as.bit(k != 0 )")//
        .stays();
  }
}

