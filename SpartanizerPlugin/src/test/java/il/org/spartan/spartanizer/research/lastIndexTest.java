package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class lastIndexTest {
  @Test public void a() {
    trimmingOf("li.size()-1")//
        .withTipper(InfixExpression.class, new LastIndex())//
        .gives("lastIndex(li)")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("li.get(li.size()-1);")//
        .withTipper(InfixExpression.class, new LastIndex())//
        .gives("li.get(lastIndex(li));")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("li.get(li.size()-1);")//
        .withTipper(InfixExpression.class, new LastIndex())//
        .withTipper(MethodInvocation.class, new LastElement())//
        .gives("last(li);")//
        .stays();
  }
}
