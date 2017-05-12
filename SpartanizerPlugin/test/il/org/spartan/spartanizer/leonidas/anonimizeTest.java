package il.org.spartan.spartanizer.leonidas;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method") //
public class anonimizeTest {
  @Test public void a() {
    azzert.that(JUnitTestMethodFacotry.code("a.x.c.d.e()"), is("a.h()"));
  }

  @Test public void testRenaming() {
    azzert.that(JUnitTestMethodFacotry.shortenIdentifiers("if(omg == val) return oomph(omg, dear());"), is("if(a == b) return c(a, d());"));
  }

  @Test public void testRenamingWithCapital() {
    azzert.that(JUnitTestMethodFacotry.shortenIdentifiers("if(omg == Val) return oomph(omg, Dear());"), is("if(a == A) return b(a, B());"));
  }
}
