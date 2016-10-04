package il.org.spartan.spartanizer.leonidas;

import static org.junit.Assert.*;

import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class TestFactoryTest {
  @SuppressWarnings("static-method") @Test public void testRenaming() {
    assertEquals("if(a == b) return c(a, d());", TestFactory.shortenIdentifiers("if(omg == val) return oomph(omg, dear());"));
  }
}
