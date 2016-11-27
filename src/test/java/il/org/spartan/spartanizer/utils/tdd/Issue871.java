package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("static-method")
public class Issue871 {
  @Test public void test0() {
    assertEquals(0, (new ParameterInt(0)).intValue());
  }
}
