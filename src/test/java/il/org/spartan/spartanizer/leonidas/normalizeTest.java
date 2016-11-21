package il.org.spartan.spartanizer.leonidas;

import static il.org.spartan.azzert.*;

import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.research.util.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method") //
public class normalizeTest {
  @Test public void testRenaming() {
    azzert.that(normalize.shortenIdentifiers("if(omg == val) return oomph(omg, dear());"), is("if(a == b) return c(a, d());"));
  }

  @Test public void testRenamingWithCapital() {
    azzert.that(normalize.shortenIdentifiers("if(omg == Val) return oomph(omg, Dear());"), is("if(a == A) return b(a, B());"));
  }

  @Test public void a() {
    azzert.that(normalize.code("a.x.c.d.e()"), is("a.h()"));
  }
}
