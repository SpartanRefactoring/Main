package il.org.spartan.spartanizer.wring;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.spartanizations.TESTUtils.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.spartanizations.*;

/** * Unit tests for the nesting class Unit test for the containing class. Note
 * our naming convention: a) test methods do not use the redundant "test"
 * prefix. b) test methods begin with the name of the method they check.
 * @author Yossi Gil
 * @since 2014-07-10 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) public class FixedPointTest {
  private static void assertConvertsTo(final String from, final String expected) {
    assertWrappedTranslation(from, expected, Wrap.Statement);
  }

  private static void assertSimplifiesTo(final String from, final String expected) {
    assertWrappedTranslation(from, expected, Wrap.Expression);
  }

  private static void assertWrappedTranslation(final String from, final String expected, final Wrap w) {
    final String wrap = w.on(from);
    assertEquals(from, w.off(wrap));
    final String unpeeled = Trimmer.fixedPoint(wrap);
    if (wrap.equals(unpeeled))
      fail("Nothing done on " + from);
    final String peeled = w.off(unpeeled);
    if (peeled.equals(from))
      assertNotEquals("No similification of " + from, from, peeled);
    if (tide.clean(peeled).equals(tide.clean(from)))
      assertNotEquals("Simpification of " + from + "is just reformatting", tide.clean(peeled), tide.clean(from));
    assertSimilar(expected, peeled);
  }

  @Test(timeout = 2000) public void chainComparison() {
    assertSimplifiesTo("a == true == b == c", "a == b == c");
  }

  @Test public void commonPrefixIfBranchesInBlock() {
    assertConvertsTo("{" + //
        "    if (a) {\n" + //
        "      f();\n" + //
        "      g();\n" + //
        "      ++i;\n" + //
        "    } else {\n" + //
        "      f();\n" + //
        "      g();\n" + //
        "      --i;\n" + //
        "    }" + //
        "}", "" + // //
            "   f();\n" + //
            "   g();\n" + //
            "    if (a) \n" + //
            "      ++i;\n" + //
            "    else \n" + //
            "      --i;" //
    );
  }

  @Test(timeout = 2000) public void desiredSimplificationOfExample() {
    assertSimplifiesTo("on * notion * of * no * nothion < the * plain + kludge", "no*of*on*notion*nothion<kludge+the*plain");
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
    final Object a = null;
    assert hasNull(a);
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

  @Test(timeout = 2000) public void inlineInitializers() {
    assertConvertsTo("int b,a = 2; return 3 * a * b; ", "return 2 * 3 * b;");
  }

  @Test(timeout = 2000) public void issue37() {
    assertConvertsTo(
        "" + //
            "    int result = mockedType.hashCode();\n" + //
            "    result = 31 * result + types.hashCode();\n" + //
            "    return result;\n" + //
            "", //
        "return 31*mockedType.hashCode()+types.hashCode();");
  }

  @Test(timeout = 2000) public void issue37abbreviated() {
    assertConvertsTo(
        "" + //
            "    int a = 3;\n" + //
            "    a = 31 * a;\n" + //
            "    return a;\n" + //
            "", //
        "return 93;");
  }

  @Test public void issue43() {
    assertConvertsTo(
        "" //
            + "String t = Z2;  "//
            + " t = t.f(A).f(b) + t.f(c);   "//
            + "return (t + 3);    ", //
        ""//
            + "return(Z2.f(A).f(b)+Z2.f(c)+3);" //
            + "" //
    );
  }

  @Test(timeout = 2000) public void multipleIfDeclarationAssignment() {
    assertConvertsTo(//
        "int a, b;a = 3;b = 5;if (a == 4)  if (b == 3) b = 2;else b = a;else if (b == 3) b = 2;else b = a*a;", //
        "int b = 3==4? 5==3 ? 2 :3:5==3?2:9;");
  }

  @Test(timeout = 2000) public void multipleInline() {
    assertConvertsTo("int b=5,a = 2,c=4; return 3 * a * b * c; ", "return 120;");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst02() {
    assertConvertsTo(
        "" //
            + "if (!s.equals(0xDEAD)) { "//
            + " int $=0;"//
            + " for (int i=0;i<s.length();++i)     "//
            + "   if (s.charAt(i)=='a')      "//
            + "     $ += 2;"//
            + "   else "//
            + "  if (s.charAt(i)=='d')      "//
            + "       $ -= 1;"//
            + "  return $;"//
            + "} else {    "//
            + " return 8;"//
            + "}",
        "" //
            + " if (s.equals(0xDEAD)) \n" + //
            "    return 8;" + //
            "   int $ = 0;\n" + //
            "   for (int i = 0;i < s.length();++i)\n" + //
            "     if (s.charAt(i) == 'a')\n" + //
            "       $ += 2;\n" + //
            "      else " + //
            "       if (s.charAt(i) == 'd')\n" + //
            "        --$;\n" + //
            "  return $;\n" //
    );
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst03a() {
    assertConvertsTo(
        "  if ('a' == s.charAt(i))\n" + //
            "          $ += 2;\n" + //
            "        else if ('d' == s.charAt(i))\n" + //
            "          $ -= 1;\n" + //
            "", //
        "  if (s.charAt(i) == 'a')\n" + //
            "          $ += 2;\n" + //
            "        else if (s.charAt(i) == 'd')\n" + //
            "          --$;\n" + //
            "");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst09() {
    assertSimplifiesTo("s.equals(532) ? 9 * yada3(s.length()) : 6 ", "!s.equals(532)?6:9*yada3(s.length())");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst11() {
    assertSimplifiesTo("b != null && b.getNodeType() == ASTNode.BLOCK ? getBlockSingleStmnt((Block) b) : b ",
        "b==null||b.getNodeType()!=ASTNode.BLOCK?b:getBlockSingleStmnt((Block)b)");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst12() {
    assertConvertsTo("if (FF() && TT()){    foo1();foo2();}else shorterFoo();", //
        "  if (!FF() || !TT())     shorterFoo();else {foo1();foo2();}");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst13() {
    assertConvertsTo(
        "    int a = 0;\n" + //
            "    if (a > 0)\n" //
            + "      return 6;\n" //
            + "    else {\n" //
            + "      int b = 9;\n" //
            + "      b *= b;\n" //
            + "      return b;\n" //
            + "    }\n" //
            + "    ;", //
        "return 0>0?6:81;");
  }

  @Test(timeout = 2000) public void shortestIfBranchFirst14() {
    assertConvertsTo("    int a = 0;\n" + //
        "    if (a > 0) {\n" + //
        "      int b = 9;\n" + //
        "      b *= b;\n" + //
        "      return 6;\n" + //
        "    } else {\n" + //
        "      int a = 5;\n" + //
        "      return b;\n" + //
        "    }", "return 0>0?6:b;");
  }

  @Ignore("bug") @Test(timeout = 2000) public void shortestOperand03() {
    assertConvertsTo(//
        "k = k * 4;if (1 + 2 - 3 - 4 + 5 / 6 - 7 + 8 * 9 > 4+k) return true;", //
        "k=4*k;if(5 / 6+8*9+1+2-3-4-7>k+4)return true;");
  }

  @Test(timeout = 2000) public void shortestOperand04() {
    assertConvertsTo("return (1 + 2 < 3 & 7 + 4 > 2 + 1 || 6 - 7 < 2 + 1);", //
        "return(3<3&11>3||-1<3);");
  }

  @Test(timeout = 2000) public void shortestOperand07() {
    assertConvertsTo("int y,o,g,i,s;return ( y + o + s > s + i |  g > 42);", "int y,o,g,i,s;return(g>42|o+s+y>i+s);");
  }

  @Test(timeout = 2000) public void shortestOperand08() {
    assertConvertsTo(
        //
        "if (bob.father.age > 42 && bob.mother.father.age > bob.age ) return true; else return false;",
        "return bob.father.age>42&&bob.mother.father.age>bob.age;");
  }

  @Test(timeout = 2000) public void shortestOperand26() {
    assertConvertsTo("return f(a,b,c,d) | f() | 0;} ", "return f()|f(a,b,c,d)|0;}");
  }

  @Test(timeout = 2000) public void shortestOperand35() {
    assertConvertsTo("return f(a,b,c,d) * moshe; ", "   return moshe * f(a,b,c,d);");
  }

  @Test(timeout = 2000) public void sortAddition5() {
    assertSimplifiesTo("1 + 2  + 3 + a < 3 -4", "a + 1 + 2 + 3 < -1");
  }

  @Test(timeout = 2000) public void ternarize01() {
    assertConvertsTo(
        //
        "String $ = s;if (s.equals(532)==true)    $ = s + 0xABBA;else    $ = SPAM;System.out.println($);",
        "System.out.println((!s.equals(532)?SPAM:s+0xABBA));");
  }

  @Test(timeout = 2000) public void ternarize02() {
    assertConvertsTo(//
        "String $ = s;if (s.equals(532)==true)    $ = s + 0xABBA;System.out.println($);", //
        "System.out.println((!s.equals(532)?s:s+0xABBA));");
  }

  @Test(timeout = 2000) public void ternarize03() {
    assertConvertsTo(//
        "if (s.equals(532))    return 6;return 9;", //
        " return s.equals(532) ? 6 : 9; ");
  }

  @Test(timeout = 2000) public void ternarize04() {
    assertConvertsTo("  int $ = 0;if (s.equals(532))    $ += 6;else    $ += 9;/*if (s.equals(532))    $ += 6;else    $ += 9;*/   return $;",
        "return (s.equals(532)?6:9);");
  }

  @Test(timeout = 2000) public void ternarize06() {
    assertConvertsTo(//
        "String $;$ = s;if (s.equals(532)==true)    $ = s + 0xABBA;System.out.println($);", //
        "System.out.println((!s.equals(532)?s:s+0xABBA));");
  }

  @Test public void ternarize07a() {
    assertConvertsTo(
        "" //
            + "String $;" //
            + "$ = s;   " //
            + "if ($==true)    " //
            + "  $ = s + 0xABBA;   " //
            + "System.out.println($); " //
            + "" //
        , "System.out.println((!s?s:s+0xABBA));" //
    );
  }

  @Test(timeout = 2000) public void ternarize11() {
    assertConvertsTo("String $ = s, foo = \"bar\";if (s.equals(532)==true)    $ = s + 0xABBA;System.out.println($);",
        "System.out.println((!s.equals(532)?s:s+0xABBA));");
  }

  @Test public void ternarize15() {
    assertConvertsTo(
        "  String $ = mode, foo = \"Not in test mode\";int k;k = 1984;if (mode.equals(f())==true)    foo = test-bob;foo = \"sponge-bob\";",
        "String $=mode,foo=\"Not in test mode\";int k=1984;if(mode.equals(f()))foo=test-bob;foo=\"sponge-bob\";");
  }

  @Test(timeout = 2000) public void ternarize17() {
    assertConvertsTo("    int a, b;\n" + //
        "    a = 3;\n" + //
        "    b = 5;\n" + //
        "    if (a == 4)\n" + //
        "      if (b == 3)\n" + //
        "        b = r();\n" + //
        "      else\n" + //
        "        b = a;\n" + //
        "    else if (b == 3)\n" + //
        "      b = r();\n" + //
        "    else\n" + //
        "      b = a;", "int b=5!=3?3:r();");
  }

  @Test(timeout = 2000) public void ternarize18() {
    assertConvertsTo(//
        "    String s = X;\n" + //
            "    String $ = s;\n" + //
            "    int a = 0;\n" + //
            "    if (s.equals($))\n" + //
            "      System.out.println(tH3 + $);\n" + //
            "    else\n" + //
            "      System.out.println(h2A+ $ + a + s);",
        "System.out.println(X.equals(X)?tH3+X:h2A+X+0+X);");
  }

  @Test(timeout = 2000) public void ternarize23() {
    assertConvertsTo(//
        "int a=0;if (s.equals(532))   a+=y(2)+10;else a+=r(3)-6;", //
        "int a=(s.equals(532)?y(2)+10:r(3)-6);");
  }

  @Test(timeout = 2000) public void ternarize24() {
    assertConvertsTo(//
        "boolean c;if (s.equals(532))    c=false;else c=true;", //
        "boolean c=!s.equals(532);");
  }

  @Test(timeout = 2000) public void ternarize40() {
    assertConvertsTo(//
        "int a, b, c;a = 3;b = 5;if (a == 4)     while (b == 3)     c = a;else    while (b == 3)     c = a*a;", //
        "int c;if(3==4)while(5==3)c=3;else while(5==3)c=9;");
  }

  @Test public void ternarize49a() {
    assertConvertsTo(
        ""//
            + "    int size = 17;\n"//
            + "   if (m.equals(153)==true)\n"//
            + "     for (int i=0; i < size; i++){\n"//
            + "       S.out.l(HH);\n"//
            + "     }\n"//
            + "   else\n"//
            + "     for (int i=0; i < size; i++){\n"//
            + "       S.out.l('f');\n"//
            + "     }",
        "" //
            + "if(m.equals(153))"//
            + "for(int i=0;i<17;++i)"//
            + "  S.out.l(HH);"//
            + "else "//
            + "  for(int i=0;i<17;++i) "//
            + "    S.out.l('f');"//
    );
  }

  @Test(timeout = 2000) public void ternarize54() {
    assertConvertsTo(//
        "if (s == null)\n" + // /
            "  return Z2;\n" + //
            "if (!s.contains(delimiter()))\n" + //
            "  return s;\n" + //
            "return s.replaceAll(delimiter(), ABC + delimiter());", //
        "return s==null?Z2:!s.contains(delimiter())?s:s.replaceAll(delimiter(),ABC+delimiter());");
  }
}
