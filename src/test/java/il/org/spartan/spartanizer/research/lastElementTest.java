package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class lastElementTest {
  @Test public void a() {
    trimmingOf("li.get(li.size()-1)")//
        .withTipper(MethodInvocation.class, new LastElement())//
        .gives("last(li)")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("li.get(li.size()-1);")//
        .withTipper(MethodInvocation.class, new LastElement())//
        .gives("last(li);")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("omg(li.get(0), li.get(li.size()-1));")//
        .withTipper(MethodInvocation.class, new FirstElement())//
        .withTipper(MethodInvocation.class, new LastElement())//
        .gives("omg(first(li),last(li));")//
        .stays();
  }
}
