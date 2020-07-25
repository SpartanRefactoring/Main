package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.MethodInvocation;
import org.junit.Test;

/** Tests in {@link First}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-20 */
@SuppressWarnings("static-method")
public class LispFirstElementTest {
  @Test public void a() {
    trimmingOf("li.get(0)")//
        .using(new First(), MethodInvocation.class)//
        .gives("first(li)")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("li.get(0);")//
        .using(new First(), MethodInvocation.class)//
        .gives("first(li);")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("omg(li.get(0),li.get(0));")//
        .using(new First(), MethodInvocation.class)//
        .gives("omg(first(li),first(li));")//
        .stays();
  }
}
