package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link ThrowOnFalse}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-04 */
@SuppressWarnings("static-method")
public class ThrowOnFalseTest {
  @Test public void a() {
    topDownTrimming("if(x.isCute()) throw new Watever();")//
        .using(IfStatement.class, new ThrowOnFalse())//
        .gives("holds(!(x.isCute())).orThrow(()->new Watever());")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("if(x.isCute() || iWant()) throw new Watever(with(This, and, zis()));")//
        .using(IfStatement.class, new ThrowOnFalse())//
        .gives("holds(!(x.isCute()||iWant())).orThrow(()->new Watever(with(This,and,zis())));")//
        .gives("holds(!x.isCute()&&!iWant()).orThrow(()->new Watever(with(This,and,zis())));")//
        .stays();
  }
}
