package il.org.spartan.spartanizer.research.metatester;
import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("static-method")
public class SimpleTest_CustomTest { 

	@Test public void testF_14() {
    assertEquals(1, 1);
  }

	@Test public void testF_15() {
    assertEquals(1, 2);
  }

	@Test public void testF_17() {
    assert true;
  }
}