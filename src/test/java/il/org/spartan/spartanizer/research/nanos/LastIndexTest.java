package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests in {@link LastIndex}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-20 */
@SuppressWarnings("static-method")
public class LastIndexTest {
  @Test public void a() {
    topDownTrimming("li.size()-1")//
        .using(InfixExpression.class, new LastIndex())//
        .gives("lastIndex(li)")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("li.get(li.size()-1);")//
        .using(InfixExpression.class, new LastIndex())//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("li.get(li.size()-1);")//
        .using(InfixExpression.class, new LastIndex())//
        .using(MethodInvocation.class, new Last())//
        .gives("last(li);")//
        .stays();
  }
}
