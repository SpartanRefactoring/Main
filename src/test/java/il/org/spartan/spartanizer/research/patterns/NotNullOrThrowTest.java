package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-04 */
@SuppressWarnings("static-method")
public class NotNullOrThrowTest {
  @Test public void a() {
    trimmingOf("if(x == null) throw new Watever();")//
        .withTipper(IfStatement.class, new NotNullOrThrow())//
        .gives("notNull(x).orThrow(()->new Watever());")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("if(x == null) throw new Watever(with(This, and, zis()));")//
        .withTipper(IfStatement.class, new NotNullOrThrow())//
        .gives("notNull(x).orThrow(()->new Watever(with(This,and,zis())));")//
        .stays();
  }
}
