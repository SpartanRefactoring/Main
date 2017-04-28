package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.testing.TestUtilsAll.*;
import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.utils.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class FixedPointTest {
  private static void assertConvertsTo(final String from, final String expected) {
    assertWrappedTranslation(from, expected, WrapIntoComilationUnit.Statement);
  }

  private static void assertSimplifiesTo(final String from, final String expected) {
    assertWrappedTranslation(from, expected, WrapIntoComilationUnit.Expression);
  }

  private static void assertWrappedTranslation(final String from, final String expected, final WrapIntoComilationUnit u) {
    final String wrap = u.on(from);
    azzert.that(u.off(wrap), is(from));
    final String unpeeled = new InteractiveSpartanizer().fixedPoint(wrap);
    if (wrap.equals(unpeeled))
      fail("Nothing done on " + from);
    final String peeled = u.off(unpeeled);
    if (peeled.equals(from))
      azzert.assertNotEquals("No similification of " + from, from, peeled);
    if (tide.clean(peeled).equals(tide.clean(from)))
      azzert.assertNotEquals("Simpification of " + from + "is just reformatting", tide.clean(peeled), tide.clean(from));
    assertSimilar(expected, peeled);
  }

  @Test(timeout = 2000) public void chainComparison() {
    assertSimplifiesTo("a == true == b == c", "a == b == c");
  }

  @Test public void commonPrefixIfBranchesInBlock() {
    assertConvertsTo("{ if (a) { f(); g(); ++i; } else { f(); g();\n --i; }}", " f(); g(); if (a)  ++i; else  --i;");
  }

  @Test(timeout = 2000) public void desiredSimplificationOfExample() {
    assertSimplifiesTo("on * notion * of * no * nothion <the * plain + kludge", "no*of*on*notion*nothion<kludge+the*plain");
  }

  @Test(timeout = 2000) public void eliminateRedundantIf1() {
    assertConvertsTo("{if (a) ; }", "");
  }

  @Test(timeout = 2000) public void eliminateRedundantIf2() {
    assertConvertsTo("{if (a) ; else {;}}", "");
  }

  @Test(timeout = 2000) public void eliminateRedundantIf3() {
    assertConvertsTo("{if (a) {;} else {;;}}", "");
  }

  @Test(timeout = 2000) public void eliminateRedundantIf4() {
    assertConvertsTo("{if (a) {;}} ", "");
  }

  @Test public void hasNullsTest() {
    assert hasNull((Object) null);
    assert !hasNull(new Object());
    assert hasNull(new Object(), null);
    assert !hasNull(new Object(), new Object());
    assert !hasNull(new Object(), new Object());
    assert !hasNull(new Object(), new Object(), new Object());
    assert !hasNull(new Object(), new Object(), new Object(), new Object());
    assert hasNull(null, new Object(), new Object(), new Object(), new Object());
    assert hasNull(new Object(), new Object(), null, new Object(), new Object());
    assert hasNull(new Object(), new Object(), new Object(), null, new Object());
    assert hasNull(new Object(), new Object(), new Object(), new Object(), null);
  }

  @Test(timeout = 4000) public void inlineInitializers() {
    assertConvertsTo("int b,a = 2; return 3 * a * b; ", "return 6 * b;");
  }

  @Test(timeout = 2000) public void issue37() {
    assertConvertsTo(" int result = mockedType.hashCode(); result = 31 * result + types.hashCode(); return result;\n",
        "return 31*mockedType.hashCode()+types.hashCode();");
  }

  @Test(timeout = 2000) public void issue37abbreviated() {
    assertConvertsTo(" int a = 3; a = 31 * a; return a;\n", "return 93;");
  }

  // Spartanizer problem
  @Ignore @Test public void issue43() {
    assertConvertsTo("String tipper = Z2;  tipper = tipper.f(A).f(b) + tipper.f(c); return (tipper + 3); ", "return(Z2.f(A).f(b)+Z2.f(c)+3);");
  }

  @Test public void shortestIfBranchFirst02() {
    trimmingOf(
        "void foo() {if (!s.equals(0xDEAD)) {int $=0; for (int i=0;i<s.length();++i) if (s.charAt(i)=='a') $ += 2; else if (s.charAt(i)=='d') $ -= 1; return $;} else {return 8;}}")
            .gives(
                "void foo() {if (s.equals(0xDEAD)) return 8; int $ = 0; for (int i = 0;i <s.length();++i) if (s.charAt(i) == 'a') $ += 2; else if (s.charAt(i) == 'd')$-=1; return $;}")
            .gives(
                "void foo() {if (s.equals(0xDEAD)) return 8; int $ = 0; for (int ¢ = 0;¢ <s.length();++¢) if (s.charAt(¢) == 'a') $ += 2; else if (s.charAt(¢) == 'd')$-=1; return $;}")
            .gives(
                "void foo() {if (s.equals(0xDEAD)) return 8; int $ = 0; for (int ¢ = 0;¢ <s.length();++¢) if (s.charAt(¢) == 'a') $ += 2; else if (s.charAt(¢) == 'd') --$; return $;}");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst03a() {
    assertConvertsTo(" if ('a' == s.charAt(i)) $ += 2; else if ('d' == s.charAt(i)) $ -= 1;\n",
        " if (s.charAt(i) == 'a') $ += 2; else if (s.charAt(i) == 'd') --$;\n");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst09() {
    assertSimplifiesTo("s.equals(532) ? 9 * yada3(s.length()) : 6 ", "!s.equals(532)?6:9*yada3(s.length())");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst11() {
    assertSimplifiesTo("b != null && b.getNodeType() == ASTNode.BLOCK ? getBlockSingleStmnt((Block) b) : b ",
        "b==null||b.getNodeType()!=ASTNode.BLOCK?b:getBlockSingleStmnt((Block)b)");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst12() {
    assertConvertsTo("if (FF() && TT()){ foo1();foo2();}else shorterFoo();", " if (!FF() || !TT()) shorterFoo();else {foo1();foo2();}");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst13() {
    assertConvertsTo(" int a = 0; if (a> 0) return 6; else { int b = 9; b *= b;\n return b; } ;", "return 0>0?6:81;");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst14() {
    assertConvertsTo(" int a = 0; if (a> 0) { int b = 9; b *= b; return 6; } else {\n int a = 5; return b; }", "return 0>0?6:b;");
  }

  @Test(timeout = 2000) public void shortestOperand03() {
    assertConvertsTo("k = k * 4;if (1 + 2 - 3 - 4 + 5 / 6 - 7 + 8 * 9> 4+k) return true;", "k*=4;if(k<57)return true;");
  }

  @Test(timeout = 2000) public void shortestOperand04() {
    assertConvertsTo("return (1 + 2 <3 & 7 + 4> 2 + 1 || 6 - 7 <2 + 1);", "return(3<3&11>3||-1<3);");
  }

  @Test(timeout = 2000) public void shortestOperand07() {
    assertConvertsTo("int y,o,g,i,s;return ( y + o + s> s + i | g> 42);", "int y,o,g,i,s;return(g>42|o+s+y>i+s);");
  }

  @Test(timeout = 2000) public void shortestOperand08() {
    assertConvertsTo("if (bob.father.age> 42 && bob.mother.father.age> bob.age ) return true; else return false;",
        "return bob.father.age>42&&bob.mother.father.age>bob.age;");
  }

  @Test(timeout = 2000) public void shortestOperand26() {
    assertConvertsTo("return f(a,b,c,d) | f() | 0;} ", "return f()|f(a,b,c,d)|0;}");
  }

  @Test(timeout = 2000) public void shortestOperand35() {
    assertConvertsTo("return f(a,b,c,d) * moshe; ", " return moshe * f(a,b,c,d);");
  }

  @Test(timeout = 2000) public void sortAddition5() {
    assertSimplifiesTo("1 + 2 + 3 + a <3 -4", "a <-7");
  }

  // Spartanizer problem
  @Ignore @Test(timeout = 2000) public void ternarize01() {
    assertConvertsTo("String $ = s;if (s.equals(532)==true) $ = s + 0xABBA;else $ = SPAM;x.y.f($);", "x.y.f(!s.equals(532)?SPAM:s+0xABBA);");
  }

  @Test(timeout = 2000) public void ternarize02() {
    assertConvertsTo("String $ = s;if (s.equals(532)==true) $ = s + 0xABBA;x.y.f($);", "x.y.f(!s.equals(532)?s:s+0xABBA);");
  }

  @Test(timeout = 2000) public void ternarize03() {
    assertConvertsTo("if (s.equals(532)) return 6;return 9;", " return s.equals(532) ? 6 : 9; ");
  }

  @Test(timeout = 2000) public void ternarize04() {
    assertConvertsTo(" int $ = 0;if (s.equals(532)) $ += 6;else $ += 9;/*if (s.equals(532)) $ += 6;else $ += 9;*/ return $;",
        "return (s.equals(532)?6:9);");
  }

  @Test(timeout = 2000) public void ternarize06() {
    assertConvertsTo("String $;$ = s;if (s.equals(532)==true) $ = s + 0xABBA;x.y.f($);", "x.y.f(!s.equals(532)?s:s+0xABBA);");
  }

  @Test public void ternarize07a() {
    assertConvertsTo("String $;$ = s; if ($==true)  $ = s + 0xABBA; x.y.f($); ", "x.y.f(!s?s:s+0xABBA);");
  }

  @Test(timeout = 2000) public void ternarize11() {
    assertConvertsTo("String $ = s, foo = \"bar\";if (s.equals(532)==true) $ = s + 0xABBA;x.y.f($);", "x.y.f(!s.equals(532)?s:s+0xABBA);");
  }

  // Spartanizer problem
  @Ignore @Test(timeout = 2000) public void ternarize17() {
    assertConvertsTo("int a, b; a = 3; b = 5; if (a == 4) if (b == 3) b = r();\n else b = a; else if (b == 3) b = r(); else b = a;", "r();");
  }

  @Test(timeout = 2000) public void ternarize18() {
    assertConvertsTo(" String s = X; String $ = s; int a = 0; if (s.equals($)) x.y.f(tH3 + $);\n else x.y.f(h2A+ $ + a + s);",
        "x.y.f(X.equals(X)?tH3+X:h2A+X+0+X);");
  }

  @Test(timeout = 2000) public void ternarize23() {
    assertConvertsTo("int a=0;if (s.equals(532)) a+=y(2)+10;else a+=r(3)-6;", "s.equals(532);y(2);r(3);");
  }

  @Test(timeout = 2000) public void ternarize24() {
    assertConvertsTo("boolean c;if (s.equals(532)) c=false;else c=true;", "s.equals(532);");
  }

  @Ignore @Test(timeout = 2000) public void ternarize40() {
    assertConvertsTo("int a, b, c;a = 3;b = 5;if (a == 4) while (b == 3) c = a;else while (b == 3) c = a*a;",
        "int c;if(3==4)while(5==3)c=3;else while(5==3)c=9;");
  }

  @Test(timeout = 2000) public void ternarize49a() {
    assertConvertsTo(
        " int size = 17; if (m.equals(153)==true) for (final Integer ¢ : range.to(size)){ sum += ¢;\n"
            + " } else for (final Integer ¢ : range.to(size)){ S.out.l('f',¢); }",
        "if(m.equals(153))for(final Integer ¢ : range.to(17))sum += ¢;else  for(final Integer ¢ : range.to(17)) S.out.l('f',¢);");
  }

  @Test(timeout = 2000) public void ternarize54() {
    assertConvertsTo("if (s == null) return Z2;if (!s.contains(delimiter())) return s;\nreturn s.replaceAll(delimiter(), ABC + delimiter());",
        "return s==null?Z2:!s.contains(delimiter())?s:s.replaceAll(delimiter(),ABC+delimiter());");
  }
}
