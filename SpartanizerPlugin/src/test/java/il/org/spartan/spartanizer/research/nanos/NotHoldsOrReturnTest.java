package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.common.*;

/** Tests {@link NotHoldsOrReturn}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-22 */
@SuppressWarnings("static-method")
public class NotHoldsOrReturnTest {
  private static final NanoPatternTipper<IfStatement> nano = new NotHoldsOrReturn();

  @Test public void a() {
    trimmingOf("if(x.isCute()) return;")//
        .using(IfStatement.class, nano)//
        .gives("holds(!(x.isCute())).orReturn();")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("if(x.isCute() || iWant()) return 0;")//
        .using(IfStatement.class, nano)//
        .gives("holds(!(x.isCute()||iWant())).orReturn(0);")//
        .gives("holds(!x.isCute()&&!iWant()).orReturn(0);") //
        .stays();
  }

  @Test public void c() {
    trimmingOf("if(iWant()) return null;")//
        .using(IfStatement.class, nano)//
        .gives("holds(!(iWant())).orReturn(null);") //
        .stays();
  }
}
