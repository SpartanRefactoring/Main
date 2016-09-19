package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.ast.wizard.*;
import static il.org.spartan.spartanizer.wrings.TESTUtils.*;
import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;
import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.apply;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.spartanizations.*;
import il.org.spartan.spartanizer.wringing.*;

/** Unit tests for {@link NameYourClassHere}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
@Ignore("Still problems with #205") //
public final class BlockSimplifyTest {
  @Test public void complexEmpty0() {
    trimmingOf("{;}").gives("/* empty */    ");
  }

  @Test public void complexEmpty0A() {
    trimmingOf("{}").gives("/* empty */");
  }

  @Test public void complexEmpty0B() {
    trimmingOf("{;}").gives("/* empty */");
  }

  @Test public void complexEmpty0C() {
    trimmingOf("{{;}}").gives("/* empty */");
  }

  @Test public void complexEmpty0D() {
    trimmingOf("{;;;{;;;}{;}}").gives("/* empty */    ");
  }

  @Test public void complexEmpty1() {
    trimmingOf("{;;{;{{}}}{}{};}").gives("/* empty */ ");
  }

  @Test public void complexSingleton() {
    assertSimplifiesTo("{;{{;;return b; }}}", "return b;", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void deeplyNestedReturn() {
    assertSimplifiesTo("{{{;return c;};;};}", "return c;", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void empty() {
    assertSimplifiesTo("{;;}", "", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void emptySimpler() {
    assertSimplifiesTo("{;}", "", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void emptySimplest() {
    assertSimplifiesTo("{}", "", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void emptySimplestA() {
    final Wrap w = Wrap.Statement;
    final String wrap = w.on("{}");
    final String unpeeled = apply(new BlockSimplify(), wrap);
    if (wrap.equals(unpeeled))
      azzert.fail("Nothing done on " + "{}");
    final String peeled = w.off(unpeeled);
    if ("{}".equals(peeled))
      azzert.that("No similification of " + "{}", peeled, is(not("{}")));
    if (tide.clean(peeled).equals(tide.clean("{}")))
      azzert.that("Simpification of " + "{}" + " is just reformatting", tide.clean("{}"), is(not(tide.clean(peeled))));
    assertSimilar("", peeled);
  }

  @Test public void emptySimplestB() {
    final Wrap w = Wrap.Statement;
    final String wrap = w.on("{}");
    apply(new BlockSimplify(), wrap);
  }

  @Test public void emptySimplestC() {
    final Wrap w = Wrap.Statement;
    final String wrap = w.on("{}");
    apply(new BlockSimplify(), wrap);
  }

  @Test public void emptySimplestD() {
    apply(new BlockSimplify(), Wrap.Statement.on("{}"));
  }

  @Test public void emptySimplestE() {
    final String from = Wrap.Statement.on("{}");
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(from);
    final Document d = new Document(from);
    assert d != null;
    final Wring<Block> inner = new BlockSimplify();
    assert inner != null;
    final WringApplicator s = new WringApplicator(inner);
    assert s != null;
    emptySimplestE_Aux(u, d, s);
  }

  @Test public void expressionVsExpression() {
    trimmingOf("6 - 7 < a * 3").gives("-1 < 3 * a");
  }

  @Test public void literalVsLiteral() {
    trimmingOf("if (a) return b; else c();").gives("if(a)return b;c();");
  }

  @Test public void seriesA00() {
    trimmingOf("public void testParseInteger() {\n" + "  String source = \"10\";\n" + "  {\n" + "    BigFraction c = properFormat.parse(source);\n"
        + "   assert c != null;\n" + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" + "  }\n" + "  {\n" + "    BigFraction c = improperFormat.parse(source);\n"
        + "   assert c != null;\n" + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" + "  }\n" + "}").stays();
  }

  @Test public void seriesA01() {
    trimmingOf("public void f() {\n" + "  String source = \"10\";\n" + "  {\n" + "    BigFraction c = properFormat.parse(source);\n"
        + "   assert c != null;\n" + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" + "  }\n" + "  {\n" + "    BigFraction c = improperFormat.parse(source);\n"
        + "   assert c != null;\n" + "    azzert.assertEquals(BigInteger.TEN, c.getNumerator());\n"
        + "    azzert.assertEquals(BigInteger.ONE, c.getDenominator());\n" + "  }\n" + "}").stays();
  }

  @Test public void seriesA02() {
    trimmingOf("public void f() {\n" + "  string s = \"10\";\n" + "  {\n" + "    f c = properformat.parse(s);\n" + "   assert c != null;\n"
        + "    azzert.assertequals(biginteger.ten, c.getnumerator());\n" + "    azzert.assertequals(biginteger.one, c.getdenominator());\n" + "  }\n"
        + "  {\n" + "    f c = improperformat.parse(s);\n" + "   assert c != null;\n" + "    azzert.assertequals(biginteger.ten, c.getnumerator());\n"
        + "    azzert.assertequals(biginteger.one, c.getdenominator());\n" + "  }\n" + "}").stays();
  }

  @Test public void seriesA03() {
    trimmingOf("public void f() {\n" + "  string s = \"10\";\n" + "  {\n" + "    f c = properformat.parse(s);\n"
        + "    azzert.assertequals(System.out.ten, c.g());\n" + "    azzert.assertequals(System.out.one, c.g());\n" + "  }\n" + "  {\n"
        + "    f c = improperformat.parse(s);\n" + "    azzert.assertequals(System.out.ten, c.g());\n"
        + "    azzert.assertequals(System.out.one, c.g());\n" + "  }\n" + "}").stays();
  }

  @Test public void seriesA04() {
    trimmingOf("public void f() {\n" + "  int s = \"10\";\n" + "  {\n" + "    f c = g.parse(s);\n" + "    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n" + "  }\n" + "  {\n" + "    f c = X.parse(s);\n" + "    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n" + "  }\n" + "}").stays();
  }

  @Test public void seriesA05() {
    trimmingOf("public void f() {\n" + "  int s = \"10\";\n" + "  {\n" + "    f c = g.parse(s);\n" + "    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n" + "  }\n" + "  {\n" + "    f c = X.parse(s);\n" + "    azzert.h(System.out.ten, c.g());\n"
        + "    azzert.h(System.out.one, c.g());\n" + "  }\n" + "}").stays();
  }

  @Test public void seriesA06() {
    trimmingOf("public void f() {\n" + "  int s = \"10\";\n" + "  {\n" + "    f c = g.parse(s);\n" + "    Y(System.out.ten, c.g());\n"
        + "    Y(System.out.one, c.g());\n" + "  }\n" + "  {\n" + "    f c = X.parse(s);\n" + "    Y(System.out.ten, c.g());\n"
        + "    Y(System.out.one, c.g());\n" + "  }\n" + "}").stays();
  }

  @Test public void seriesA07() {
    trimmingOf("public void f() {\n" + "  int s = \"10\";\n" + "  {\n" + "    f c = g.parse(s);\n" + "    Y(System.out.ten, c.g());\n"
        + "    Y(System.out.one, c.g());\n" + "  }\n" + "  {\n" + "    f c = X.parse(s);\n" + "    Y(System.out.ten, c.g());\n"
        + "    Y(System.out.one, c.g());\n" + "  }\n" + "}").stays();
  }

  @Test public void seriesA08() {
    trimmingOf("public void f() {\n" + "  int s = 10;\n" + "  {\n" + "    f c = g.parse(s);\n" + "    Y(q, c.g());\n" + "    Y(ne, c.g());\n"
        + "  }\n" + "  {\n" + "    f c = X.parse(s);\n" + "    Y(q, c.g());\n" + "    Y(ne, c.g());\n" + "  }\n" + "}").stays();
  }

  @Test public void seriesA09() {
    trimmingOf("public void f() {\n" + "  int s = 10;\n" + "  {\n" + "     g.parse(s);\n" + "    Y(q, c.g());\n" + "  }\n" + "  {\n"
        + "     X.parse(s);\n" + "    Y(q, c.g());\n" + "  }\n" + "}")
            .gives(
                "public void f() {\n" + "  int s = 10;\n" + "  g.parse(s);\n" + "  Y(q, c.g());\n" + "  X.parse(s);\n" + "  Y(q, c.g());\n" + "}\n")
            .stays();
  }

  @Test public void seriesA10() {
    trimmingOf("public void f() {\n" + "  int s = 10;\n" + "  {\n" + "    g.parse(s);\n" + "    Y(q, c.g());\n" + "  }\n" + "  {\n"
        + "    X.parse(s);\n" + "    Y(q, c.g());\n" + "  }\n" + "}")
            .gives(
                "public void f() {\n" + "  int s = 10;\n" + "  g.parse(s);\n" + "  Y(q, c.g());\n" + "  X.parse(s);\n" + "  Y(q, c.g());\n" + "}\n")
            .stays();
  }

  @Test public void threeStatements() {
    assertSimplifiesTo("{i++;{{;;return b; }}j++;}", "i++;return b;j++;", new BlockSimplify(), Wrap.Statement);
  }

  private void emptySimplestE_Aux(final CompilationUnit u, final Document d, final WringApplicator a) {
    try {
      a.rewriterOf(u,  (IMarker) null).rewriteAST(d, null).apply(d);
    } catch (MalformedTreeException | BadLocationException e) {
      throw new AssertionError(e);
    }
  }
}
