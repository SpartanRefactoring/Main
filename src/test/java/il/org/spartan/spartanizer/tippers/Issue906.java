package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

@Ignore
@SuppressWarnings("static-method")
public class Issue906 {
  @Test public void issue075h() {
    trimmingOf("int i; i = +0;").gives("int i = +0;").gives("");
  }
}
