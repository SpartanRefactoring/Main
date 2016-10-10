package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.leonidas.*;

/** Failing test, originally from {@link TestFactoryTest}.
 * @since 2016 */
@SuppressWarnings("static-method") @FixMethodOrder(MethodSorters.NAME_ASCENDING) @Ignore public class Issue436 {
  @Test public void testRenamingWithQualified() {
    assertEquals("if(a == A) return b(a, B());", TestFactory.shortenIdentifiers("if(omg == Val) return oomph(omg, Dear.foo());"));
  }
}
