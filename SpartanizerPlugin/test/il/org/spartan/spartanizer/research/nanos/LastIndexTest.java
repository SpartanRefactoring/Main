package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests in {@link LastIndex}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-20 */
@SuppressWarnings("static-method")
public class LastIndexTest {
  @Test public void a() {
    trimmingOf("li.size()-1")//
        .using(new LastIndex(), InfixExpression.class)//
        .gives("lastIndex(li)")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("li.get(li.size()-1);")//
        .using(new LastIndex(), InfixExpression.class)//
        .stays();
  }

  @Test public void c() {
    trimmingOf("li.get(li.size()-1);")//
        .using(new LastIndex(), InfixExpression.class)//
        .using(new Last(), MethodInvocation.class)//
        .gives("last(li);")//
        .stays();
  }
}
