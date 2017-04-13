package il.org.spartan.spartanizer.research.metatester;
import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runner.*;
@Ignore
@SuppressWarnings("static-method")
public class SimpleTest_CustomTest { 

	public @Test void   testF_14() {
	    assertEquals(1, 2);
	}

	public @Test void   testF_16() {
	    assertEquals(1, 1);
	}

	public @Test void   testF_18() {
	    assert true;
	}
}