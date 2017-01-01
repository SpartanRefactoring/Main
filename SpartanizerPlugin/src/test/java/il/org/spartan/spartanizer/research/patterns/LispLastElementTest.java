package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class LispLastElementTest {
  @Test public void a() {
    trimmingOf("li.get(li.size()-1)")//
        .withTipper(MethodInvocation.class, new LispLastElement())//
        .gives("last(li)")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("li.get(li.size()-1);")//
        .withTipper(MethodInvocation.class, new LispLastElement())//
        .gives("last(li);")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("omg(li.get(0), li.get(li.size()-1));")//
        .withTipper(MethodInvocation.class, new LispFirstElement())//
        .withTipper(MethodInvocation.class, new LispLastElement())//
        .gives("omg(first(li),last(li));")//
        .stays();
  }
}
