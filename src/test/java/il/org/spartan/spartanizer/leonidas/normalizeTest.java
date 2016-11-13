package il.org.spartan.spartanizer.leonidas;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.research.util.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method") //
public class normalizeTest {
  @Test public void testRenaming() {
    assertEquals("if(a == b) return c(a, d());", normalize.shortenIdentifiers("if(omg == val) return oomph(omg, dear());"));
  }
  @Test public void testRenamingWithCapital() {
    assertEquals("if(a == A) return b(a, B());", normalize.shortenIdentifiers("if(omg == Val) return oomph(omg, Dear());"));
  }
  @Test public void a() {
    assertEquals("a.h()", normalize.codeFragment("a.x.c.d.e()"));
  }
}
