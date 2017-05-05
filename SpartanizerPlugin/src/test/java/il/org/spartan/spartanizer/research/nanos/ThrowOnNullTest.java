package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link ThrowOnNull}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-04 */
@SuppressWarnings("static-method")
public class ThrowOnNullTest {
  @Test public void a() {
    trimmingOf("if(x == null) throw new Watever();")//
        .using(new ThrowOnNull(), IfStatement.class)//
        .gives("notNull(x).orThrow(()->new Watever());")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("if(x == null) throw new Watever(with(This, and, zis()));")//
        .using(new ThrowOnNull(), IfStatement.class)//
        .gives("notNull(x).orThrow(()->new Watever(with(This,and,zis())));")//
        .stays();
  }
}
