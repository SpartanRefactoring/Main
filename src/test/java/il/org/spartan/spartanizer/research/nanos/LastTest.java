package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests in {@link Last}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-20 */
@SuppressWarnings("static-method")
public class LastTest {
  @Test public void a() {
    topDownTrimming("li.get(li.size()-1)")//
        .using(MethodInvocation.class, new Last())//
        .gives("last(li)")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("li.get(li.size()-1);")//
        .using(MethodInvocation.class, new Last())//
        .gives("last(li);")//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("omg(li.get(0), li.get(li.size()-1));")//
        .using(MethodInvocation.class, new First())//
        .using(MethodInvocation.class, new Last())//
        .gives("omg(first(li),last(li));")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("li.m().get(li.m().size()-1);")//
        .using(MethodInvocation.class, new Last())//
        .gives("last(li.m());")//
        .stays();
  }
}
