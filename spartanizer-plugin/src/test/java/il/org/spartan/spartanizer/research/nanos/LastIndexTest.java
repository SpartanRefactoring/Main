package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.junit.Test;

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
