package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0426 {
  @Test public void a() {
    topDownTrimming("for(final Integer i: range.to(10)){System.out.println(i);System.out.println(i);}")
        .gives("for(final Integer ¢: range.to(10)){System.out.println(¢);System.out.println(¢);}")//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("for(final Integer i: range.to(10)){System.out.println(¢);System.out.println(i);}")//
        .stays();
  }
}
