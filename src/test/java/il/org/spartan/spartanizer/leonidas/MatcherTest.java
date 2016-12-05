package il.org.spartan.spartanizer.leonidas;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since Dec 3, 2016 */
@SuppressWarnings("static-method")
public class MatcherTest {
  @Test public void a() {
    assertTrue(new Matcher("$X", "").matches((wizard.ast("s"))));
  }

  @Test public void b() {
    assertTrue(new Matcher("for($N1 $N2 : $X1) $B", "").matches(findFirst.enhancedForStatement(wizard.ast("for (A b : C) print();"))));
  }

  @Test public void c() {
    assertEquals("print();\n",
        (new Matcher("for($N1 $N2 : $X1) $B", "")).getMatching(findFirst.enhancedForStatement(wizard.ast("for (A b : C) print();")), "$B") + "");
  }

  @Test public void d() {
    assertEquals("a", (new Matcher("$X + b", "")).getMatching(findFirst.expression(wizard.ast("a + b")), "$X") + "");
  }

  @Test public void e() {
    assertTrue(new Matcher("if($X1)throw $X2; ", "").matches(findFirst.ifStatement(wizard.ast("if(x == null) throw new RuntimeError();"))));
  }

  @Test public void f() {
    assertTrue(new Matcher("return $N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a();"))));
  }

  @Test public void g() {
    assertTrue(new Matcher("return $N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a(b);"))));
  }

  @Test public void h() {
    assertTrue(new Matcher("return $N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a(b,c,d());"))));
  }

  @Test public void i() {
    assertTrue(new Matcher("return $N.$N2($A);", "").matches(findFirst.returnStatement(wizard.ast("return a.b();"))));
  }

  @Test public void j() {
    assertTrue(new Matcher("return $N2.$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a.b(c);"))));
  }

  @Test public void k() {
    assertTrue(new Matcher("return $N2.$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return g.h.j.a.b(c(h,i,u));"))));
  }

  @Test public void l() {
    assertTrue(new Matcher("return ($T)a;", "").matches(findFirst.returnStatement(wizard.ast("return (Object)a;"))));
  }
}
