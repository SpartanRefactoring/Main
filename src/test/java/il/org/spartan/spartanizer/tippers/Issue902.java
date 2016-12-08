package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("static-method")
@Ignore
public class Issue902 {
  @Test public void test1() {
    trimmingOf("void f(){int x; int y;return;}").gives("void f(){int x; int y;}").gives("void f(){}").stays();
  }
}
