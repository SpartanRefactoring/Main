package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** TODO Alex Kopzon please add a description
 * @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings("static-method")
public class Issue0378 {
  @Test public void a() {
    trimmingOf("void func(int i) {return something_else;}")//
        .gives("void func(int __) {return something_else;}")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("void func(int notJD) {return something_else;}")//
        .stays();
  }
}
