package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO orimarco {@code marcovitch.ori@gmail.com} please add a description
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-04 */
@SuppressWarnings("static-method")
public class NotNullOrThrowTest {
  @Test public void a() {
    topDownTrimming("if(x == null) throw new Watever();")//
        .using(IfStatement.class, new NotNullOrThrow())//
        .gives("notNull(x).orThrow(()->new Watever());")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("if(x == null) throw new Watever(with(This, and, zis()));")//
        .using(IfStatement.class, new NotNullOrThrow())//
        .gives("notNull(x).orThrow(()->new Watever(with(This,and,zis())));")//
        .stays();
  }
}
