package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class LispLastIndexTest {
  @Test public void a() {
    trimmingOf("li.size()-1")//
        .withTipper(InfixExpression.class, new LispLastIndex())//
        .gives("lastIndex(li)")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("li.get(li.size()-1);")//
        .withTipper(InfixExpression.class, new LispLastIndex())//
        .gives("li.get(lastIndex(li));")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("li.get(li.size()-1);")//
        .withTipper(InfixExpression.class, new LispLastIndex())//
        .withTipper(MethodInvocation.class, new LispLastElement())//
        .gives("last(li);")//
        .stays();
  }
}
