package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests in {@link Last}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-20 */
@SuppressWarnings("static-method")
public class LispLastElementTest {
  @Test public void a() {
    trimmingOf("li.get(li.size()-1)")//
        .using(MethodInvocation.class, new Last())//
        .gives("last(li)")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("li.get(li.size()-1);")//
        .using(MethodInvocation.class, new Last())//
        .gives("last(li);")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("omg(li.get(0), li.get(li.size()-1));")//
        .using(MethodInvocation.class, new First())//
        .using(MethodInvocation.class, new Last())//
        .gives("omg(first(li),last(li));")//
        .stays();
  }
}
