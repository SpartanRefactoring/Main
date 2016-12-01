package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

public class Issue467 {
  @Ignore
  public static class NotWorking {
    @Test public static void a() {
      trimmingOf("switch(x--){case 1: y=3; case 2: y =4;}").stays();
    }
  }
}
