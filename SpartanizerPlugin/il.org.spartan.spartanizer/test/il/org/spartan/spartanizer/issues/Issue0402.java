package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** A test class regarding a bug with
 * {@link LocalInitializedStatementToForInitializers}. Desired behavior is not
 * to allow inlining if the modifiers are different.
 * @author Dan Greenstein
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0402 {
  @Test public void a() {
    trimmingOf("final L<O> list = new A<>();final int len = Array.getLength(def);for (int ¢ = 0; ¢ <len; ++¢)list.add(Array.get(def, ¢));$.p(list);")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("final L<O> list = new A<>();final int len = o();for (int ¢ = 0; ¢ <len; ++¢)list.add(Array.get(def, ¢));$.p(list);")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("final L<O> list = new A<>();volatile int len = o();for (int ¢ = 0; ¢ <len; ++¢)list.add(Array.get(def, ¢));$.p(list);")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("final L<O> list = new A<>();final int len = o();for (final int ¢ = 0; ¢ <len; ++¢)list.add(Array.get(def, ¢));$.p(list);")
        .gives("final L<O> list = new A<>();for (final int len = o(), ¢ = 0; ¢ <len; ++¢)list.add(Array.get(def, ¢));$.p(list);").stays();
  }

  @Test public void e() {
    trimmingOf("final L<O> list = new A<>();final int len = o();for (int ¢ = 0; ¢ <len; ++¢)list.add(Array.get(def, ¢));$.p(list);")//
        .stays();
  }

  @Test public void f() {
    trimmingOf("final L<O> list = new A<>();int len = o();for (final int ¢ = 0; ¢ <len; ++¢)list.add(Array.get(def, ¢));$.p(list);")//
        .stays();
  }
}
