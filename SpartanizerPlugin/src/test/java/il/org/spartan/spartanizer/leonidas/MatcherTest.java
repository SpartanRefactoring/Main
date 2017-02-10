package il.org.spartan.spartanizer.leonidas;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.research.Matcher.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** TODO: Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @author Dor Ma'ayan
 * @since Dec 3, 2016 */
@SuppressWarnings("static-method")
public class MatcherTest {
  @Test public void a() {
    assert patternMatcher("$X", "").matches(ast("s"));
  }

  @Test public void a2() {
    assert patternMatcher("x + 7", "").matches(findFirst.infixExpression(ast("x + 7")));
  }

  @Test public void b() {
    assert patternMatcher("for($N1 $N2 : $X1) $B", "").matches(findFirst.enhancedForStatement(ast("for (A b : C) print();")));
  }

  @Test public void b2() {
    assert !patternMatcher("x + 7", "").matches(findFirst.infixExpression(ast("x - 7")));
  }

  @Test public void c() {
    azzert.that(patternMatcher("for($N1 $N2 : $X1) $B", "").getMatching(findFirst.enhancedForStatement(ast("for (A b : C) print();")), "$B") + "",
        is("print();\n"));
  }

  @Test public void c2() {
    assert !patternMatcher("x++", "").matches(findFirst.infixExpression(ast("--x")));
  }

  @Test public void d() {
    azzert.that(patternMatcher("$X + b", "").getMatching(findFirst.expression(ast("a + b")), "$X") + "", is("a"));
  }

  @Test public void d2() {
    assert !patternMatcher("++x", "").matches(findFirst.infixExpression(ast("++x")));
  }

  @Test public void d3() {
    assert patternMatcher("$D", "").matches(findFirst.expression(ast("null")));
  }

  @Test public void d4() {
    assert patternMatcher("$D", "").matches(findFirst.expression(ast("0")));
  }

  @Test public void d5() {
    assert patternMatcher("$D", "").matches(findFirst.expression(ast("false")));
  }

  @Test public void e() {
    assert patternMatcher("if($X1)throw $X2; ", "").matches(findFirst.ifStatement(ast("if(x == null) throw new RuntimeError();")));
  }

  @Test public void e2() {
    assert patternMatcher("$X+6+7", "").matches(findFirst.infixExpression(ast("a+6+7")));
  }

  @Test public void f() {
    assert patternMatcher("return $N($A);", "").matches(findFirst.instanceOf(ReturnStatement.class).in(ast("return a();")));
  }

  @Test public void f2() {
    assert !patternMatcher("$X+6+null", "").matches(findFirst.infixExpression(ast("a+6+7")));
  }

  @Test public void g() {
    assert patternMatcher("return $N($A);", "").matches(findFirst.instanceOf(ReturnStatement.class).in(ast("return a(b);")));
  }

  @Test public void g2() {
    assert !patternMatcher("try $B1 catch($T $N){int a;}", "").matches(findFirst.tryStatement(ast("try{}catch(What | Ever never ){}")));
  }

  @Test public void h() {
    assert patternMatcher("return $N($A);", "").matches(findFirst.instanceOf(ReturnStatement.class).in(ast("return a(b,c,d());")));
  }

  @Test public void h2() {
    assert !patternMatcher("try $B1 catch($T $N){int a;}finally{int a;}", "")
        .matches(findFirst.tryStatement(ast("try{}catch(What | Ever never){int a;}finally{}")));
  }

  @Test public void i() {
    assert patternMatcher("return $N.$N2($A);", "").matches(findFirst.instanceOf(ReturnStatement.class).in(ast("return a.b();")));
  }

  @Test public void i2() {
    assert patternMatcher("try $B1 catch($T1 $N1){int a;}catch($T2 $N2){$B}finally{int a;}", "")
        .matches(findFirst.tryStatement(ast("try{}catch(What | Ever never){int a;}catch(Ever never){int a;}finally{int a;}")));
  }

  @Test public void j() {
    assert patternMatcher("return $N2.$N($A);", "").matches(findFirst.instanceOf(ReturnStatement.class).in(ast("return a.b(c);")));
  }

  @Test public void j2() {
    assert !patternMatcher("try $B1 catch($T $N){int a;}finally{int a;}", "")
        .matches(findFirst.tryStatement(ast("try{}catch(What | Ever never){int a;}finally{int t=0;for(int i=0;i<5;i++){t++;}}")));
  }

  @Test public void k() {
    assert patternMatcher("return $N2.$N($A);", "").matches(findFirst.instanceOf(ReturnStatement.class).in(ast("return g.h.j.a.b(c(h,i,u));")));
  }

  @Test public void k10() {
    assert patternMatcher("$N.$SN", "").matches(findFirst.expression(ast("x.y.z")));
  }

  @Test public void k11() {
    assert patternMatcher("$N.$SN()", "").matches(findFirst.expression(ast("x.y.z()")));
  }

  @Test public void k12() {
    assert patternMatcher("$SN == null ? null : $SN.$SN2.$SN3()", "").matches(findFirst.conditionalExpression(ast("x == null ? null : x.y.z()")));
  }

  @Test public void k13() {
    assert patternMatcher("$SN == $D1 ? $D2 : $SN.$SN2.$SN3()", "").matches(findFirst.conditionalExpression(ast("x == null ? null : x.y.z()")));
  }

  @Test public void k14() {
    assert patternMatcher("$SN == $D1 ? $D2 : $SN.$SN2.$SN3()", "").matches(findFirst.conditionalExpression(ast("x == false ? 0 : x.y.z()")));
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

  @Test public void k5() {
    assert patternMatcher("$N1.$N2", "").matches(findFirst.name(ast("x.y.z")));
  }

  @Test public void k6() {
    assert patternMatcher("$SN1", "").matches(findFirst.expression(ast("x")));
  }

  @Test public void k7() {
    assert patternMatcher("$SN1 + $SN2", "").matches(findFirst.expression(ast("x + y")));
  }

  @Test public void k8() {
    assert patternMatcher("$SN + $SN", "").matches(findFirst.expression(ast("x + x")));
  }

  @Test public void k9() {
    assert patternMatcher("$SN.$SN2", "").matches(findFirst.expression(ast("x.y")));
  }

  @Test public void l() {
    assert patternMatcher("return ($T)a;", "").matches(findFirst.instanceOf(ReturnStatement.class).in(ast("return (Object)a;")));
  }

  @Test public void m() {
    assert patternMatcher("a += 2", "").matches(findFirst.assignment(ast("a += 2;")));
  }

  @Test public void n() {
    assert patternMatcher("--$N", "").matches(findFirst.instanceOf(PrefixExpression.class).in(ast("--x")));
  }

  @Test public void o() {
    assert !patternMatcher("--$N", "").matches(findFirst.instanceOf(PrefixExpression.class).in(ast("¢-=2")));
  }

  @Test public void p() {
    assert patternMatcher("for(int $N = $L1; $N < $L2; ++$N)$B", "").matches(findFirst.forStatement(ast("for(int i = 0; i < 7; ++i) ;")));
  }

  @Test public void p01() {
    assert !patternMatcher("x", "").matches(findFirst.expression(ast("(x)")));
  }

  @Test public void p02() {
    assert patternMatcher("(x)", "").matches(findFirst.expression(ast("(x)")));
  }

  @Test public void p03() {
    assert !patternMatcher("$N", "").matches(findFirst.expression(ast("(x)")));
  }

  @Test public void p04() {
    assert !patternMatcher("$SN", "").matches(findFirst.expression(ast("(x)")));
  }

  @Test public void p05() {
    assert patternMatcher("$X", "").matches(findFirst.expression(ast("(x)")));
  }

  @Test public void q() {
    assert patternMatcher("return $N1().$N($A);", "").matches(findFirst.instanceOf(ReturnStatement.class).in(ast("return a().b();")));
  }

  @Test public void r() {
    assert patternMatcher("return $N1().$N($A);", "").matches(findFirst.instanceOf(ReturnStatement.class).in(ast("return a().b(a,b,c());")));
  }

  @Test public void s() {
    assert !patternMatcher("return $N1().$N($A);", "").matches(findFirst.instanceOf(ReturnStatement.class).in(ast("return a(c).b();")));
  }

  @Test public void t() {
    assert patternMatcher("$N1();", "").matches(findFirst.expressionStatement(ast("a();")));
  }

  @Test public void t01() {
    assert patternMatcher("$T $N;", "").matches(findFirst.variableDeclarationStatement(ast("Object x;")));
  }

  @Test public void t02() {
    assert patternMatcher("$T $N;", "").matches(findFirst.variableDeclarationStatement(ast("int x;")));
  }

  @Test public void t03() {
    assert patternMatcher("$T $N;", "").matches(findFirst.variableDeclarationStatement(ast("Class<?> x;")));
  }

  @Test public void t04() {
    assert patternMatcher("$T $N;", "").matches(findFirst.variableDeclarationStatement(ast("Class<? extends N> x;")));
  }

  @Test public void t05() {
    assert !patternMatcher("$T y;", "").matches(findFirst.variableDeclarationStatement(ast("Class<? extends N> x;")));
  }

  @Test public void t06() {
    assert !patternMatcher("$T x;", "").matches(findFirst.variableDeclarationStatement(ast("Object y;")));
  }

  @Test public void t07() {
    assert patternMatcher("for($T $N : $X) f();", "").matches(findFirst.enhancedForStatement(ast("for(C i : col) f();")));
  }

  @Test public void t08() {
    assert patternMatcher("for($T $N : $X) f();", "").matches(findFirst.enhancedForStatement(ast("for(C<?> i : col) f();")));
  }

  @Test public void t09() {
    assert patternMatcher("for($T $N : $X) f();", "").matches(findFirst.enhancedForStatement(ast("for(C<? extends N> i : col) f();")));
  }

  @Test public void t10() {
    assert patternMatcher("for($T $N : $X1) $X2;", "").matches(findFirst.enhancedForStatement(ast("for(C<? extends N> i : col) f();")));
  }

  @Test public void t10b() {
    assert patternMatcher("for($T $N1 : $N2) $X;", "").matches(findFirst.enhancedForStatement(ast("for(C<? extends N> i : col) f();")));
  }

  @Test public void t11() {
    assert patternMatcher("for($T $N2 : $N3) $X;", "").matches(findFirst.enhancedForStatement(ast("for (Class ¢ : bf) f();")));
  }

  @Test public void t12() {
    assert patternMatcher("for($T $N2 : $N3) $X;", "").matches(findFirst.enhancedForStatement(ast("for (Class<?> ¢ : bf) f();")));
  }

  @Test public void t13() {
    assert patternMatcher("for($T $N2 : $N3) $X;", "")
        .matches(findFirst.enhancedForStatement(ast("for (Class<? extends BroadcastFilter> ¢ : bf) f();")));
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

  @Test public void x01() {
    assert !patternMatcher("$X;", "").matches(findFirst.expression(ast("return 6;")));
  }

  @Test public void y() {
    assert patternMatcher("try $B1 catch($T $N) $B2", "").matches(findFirst.tryStatement(ast("try{}catch(What ever){}")));
  }

  @Test public void z() {
    assert patternMatcher("try $B1 catch($T $N) $B2", "").matches(findFirst.tryStatement(ast("try{}catch(What | Ever never ){}")));
  }
}
