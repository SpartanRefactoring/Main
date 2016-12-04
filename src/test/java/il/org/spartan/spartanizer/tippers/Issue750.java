package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import static org.junit.Assert.*;

import org.junit.*;

public class Issue750 {
  @Test public void regularStillWorking() {
    trimmingOf("int foo(int x) { for (Object o : arr) System.out.println(o); }").gives("int foo(int x) { for (Object ¢ : arr) System.out.println(¢); }");
  }

  @Test public void bugFixed() {
    trimmingOf("int foo(int ¢) { for (Object o : arr) System.out.println(o); }").stays();
  }
}
