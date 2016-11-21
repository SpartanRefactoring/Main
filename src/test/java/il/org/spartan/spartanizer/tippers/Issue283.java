package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method")
public class Issue283 {

  @Test public void test() {
    trimmingOf("@SuppressWarnings(\"unused\") " //
        + "@Deprecated " //
        + "@Override" //
        + " void myMethod() { }") //
    .gives("@Deprecated "
        + "@Override "
        + "@SuppressWarnings(\"unused\") " //
        + " void myMethod() { }")//
    .stays();
  }
}
