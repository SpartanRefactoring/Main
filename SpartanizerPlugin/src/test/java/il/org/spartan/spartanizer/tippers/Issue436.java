package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.leonidas.*;
import il.org.spartan.spartanizer.research.*;

/** Failing test, originally from {@link NormalizeTest}.
 * @since 2016 */
@SuppressWarnings("static-method") @FixMethodOrder(MethodSorters.NAME_ASCENDING) @Ignore public class Issue436 {
  @Test public void testRenamingWithQualified() {
    assertEquals("if(a == A) return b(a, B());", normalize.shortenIdentifiers("if(omg == Val) return oomph(omg, Dear.foo());"));
  }
}
