package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for {@link NameYourClassHere}
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class BlockSimplifyTest {
  @Test public void complexEmpty0() {
    trimmingOf("{;}")//
        .gives("/* empty */    ");
  }

  @Test public void complexEmpty0A() {
    trimmingOf("{}")//
        .gives("/* empty */");
  }

  @Test public void complexEmpty0B() {
    trimmingOf("{;}")//
        .gives("/* empty */");
  }

  @Test public void complexEmpty0C() {
    trimmingOf("{{;}}")//
        .gives("/* empty */");
  }

  @Test public void complexEmpty0D() {
    trimmingOf("{;;;{;;;}{;}}")//
        .gives("/* empty */    ");
  }

  @Test public void complexEmpty1() {
    trimmingOf("{;;{;{{}}}{}{};}")//
        .gives("/* empty */ ");
  }

  @Test public void complexSingleton() {
    trimmingOf("{;{{;;return b; }}}")//
        .gives("return b;");
  }

  @Test public void deeplyNestedReturn() {
    trimmingOf("{{{;return c;};;};}")//
        .gives("return c;");
  }

  @Test public void empty() {
    trimmingOf("{;;}")//
        .gives("");
  }

  @Test public void emptySimpler() {
    trimmingOf("{;}")//
        .gives("");
  }

  @Test public void emptySimplest() {
    trimmingOf("{}")//
        .gives("");
  }

  @Test public void expressionVsExpression() {
    trimmingOf("6 - 7 <a * 3")//
        .gives("-1 <3 * a");
  }

  @Test public void literalVsLiteral() {
    trimmingOf("if (a) return b; else c();")//
        .gives("if(a)return b;c();");
  }

  @Test public void seriesA00() {
    trimmingOf("public void testParseInteger() {\n  String source = \"10\";\n  {\n    BigFraction c = properFormat.parse(source2);\n"
        + "   assert c != null;\n    azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());\n  }\n  {\n"
        + "    BigFraction c = improperFormat.parse(source);\n   assert c != null;\n"
        + "    azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA01() {
    trimmingOf("public void f() {\n  String source = \"10\";\n  {\n    BigFraction c = properFormat.parse(source2);\n"
        + "   assert c != null;\n    azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());\n  }\n  {\n"
        + "    BigFraction c = improperFormat.parse(source);\n   assert c != null;\n"
        + "    azzert.wizard.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.wizard.assertEquals(BigInteger.ONE, c.getDenominator());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA02() {
    trimmingOf("public void f() {\n  string s = \"10\";\n  {\n    f c = properformat.parse(s2);\n   assert c != null;\n"
        + "    azzert.wizard.assertEquals(biginteger.ten, c.getnumerator());\n"
        + "    azzert.wizard.assertEquals(biginteger.one, c.getdenominator());\n  }\n  {\n    f c = improperformat.parse(s);\n"
        + "   assert c != null;\n    azzert.wizard.assertEquals(biginteger.ten, c.getnumerator());\n"
        + "    azzert.wizard.assertEquals(biginteger.one, c.getdenominator());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA03() {
    trimmingOf("public void f() {\n  string s = \"10\";\n  {\n    f c = properformat.parse(s2);\n"
        + "    azzert.wizard.assertEquals(System.out.ten, c.g());\n    azzert.wizard.assertEquals(System.out.one, c.g());\n  }\n  {\n"
        + "    f c = improperformat.parse(s);\n    azzert.wizard.assertEquals(System.out.ten, c.g());\n"
        + "    azzert.wizard.assertEquals(System.out.one, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA04() {
    trimmingOf("public void f() {\n  int s = \"10\";\n  {\n    f c = g.parse(s2);\n    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n  }\n  {\n    f c = X.parse(s);\n    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA05() {
    trimmingOf("public void f() {\n  int s = \"10\";\n  {\n    f c = g.parse(s2);\n    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n  }\n  {\n    f c = X.parse(s);\n    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA06() {
    trimmingOf("public void f() {\n  int s = \"10\";\n  {\n    f c = g.parse(s2);\n    Y(System.out.ten, c.g());\n"
        + "    Y(System.out.one, c.g());\n  }\n  {\n    f c = X.parse(s);\n    Y(System.out.ten, c.g());\n    Y(System.out.one, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA07() {
    trimmingOf("public void f() {\n  int s = \"10\";\n  {\n    f c = g.parse(s2);\n    Y(System.out.ten, c.g());\n"
        + "    Y(System.out.one, c.g());\n  }\n  {\n    f c = X.parse(s);\n    Y(System.out.ten, c.g());\n    Y(System.out.one, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA08() {
    trimmingOf("public void f() {\n  int s = 10;\n  {\n    f c = g.parse(s2);\n    Y(q, c.g());\n    Y(ne, c.g());\n"
        + "  }\n  {\n    f c = X.parse(s);\n    Y(q, c.g());\n    Y(ne, c.g());\n  }\n}")//
            .stays();
  }

  @Test public void seriesA09() {
    trimmingOf("public void f() {\n  int s = 10;\n  {\n     g.parse(s);\n    Y(q, c.g());\n  }\n  {\n     X.parse(s);\n    Y(q, c.g());\n  }\n}")
        .gives("public void f() {\n  int s = 10;\n  g.parse(s);\n  Y(q, c.g());\n  X.parse(s);\n  Y(q, c.g());\n}\n").stays();
  }

  @Test public void seriesA10() {
    trimmingOf("public void f() {\n  int s = 10;\n  {\n    g.parse(s);\n    Y(q, c.g());\n  }\n  {\n    X.parse(s);\n    Y(q, c.g());\n  }\n}")
        .gives("public void f() {\n  int s = 10;\n  g.parse(s);\n  Y(q, c.g());\n  X.parse(s);\n  Y(q, c.g());\n}\n").stays();
  }

  @Test public void threeStatements() {
    trimmingOf("{i++;{{;;return b; }}j++;}")//
        .gives("i++;return b;j++;");
  }
}
