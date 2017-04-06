package il.org.spartan.spartanizer.research.metatester;
import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("static-method")
public class SimpleTest_CustomTest { 

	public @Test void   testF_13() {
	    assertEquals(1, 2);
	}

	public @Test void   testF_15() {
	    assertEquals(1, 1);
	}

	public @Test void   testF_17() {
	    assert true;
	}
}