package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-20 */
@SuppressWarnings("static-method")
public class LispLastIndexTest {
  @Test public void a() {
    trimmingOf("li.size()-1")//
        .using(InfixExpression.class, new LispLastIndex())//
        .gives("lastIndex(li)")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("li.get(li.size()-1);")//
        .using(InfixExpression.class, new LispLastIndex())//
        .stays();
  }

  @Test public void c() {
    trimmingOf("li.get(li.size()-1);")//
        .using(InfixExpression.class, new LispLastIndex())//
        .using(MethodInvocation.class, new LispLastElement())//
        .gives("last(li);")//
        .stays();
  }
}
