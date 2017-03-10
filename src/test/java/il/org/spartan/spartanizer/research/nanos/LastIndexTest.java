package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests in {@link LastIndex}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-20 */
@SuppressWarnings("static-method")
public class LastIndexTest {
  @Test public void a() {
    trimmingOf("li.size()-1")//
        .using(InfixExpression.class, new LastIndex())//
        .gives("lastIndex(li)")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("li.get(li.size()-1);")//
        .using(InfixExpression.class, new LastIndex())//
        .stays();
  }

  @Test public void c() {
    trimmingOf("li.get(li.size()-1);")//
        .using(InfixExpression.class, new LastIndex())//
        .using(MethodInvocation.class, new Last())//
        .gives("last(li);")//
        .stays();
  }
}
