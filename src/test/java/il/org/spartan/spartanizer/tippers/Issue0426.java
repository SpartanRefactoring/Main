package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.junit.*;

/** TODO: Alex Kopzon please add a description
 * @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0426 {
  @Test public void a() {
    trimmingOf("for(final Integer i: range.to(10)){System.out.println(i);System.out.println(i);}")
        .gives("for(final Integer ¢: range.to(10)){System.out.println(¢);System.out.println(¢);}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("for(final Integer i: range.to(10)){System.out.println(¢);System.out.println(i);}")//
        .stays();
  }
}
