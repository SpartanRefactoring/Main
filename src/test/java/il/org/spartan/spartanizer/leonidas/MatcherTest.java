package il.org.spartan.spartanizer.leonidas;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import static il.org.spartan.spartanizer.research.Matcher.*;
import static il.org.spartan.spartanizer.ast.navigate.wizard.ast;

/** @author Ori Marcovitch
 * @author Dor Ma'ayan
 * @since Dec 3, 2016 */
@SuppressWarnings("static-method")
public class MatcherTest {
  @Test public void a() {
    assert patternMatcher("$X", "").matches(ast("s"));
  }

  @Test public void b() {
    assert patternMatcher("for($N1 $N2 : $X1) $B", "").matches(findFirst.enhancedForStatement(ast("for (A b : C) print();")));
  }

  @Test public void c() {
    assertEquals("print();\n",
        patternMatcher("for($N1 $N2 : $X1) $B", "").getMatching(findFirst.enhancedForStatement(ast("for (A b : C) print();")), "$B") + "");
  }

  @Test public void d() {
    assertEquals("a", patternMatcher("$X + b", "").getMatching(findFirst.expression(ast("a + b")), "$X") + "");
  }

  @Test public void e() {
    assert patternMatcher("if($X1)throw $X2; ", "").matches(findFirst.ifStatement(ast("if(x == null) throw new RuntimeError();")));
  }

  @Test public void f() {
    assert patternMatcher("return $N($A);", "").matches(findFirst.returnStatement(ast("return a();")));
  }

  @Test public void g() {
    assert patternMatcher("return $N($A);", "").matches(findFirst.returnStatement(ast("return a(b);")));
  }

  @Test public void h() {
    assert patternMatcher("return $N($A);", "").matches(findFirst.returnStatement(ast("return a(b,c,d());")));
  }

  @Test public void i() {
    assert patternMatcher("return $N.$N2($A);", "").matches(findFirst.returnStatement(ast("return a.b();")));
  }

  @Test public void j() {
    assert patternMatcher("return $N2.$N($A);", "").matches(findFirst.returnStatement(ast("return a.b(c);")));
  }

  @Test public void k() {
    assert patternMatcher("return $N2.$N($A);", "").matches(findFirst.returnStatement(ast("return g.h.j.a.b(c(h,i,u));")));
  }

  @Test public void l() {
    assert patternMatcher("return ($T)a;", "").matches(findFirst.returnStatement(ast("return (Object)a;")));
  }

  @Test public void m() {
    assert patternMatcher("a += 2", "").matches(findFirst.assignment(ast("a += 2;")));
  }

  @Test public void n() {
    assert patternMatcher("--$N", "").matches(findFirst.prefixExpression(ast("--x")));
  }

  @Test public void o() {
    assert !patternMatcher("--$N", "").matches(findFirst.prefixExpression(ast("¢-=2")));
  }

  @Test public void p() {
    assert patternMatcher("for(int $N = $L1; $N < $L2; ++$N)$B", "").matches(findFirst.forStatement(ast("for(int i = 0; i < 7; ++i) ;")));
  }

  @Test public void q() {
    assert patternMatcher("return $N1().$N($A);", "").matches(findFirst.returnStatement(ast("return a().b();")));
  }

  @Test public void r() {
    assert patternMatcher("return $N1().$N($A);", "").matches(findFirst.returnStatement(ast("return a().b(a,b,c());")));
  }

  @Test public void s() {
    assert !patternMatcher("return $N1().$N($A);", "").matches(findFirst.returnStatement(ast("return a(c).b();")));
  }

  @Test public void t() {
    assert patternMatcher("$N1();", "").matches(findFirst.expressionStatement(ast("a();")));
  }

  @Test public void u() {
    assert !patternMatcher("$N1();", "").matches(findFirst.expressionStatement(ast("a(b);")));
  }

  @Test public void v() {
    assert blockMatcher("for($T $N : $X1) if($X2) return $N;", "").blockMatches(findFirst.block(ast("for(Object i : is) if(i.isNice()) return i;")));
  }

  @Test public void w() {
    assert !patternMatcher("x = 1", "").matches(findFirst.assignment(ast("x += 1")));
  }

  @Test public void x() {
    assert !patternMatcher("x < 7", "").matches(findFirst.infixExpression(ast("x <= 7")));
  }

  @Test public void y() {
    assert patternMatcher("try $B1 catch($T $N) $B2", "").matches(findFirst.tryStatement(ast("try{}catch(What ever){}")));
  }

  @Test public void z() {
    assert patternMatcher("try $B1 catch($T $N) $B2", "").matches(findFirst.tryStatement(ast("try{}catch(What | Ever never ){}")));
  }

  @Test public void a2() {
    assert patternMatcher("x + 7", "").matches(findFirst.infixExpression(ast("x + 7")));
  }

  @Test public void b2() {
    assert !patternMatcher("x + 7", "").matches(findFirst.infixExpression(ast("x - 7")));
  }

  @Test public void c2() {
    assert !patternMatcher("x++", "").matches(findFirst.infixExpression(ast("--x")));
  }

  @Test public void d2() {
    assert !patternMatcher("++x", "").matches(findFirst.infixExpression(ast("++x")));
  }

  @Test public void e2() {
    assert patternMatcher("$X+6+7", "").matches(findFirst.infixExpression(ast("a+6+7")));
  }

  @Test public void f2() {
    assert !patternMatcher("$X+6+null", "").matches(findFirst.infixExpression(ast("a+6+7")));
  }

  @Test public void g2() {
    assert !patternMatcher("try $B1 catch($T $N){int a;}", "").matches(findFirst.tryStatement(ast("try{}catch(What | Ever never ){}")));
  }

  @Test public void h2() {
    assert !patternMatcher("try $B1 catch($T $N){int a;}finally{int a;}", "")
        .matches(findFirst.tryStatement(ast("try{}catch(What | Ever never){int a;}finally{}")));
  }

  @Test public void i2() {
    assert patternMatcher("try $B1 catch($T1 $N1){int a;}catch($T2 $N2){$B}finally{int a;}", "")
        .matches(findFirst.tryStatement(ast("try{}catch(What | Ever never){int a;}catch(Ever never){int a;}finally{int a;}")));
  }

  @Test public void j2() {
    assert !patternMatcher("try $B1 catch($T $N){int a;}finally{int a;}", "")
        .matches(findFirst.tryStatement(ast("try{}catch(What | Ever never){int a;}finally{int t=0;for(int i=0;i<5;i++){t++;}}")));
  }

  @Test public void k2() {
    assert !patternMatcher("try $B1 catch(a b) $B2", "").matches(findFirst.tryStatement(ast("try{}catch(What | Ever never ){}")));
  }

  @Test public void k3() {
    assert patternMatcher("$L", "").matches(findFirst.booleanLiteral(ast("true")));
  }

  @Test public void k4() {
    assert patternMatcher("$N1 = $L", "").matches(findFirst.assignment(ast("x = true")));
  }
}
