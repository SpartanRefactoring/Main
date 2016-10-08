package il.org.spartan.spartanizer.leonidas;

import static org.junit.Assert.*;

import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")//
public class TestFactoryTest {
  Object o = TipperFactory.tipper("", "", "");

  @Test public void testRenaming() {
    assertEquals("if(a == b) return c(a, d());", TestFactory.shortenIdentifiers("if(omg == val) return oomph(omg, dear());"));
  }

  @Test public void testRenamingWithCapital() {
    assertEquals("if(a == A) return b(a, B());", TestFactory.shortenIdentifiers("if(omg == Val) return oomph(omg, Dear());"));
  }

  @Ignore @Test public void testRenamingWithQualified() {
    assertEquals("if(a == A) return b(a, B());", TestFactory.shortenIdentifiers("if(omg == Val) return oomph(omg, Dear.foo());"));
  }
}
