package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

@Ignore
@SuppressWarnings("static-method")
public class Issue908 {
  @Test public void issue74d() {
    trimmingOf("int[] a = new int[] {2,3};")//
        .gives("");
  }
}
