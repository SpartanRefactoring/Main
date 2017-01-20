package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method")
public class Issue1098 {
  @Test public void t1() {
    trimmingOf("int a() {"
        + "switch (¢ + \"\") {"
        + "case \"Object\":"
        + "case \"java.lang.Object\":"
        + "return true;"
        + "default:"
        + "return false;"
        + "}"
        + "}").stays();
  }
}
