package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

// Whos is the author of this issue? #750
@SuppressWarnings("static-method")
@Ignore
public class Issue750 {
  @Test public void regularStillWorking() {
    trimmingOf("int foo(int x){for (Object o :a)System.out.println(o); }")//
        .gives("int foo(int x){for (Object ¢ :a)System.out.println(¢); }");
  }

  @Test public void bugFixed() {
    trimmingOf("int foo(int ¢) { for (Object o : arr) System.out.println(o); }").stays();
  }
}
