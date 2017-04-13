package il.org.spartan.spartanizer.research.metatester;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runner.*;

@SuppressWarnings("static-method")
public class SimpleTest_CustomTest { 

	@Test public void testF_13() {
    assertEquals(1, 1);
  }

	@Test public void testF_15() {
    assertEquals(1, 1);
  }

	@Test public void testF_17() {
    assert true;
  }

	@Test public void testG_21() {
    assert true;
  }
}