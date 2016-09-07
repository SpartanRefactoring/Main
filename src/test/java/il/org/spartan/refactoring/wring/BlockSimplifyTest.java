package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.wring.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Yossi Gil
 * @since 2016 */
@SuppressWarnings("static-method") //
public class BlockSimplifyTest {
  @Test public void seriesA00() {
    trimming("" //
        + "public void testParseInteger() {\n" //
        + "  String source = \"10\";\n" //
        + "  {\n" //
        + "    BigFraction c = properFormat.parse(source);\n" //
        + "   assert c != null;\n" //
        + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n" //
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    BigFraction c = improperFormat.parse(source);\n" //
        + "   assert c != null;\n" //
        + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n" //
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" //
        + "  }\n" //
        + "}").to(null);
  }

  @Test public void seriesA01() {
    trimming("" //
        + "public void f() {\n" //
        + "  String source = \"10\";\n" //
        + "  {\n" //
        + "    BigFraction c = properFormat.parse(source);\n" //
        + "   assert c != null;\n" //
        + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n" //
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    BigFraction c = improperFormat.parse(source);\n" //
        + "   assert c != null;\n" //
        + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n" //
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" //
        + "  }\n" //
        + "}").to(null);
  }

  @Test public void seriesA02() {
    trimming("" //
        + "public void f() {\n" //
        + "  string s = \"10\";\n" //
        + "  {\n" //
        + "    f c = properformat.parse(s);\n" //
        + "   assert c != null;\n" //
        + "    azzert.assertequals(biginteger.ten, c.getnumerator());\n" //
        + "    azzert.assertequals(biginteger.one, c.getdenominator());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    f c = improperformat.parse(s);\n" //
        + "   assert c != null;\n" //
        + "    azzert.assertequals(biginteger.ten, c.getnumerator());\n" //
        + "    azzert.assertequals(biginteger.one, c.getdenominator());\n" //
        + "  }\n" //
        + "}").to(null);
  }

  @Test public void seriesA03() {
    trimming("" //
        + "public void f() {\n" //
        + "  string s = \"10\";\n" //
        + "  {\n" //
        + "    f c = properformat.parse(s);\n" //
        + "    azzert.assertequals(System.out.ten, c.g());\n" //
        + "    azzert.assertequals(System.out.one, c.g());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    f c = improperformat.parse(s);\n" //
        + "    azzert.assertequals(System.out.ten, c.g());\n" //
        + "    azzert.assertequals(System.out.one, c.g());\n" //
        + "  }\n" //
        + "}").to(null);
  }

  @Test public void seriesA04() {
    trimming("" //
        + "public void f() {\n" //
        + "  int s = \"10\";\n" //
        + "  {\n" //
        + "    f c = g.parse(s);\n" //
        + "    azzert.h(System.out.ten, c.g());\n" //
        + "    azzert.h(System.out.one, c.g());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    f c = X.parse(s);\n" //
        + "    azzert.h(System.out.ten, c.g());\n" //
        + "    azzert.h(System.out.one, c.g());\n" //
        + "  }\n" //
        + "}").to(null);
  }

  @Test public void seriesA05() {
    trimming("" //
        + "public void f() {\n" //
        + "  int s = \"10\";\n" //
        + "  {\n" //
        + "    f c = g.parse(s);\n" //
        + "    azzert.h(System.out.ten, c.g());\n" //
        + "    azzert.h(System.out.one, c.g());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    f c = X.parse(s);\n" //
        + "    azzert.h(System.out.ten, c.g());\n" //
        + "    azzert.h(System.out.one, c.g());\n" //
        + "  }\n" //
        + "}").to(null);
  }

  @Test public void seriesA06() {
    trimming("" //
        + "public void f() {\n" //
        + "  int s = \"10\";\n" //
        + "  {\n" //
        + "    f c = g.parse(s);\n" //
        + "    Y(System.out.ten, c.g());\n" //
        + "    Y(System.out.one, c.g());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    f c = X.parse(s);\n" //
        + "    Y(System.out.ten, c.g());\n" //
        + "    Y(System.out.one, c.g());\n" //
        + "  }\n" //
        + "}").to(null);
  }

  @Test public void seriesA07() {
    trimming("" //
        + "public void f() {\n" //
        + "  int s = \"10\";\n" //
        + "  {\n" //
        + "    f c = g.parse(s);\n" //
        + "    Y(System.out.ten, c.g());\n" //
        + "    Y(System.out.one, c.g());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    f c = X.parse(s);\n" //
        + "    Y(System.out.ten, c.g());\n" //
        + "    Y(System.out.one, c.g());\n" //
        + "  }\n" //
        + "}").to(null);
  }

  @Test public void seriesA08() {
    trimming("" //
        + "public void f() {\n" //
        + "  int s = 10;\n" //
        + "  {\n" //
        + "    f c = g.parse(s);\n" //
        + "    Y(q, c.g());\n" //
        + "    Y(ne, c.g());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    f c = X.parse(s);\n" //
        + "    Y(q, c.g());\n" //
        + "    Y(ne, c.g());\n" //
        + "  }\n" //
        + "}").to(null);
  }

  @Test public void seriesA09() {
    trimming("" //
        + "public void f() {\n" //
        + "  int s = 10;\n" //
        + "  {\n" //
        + "     g.parse(s);\n" //
        + "    Y(q, c.g());\n" //
        + "  }\n" //
        + "  {\n" //
        + "     X.parse(s);\n" //
        + "    Y(q, c.g());\n" //
        + "  }\n" //
        + "}")
            .to("" //
                + "public void f() {\n" //
                + "  int s = 10;\n" //
                + "  g.parse(s);\n" //
                + "  Y(q, c.g());\n" //
                + "  X.parse(s);\n" //
                + "  Y(q, c.g());\n" //
                + "}\n" //
            ).to(null);
  }

  @Test public void seriesA10() {
    trimming("" //
        + "public void f() {\n" //
        + "  int s = 10;\n" //
        + "  {\n" //
        + "    g.parse(s);\n" //
        + "    Y(q, c.g());\n" //
        + "  }\n" //
        + "  {\n" //
        + "    X.parse(s);\n" //
        + "    Y(q, c.g());\n" //
        + "  }\n" //
        + "}")
            .to("" //
                + "public void f() {\n" //
                + "  int s = 10;\n" //
                + "  g.parse(s);\n" //
                + "  Y(q, c.g());\n" //
                + "  X.parse(s);\n" //
                + "  Y(q, c.g());\n" //
                + "}\n" //
            ).to(null);
  }
}
