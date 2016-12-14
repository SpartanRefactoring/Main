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
    assert Matcher.patternMatcher("$X", "").matches(wizard.ast("s"));
  }

  @Test public void b() {
    assert Matcher.patternMatcher("for($N1 $N2 : $X1) $B", "").matches(findFirst.enhancedForStatement(wizard.ast("for (A b : C) print();")));
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
    assert Matcher.patternMatcher("if($X1)throw $X2; ", "").matches(findFirst.ifStatement(wizard.ast("if(x == null) throw new RuntimeError();")));
  }

  @Test public void f() {
    assert Matcher.patternMatcher("return $N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a();")));
  }

  @Test public void g() {
    assert Matcher.patternMatcher("return $N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a(b);")));
  }

  @Test public void h() {
    assert Matcher.patternMatcher("return $N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a(b,c,d());")));
  }

  @Test public void i() {
    assert Matcher.patternMatcher("return $N.$N2($A);", "").matches(findFirst.returnStatement(wizard.ast("return a.b();")));
  }

  @Test public void j() {
    assert Matcher.patternMatcher("return $N2.$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a.b(c);")));
  }

  @Test public void k() {
    assert Matcher.patternMatcher("return $N2.$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return g.h.j.a.b(c(h,i,u));")));
  }

  @Test public void l() {
    assert Matcher.patternMatcher("return ($T)a;", "").matches(findFirst.returnStatement(wizard.ast("return (Object)a;")));
  }

  @Test public void m() {
    assert Matcher.patternMatcher("a += 2", "").matches(findFirst.assignment(wizard.ast("a += 2;")));
  }

  @Test public void n() {
    assert Matcher.patternMatcher("--$N", "").matches(findFirst.prefixExpression(wizard.ast("--x")));
  }

  @Test public void o() {
    assert !Matcher.patternMatcher("--$N", "").matches(findFirst.prefixExpression(wizard.ast("¢-=2")));
  }

  @Test public void p() {
    assert Matcher.patternMatcher("for(int $N = $L1; $N < $L2; ++$N)$B", "")
        .matches(findFirst.forStatement(wizard.ast("for(int i = 0; i < 7; ++i) ;")));
  }

  @Test public void q() {
    assert Matcher.patternMatcher("return $N1().$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a().b();")));
  }

  @Test public void r() {
    assert Matcher.patternMatcher("return $N1().$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a().b(a,b,c());")));
  }

  @Test public void s() {
    assert !Matcher.patternMatcher("return $N1().$N($A);", "").matches(findFirst.returnStatement(wizard.ast("return a(c).b();")));
  }

  @Test public void t() {
    assert Matcher.patternMatcher("$N1();", "").matches(findFirst.expressionStatement(wizard.ast("a();")));
  }

  @Test public void u() {
    assert !Matcher.patternMatcher("$N1();", "").matches(findFirst.expressionStatement(wizard.ast("a(b);")));
  }

  @Test public void v() {
    assert Matcher.blockMatcher("for($T $N : $X1) if($X2) return $N;", "")
        .blockMatches(findFirst.block(wizard.ast("for(Object i : is) if(i.isNice()) return i;")));
  }

  @Test public void w() {
    assert !Matcher.patternMatcher("x = 1", "").matches(findFirst.assignment(wizard.ast("x += 1")));
  }

  @Test public void x() {
    assert !Matcher.patternMatcher("x < 7", "").matches(findFirst.infixExpression(wizard.ast("x <= 7")));
  }

  @Test public void y() {
    assert Matcher.patternMatcher("try $B1 catch($T $N) $B2", "").matches(findFirst.tryStatement(wizard.ast("try{}catch(What ever){}")));
  }

  @Test public void z() {
    assert Matcher.patternMatcher("try $B1 catch($T $N) $B2", "").matches(findFirst.tryStatement(wizard.ast("try{}catch(What | Ever never ){}")));
  }

  @Test public void a2() {
    assert Matcher.patternMatcher("x + 7", "").matches(findFirst.infixExpression(wizard.ast("x + 7")));
  }

  @Test public void b2() {
    assert !Matcher.patternMatcher("x + 7", "").matches(findFirst.infixExpression(wizard.ast("x - 7")));
  }

  @Test public void c2() {
    assert !Matcher.patternMatcher("x++", "").matches(findFirst.infixExpression(wizard.ast("--x")));
  }

  @Test public void d2() {
    assert !Matcher.patternMatcher("++x", "").matches(findFirst.infixExpression(wizard.ast("++x")));
  }

  @Test public void e2() {
    assert Matcher.patternMatcher("$X+6+7", "").matches(findFirst.infixExpression(wizard.ast("a+6+7")));
  }

  @Test public void f2() {
    assert !Matcher.patternMatcher("$X+6+null", "").matches(findFirst.infixExpression(wizard.ast("a+6+7")));
  }
}
