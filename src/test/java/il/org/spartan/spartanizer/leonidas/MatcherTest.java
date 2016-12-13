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
    assertTrue(Matcher.patternMatcher("$X", "").matches(wizard.ast("s")));
  }

  @Test public void b() {
    assertTrue(Matcher.patternMatcher("for($N1 $N2 : $X1) $B", "").matches(findFirst.enhancedForStatement(wizard.ast("for (A b : C) print();"))));
  }

  @Test public void c() {
    assertEquals("print();\n",
        Matcher.patternMatcher("for($N1 $N2 : $X1) $B", "").getMatching(findFirst.enhancedForStatement(wizard.ast("for (A b : C) print();")), "$B")
            + "");
  }

  @Test public void d() {
    assertEquals("a", Matcher.patternMatcher("$X + b", "").getMatching(findFirst.expression(wizard.ast("a + b")), "$X") + "");
  }

  @Test public void e() {
    assertTrue(
        Matcher.patternMatcher("if($X1)throw $X2; ", "").matches(findFirst.ifStatement(wizard.ast("if(x == null) throw new RuntimeError();"))));
  }

  @Test public void f() {
    assertTrue(Matcher.patternMatcher("return $N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a();"))));
  }

  @Test public void g() {
    assertTrue(Matcher.patternMatcher("return $N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a(b);"))));
  }

  @Test public void h() {
    assertTrue(Matcher.patternMatcher("return $N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a(b,c,d());"))));
  }

  @Test public void i() {
    assertTrue(Matcher.patternMatcher("return $N.$N2($A);", "").matches(findFirst.returnStatement(wizard.ast("return a.b();"))));
  }

  @Test public void j() {
    assertTrue(Matcher.patternMatcher("return $N2.$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a.b(c);"))));
  }

  @Test public void k() {
    assertTrue(Matcher.patternMatcher("return $N2.$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return g.h.j.a.b(c(h,i,u));"))));
  }

  @Test public void l() {
    assertTrue(Matcher.patternMatcher("return ($T)a;", "").matches(findFirst.returnStatement(wizard.ast("return (Object)a;"))));
  }

  @Test public void m() {
    assertTrue(Matcher.patternMatcher("a += 2", "").matches(findFirst.assignment(wizard.ast("a += 2;"))));
  }

  @Test public void n() {
    assertTrue(Matcher.patternMatcher("--$N", "").matches(findFirst.prefixExpression(wizard.ast("--x"))));
  }

  @Test public void o() {
    assertFalse(Matcher.patternMatcher("--$N", "").matches(findFirst.prefixExpression(wizard.ast("¢-=2"))));
  }

  @Test public void p() {
    assertTrue(Matcher.patternMatcher("for(int $N = $L1; $N < $L2; ++$N)$B", "")
        .matches(findFirst.forStatement(wizard.ast("for(int i = 0; i < 7; ++i) ;"))));
  }

  @Test public void q() {
    assertTrue(Matcher.patternMatcher("return $N1().$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a().b();"))));
  }

  @Test public void r() {
    assertTrue(Matcher.patternMatcher("return $N1().$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a().b(a,b,c());"))));
  }

  @Test public void s() {
    assertFalse(Matcher.patternMatcher("return $N1().$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a(c).b();"))));
  }

  @Test public void t() {
    assertTrue(Matcher.patternMatcher("$N1();", "").matches(findFirst.expressionStatement(wizard.ast("a();"))));
  }

  @Test public void u() {
    assertFalse(Matcher.patternMatcher("$N1();", "").matches(findFirst.expressionStatement(wizard.ast("a(b);"))));
  }

  @Test public void v() {
    assertTrue(Matcher.blockMatcher("for($T $N : $X1) if($X2) return $N;", "")
        .blockMatches(findFirst.block(wizard.ast("for(Object i : is) if(i.isNice()) return i;"))));
  }

  @Test public void w() {
    assertFalse(Matcher.patternMatcher("x = 1", "").matches(findFirst.assignment(wizard.ast("x += 1"))));
  }

   @Test public void x() {
    assertFalse(Matcher.patternMatcher("x < 7", "").matches(findFirst.infixExpression(wizard.ast("x <= 7"))));
  }

  @Test public void y() {
    assertTrue(Matcher.patternMatcher("try $B1 catch($T $N) $B2", "").matches(findFirst.tryStatement(wizard.ast("try{}catch(What ever){}"))));
  }

   @Test public void z() {
    assertTrue(
        Matcher.patternMatcher("try $B1 catch($T $N) $B2", "").matches(findFirst.tryStatement(wizard.ast("try{}catch(What | Ever never ){}"))));
  }
   
   @Test public void a2() {
     assertTrue(Matcher.patternMatcher("x + 7", "").matches(findFirst.infixExpression(wizard.ast("x + 7"))));
   }
   
   @Test public void b2() {
     assertFalse(Matcher.patternMatcher("x + 7", "").matches(findFirst.infixExpression(wizard.ast("x - 7"))));
   }
   
   @Test public void c2() {
     assertFalse(Matcher.patternMatcher("x++", "").matches(findFirst.infixExpression(wizard.ast("--x"))));
   }
   
   @Test public void d2() {
     assertFalse(Matcher.patternMatcher("++x", "").matches(findFirst.infixExpression(wizard.ast("++x"))));
   }
   
   @Test public void e2() {
     assertTrue(Matcher.patternMatcher("$X+6+7", "").matches(findFirst.infixExpression(wizard.ast("a+6+7"))));
   }
   
   @Test public void f2() {
     assertFalse(Matcher.patternMatcher("$X+6+null", "").matches(findFirst.infixExpression(wizard.ast("a+6+7"))));
   }
   
}
