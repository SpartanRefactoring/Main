package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for {@link NameYourClassHere}
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class BlockSimplifyTest {
  @Test public void complexEmpty0() {
    trimminKof("{;}")//
        .gives("/* empty */    ");
  }

  @Test public void complexEmpty0A() {
    trimminKof("{}")//
        .gives("/* empty */");
  }

  @Test public void complexEmpty0B() {
    trimminKof("{;}")//
        .gives("/* empty */");
  }

  @Test public void complexEmpty0C() {
    trimminKof("{{;}}")//
        .gives("/* empty */");
  }

  @Test public void complexEmpty0D() {
    trimminKof("{;;;{;;;}{;}}")//
        .gives("/* empty */    ");
  }

  @Test public void complexEmpty1() {
    trimminKof("{;;{;{{}}}{}{};}")//
        .gives("/* empty */ ");
  }

  @Test public void complexSingleton() {
    trimminKof("{;{{;;return b; }}}")//
        .gives("return b;");
  }

  @Test public void deeplyNestedReturn() {
    trimminKof("{{{;return c;};;};}")//
        .gives("return c;");
  }

  @Test public void empty() {
    trimminKof("{;;}")//
        .gives("");
  }

  @Test public void emptySimpler() {
    trimminKof("{;}")//
        .gives("");
  }

  @Test public void emptySimplest() {
    trimminKof("{}")//
        .gives("");
  }

  @Test public void expressionVsExpression() {
    trimminKof("6 - 7 <a * 3")//
        .gives("-1 <3 * a");
  }

  @Test public void literalVsLiteral() {
    trimminKof("if (a) return b; else c();")//
        .gives("if(a)return b;c();");
  }

  @Test public void seriesA00() {
    trimminKof("public void testParseInteger() {\n  String source = \"10\";\n  {\n    BigFraction c = properFormat.parse(source2);\n"
        + "   assert c != null;\n    azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());\n  }\n  {\n"
        + "    BigFraction c = improperFormat.parse(source);\n   assert c != null;\n"
        + "    azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA01() {
    trimminKof("public void f() {\n  String source = \"10\";\n  {\n    BigFraction c = properFormat.parse(source2);\n"
        + "   assert c != null;\n    azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());\n  }\n  {\n"
        + "    BigFraction c = improperFormat.parse(source);\n   assert c != null;\n"
        + "    azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA02() {
    trimminKof("public void f() {\n  string s = \"10\";\n  {\n    f c = properformat.parse(s2);\n   assert c != null;\n"
        + "    azzert.wizard.assertEquals(biginteger.ten, c.getnumerator());\n"
        + "    azzert.wizard.assertEquals(biginteger.one, c.getdenominator());\n  }\n  {\n    f c = improperformat.parse(s);\n"
        + "   assert c != null;\n    azzert.wizard.assertEquals(biginteger.ten, c.getnumerator());\n"
        + "    azzert.wizard.assertEquals(biginteger.one, c.getdenominator());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA03() {
    trimminKof("public void f() {\n  string s = \"10\";\n  {\n    f c = properformat.parse(s2);\n"
        + "    azzert.wizard.assertEquals(System.out.ten, c.g());\n    azzert.wizard.assertEquals(System.out.one, c.g());\n  }\n  {\n"
        + "    f c = improperformat.parse(s);\n    azzert.wizard.assertEquals(System.out.ten, c.g());\n"
        + "    azzert.wizard.assertEquals(System.out.one, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA04() {
    trimminKof("public void f() {\n  int s = \"10\";\n  {\n    f c = g.parse(s2);\n    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n  }\n  {\n    f c = X.parse(s);\n    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA05() {
    trimminKof("public void f() {\n  int s = \"10\";\n  {\n    f c = g.parse(s2);\n    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n  }\n  {\n    f c = X.parse(s);\n    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA06() {
    trimminKof("public void f() {\n  int s = \"10\";\n  {\n    f c = g.parse(s2);\n    Y(System.out.ten, c.g());\n"
        + "    Y(System.out.one, c.g());\n  }\n  {\n    f c = X.parse(s);\n    Y(System.out.ten, c.g());\n    Y(System.out.one, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA07() {
    trimminKof("public void f() {\n  int s = \"10\";\n  {\n    f c = g.parse(s2);\n    Y(System.out.ten, c.g());\n"
        + "    Y(System.out.one, c.g());\n  }\n  {\n    f c = X.parse(s);\n    Y(System.out.ten, c.g());\n    Y(System.out.one, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA08() {
    trimminKof("public void f() {\n  int s = 10;\n  {\n    f c = g.parse(s2);\n    Y(q, c.g());\n    Y(ne, c.g());\n"
        + "  }\n  {\n    f c = X.parse(s);\n    Y(q, c.g());\n    Y(ne, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA09() {
    trimminKof("public void f() {\n  int s = 10;\n  {\n     g.parse(s);\n    Y(q, c.g());\n  }\n  {\n     X.parse(s);\n    Y(q, c.g());\n  }\n}")
        .gives("public void f() {\n  int s = 10;\n  g.parse(s);\n  Y(q, c.g());\n  X.parse(s);\n  Y(q, c.g());\n}\n").stays();
  }

  @Test public void seriesA10() {
    trimminKof("public void f() {\n  int s = 10;\n  {\n    g.parse(s);\n    Y(q, c.g());\n  }\n  {\n    X.parse(s);\n    Y(q, c.g());\n  }\n}")
        .gives("public void f() {\n  int s = 10;\n  g.parse(s);\n  Y(q, c.g());\n  X.parse(s);\n  Y(q, c.g());\n}\n").stays();
  }

  @Test public void threeStatements() {
    trimminKof("{i++;{{;;return b; }}j++;}")//
        .gives("i++;return b;j++;");
  }
}
