package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class LispFirstElementTest {
  @Test public void a() {
    trimmingOf("li.get(0)")//
        .withTipper(MethodInvocation.class, new LispFirstElement())//
        .gives("first(li)")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("li.get(0);")//
        .withTipper(MethodInvocation.class, new LispFirstElement())//
        .gives("first(li);")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("omg(li.get(0),li.get(0));")//
        .withTipper(MethodInvocation.class, new LispFirstElement())//
        .gives("omg(first(li),first(li));")//
        .stays();
  }
}
