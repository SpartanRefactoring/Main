package il.org.spartan.spartanizer.ast.navigate;

import org.junit.*;

import il.org.spartan.spartanizer.ast.safety.*;

@SuppressWarnings("static-method")
public class safeTest {
  @Test public void a() {
    assert safe.div(1, 2) == 0.5;
  }
}
