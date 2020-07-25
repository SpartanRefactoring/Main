package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.azzert.hasItem;
import static fluent.ly.azzert.is;
import static il.org.spartan.spartanizer.engine.parse.e;
import static il.org.spartan.spartanizer.engine.parse.i;

import java.util.List;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.junit.Ignore;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metric;
import il.org.spartan.spartanizer.ast.nodes.metrics.Metrics;
import il.org.spartan.spartanizer.ast.safety.az;

/** Test class for metrics.java. for more information, please view issue #823
 * @author Inbal Matityahu
 * @author Or Troyaner
 * @author Tom Nof
 * @since 16-11-04 */
// TODO: Yossi Gil (?)
@Ignore
@SuppressWarnings({ "static-method", "javadoc" })
public final class metricsTest {
  private final String helloWorldQuoted = "\"Hello, World!\\n\"";
  private final Expression x1 = e("(-b - sqrt(b * b - 4 * a* c))/(2*a)"), x2 = e("(-b + sqrt(b * b - 4 * a* c))/(2*a)");
  private final Expression booleans = e("true||false||true");
  private final Expression helloWorld = e("f(" + helloWorldQuoted + ")");

  @Test public void accurateLiterals() {
    azzert.that(Metrics.literals(helloWorld), hasItem("Hello, World!\n"));
  }
  @Test public void bodySizeTest() {
    azzert.that(Metrics.bodySize(booleans), is(0));
    azzert.that(Metrics.bodySize(make.ast("static boolean foo() {}")), is(1));
    azzert.that(Metrics.bodySize(make.ast("static boolean foo() {int x=3;}")), is(6));
    azzert.that(Metrics.bodySize(make.ast("static boolean foo() {int x=3; int y=4;}")), is(11));
  }
  @Test public void condensedSizeTest() {
    azzert.that(Metrics.condensedSize(booleans), is(17));
    azzert.that(Metrics.condensedSize(x1), is(26));
  }
  @Test public void countMethods() {
    azzert.that(Metrics.countMethods(make.ast("static boolean foo() {while((boolean)1==true) return true; }")), is(1));
  }
  @Test public void dexterityIsNull() {
    azzert.that(Metrics.dexterity(null), is(0));
  }
  @Test public void dictionary() {
    azzert.that(Metric.dictionary(x1), hasItem("a"));
    azzert.that(Metric.dictionary(x2), hasItem("b"));
    azzert.that(Metric.dictionary(x2), hasItem("sqrt"));
  }
  @Test public void horizontalComplexityTest() {
    // Test a null list and a null Statement
    final Statement s = null;
    azzert.that(Metrics.horizontalComplexity(0, s), is(0));
    azzert.that(Metrics.horizontalComplexity(0, (List<Statement>) null), is(0));
    // Test a list with one null statement
    final List<Statement> statements = an.empty.list();
    statements.add(s);
    azzert.that(Metrics.horizontalComplexity(0, statements), is(0));
    statements.add(az.statement(make.ast("if(true) return 1;")));
    azzert.that(Metrics.horizontalComplexity(0, statements), is(13446));
  }
  // horizontalComplexity
  @Test public void issue101_5() {
    azzert.that(Metrics.nodes(i("3+4+5+6")), is(5));
  }
  @Test public void issue128_1() {
    azzert.that(Metrics.nodes(i("3+4")), is(3));
  }
  @Test public void issue128_11() {
    azzert.that(Metrics.internals(i("3+4")), is(1));
  }
  @Test public void issue128_12() {
    azzert.that(Metrics.leaves(i("3+4")), is(2));
  }
  @Test public void issue128_13() {
    azzert.that(Metrics.internals(e("a==4 ? 34 : 56")), is(2));
  }
  @Test public void issue128_14() {
    azzert.that(Metrics.leaves(e("a==4 ? 34 : 56")), is(4));
  }
  @Test public void issue128_15() {
    azzert.that(Metrics.dexterity(e("1+2")), is(2));
  }
  @Test public void issue128_16() {
    azzert.that(Metrics.dexterity(e("a+2")), is(3));
  }
  @Test public void issue128_17() {
    azzert.that(Metrics.dexterity(e("g(false)||a(h())")), is(3));
  }
  @Test public void issue128_18() {
    azzert.that(Metrics.dexterity(e("a==4 ? (34++) : 56+34+99")), is(5));
  }
  @Test public void issue128_2() {
    azzert.that(Metrics.nodes(i("3+4")), is(3));
  }
  @Test public void issue128_3() {
    azzert.that(Metrics.nodes(i("5*6+43*2")), is(7));
  }
  @Test public void issue128_4() {
    azzert.that(Metrics.nodes(i("3+4*4+6*7+8")), is(11));
  }
  @Test public void issue128_6() {
    azzert.that(Metrics.nodes(e("a==4 ? 34 : 56")), is(6));
  }
  @Test public void issue128_7() {
    azzert.that(Metrics.nodes(e("a==4 ? 34 : 56+34")), is(8));
  }
  @Test public void issue128_8() {
    azzert.that(Metrics.nodes(e("a==4 ? 34 : 56+34+99")), is(9));
  }
  @Test public void issue128_9() {
    azzert.that(Metrics.nodes(e("!f.g(X,false)||a.b.e(m.h())")), is(10));
  }
  @Test public void issue129_10() {
    azzert.that(Metrics.nodes(e("g(false)||a(h())")), is(5));
  }
  @Test public void literacy() {
    azzert.that(Metrics.literacy(x1), is(2));
    azzert.that(Metrics.literacy(x2), is(2));
    azzert.that(Metrics.literacy(i("3+4+5+6")), is(4));
  }
  @Test public void literals() {
    azzert.that(Metrics.literals(x1), hasItem("2"));
    azzert.that(Metrics.literals(x2), hasItem("4"));
    azzert.that(Metrics.literals(i("3+4+5+6")), hasItem("6"));
  }
  @Test public void literalsBoolean() {
    azzert.that(Metrics.literals(booleans), hasItem("true"));
    azzert.that(Metrics.literals(booleans), hasItem("false"));
    azzert.that(Metrics.vocabulary(booleans), is(0));
  }
  @Test public void nullIsLiteral() {
    azzert.that(Metrics.literals(e("null")), hasItem("null"));
  }
  @Test public void stringAreLiterals() {
    azzert.that(Metrics.literacy(helloWorld), is(1));
  }
  @Test public void tokensTest() {
    azzert.that(Metrics.tokens(helloWorldQuoted), is(1));
    azzert.that(Metrics.tokens("\\*Hello, World!\\n*\""), is(8));
    azzert.that(Metrics.tokens("\\/*Hello*/"), is(0));
  }
  @Test public void understandability() {
    azzert.that(Metrics.nodeUnderstandability(findFirst.typeDeclaration(make.ast("class C{public void m(){ int x;}}"))), is(1));
  }
  @Test public void understandability2() {
    azzert.that(Metrics.nodeUnderstandability(findFirst.variableDeclarationFragment(make.ast("class C{public void m(){ int x; int y;}}"))), is(6));
  }
  @Test public void understandability3() {
    azzert.that(Metrics.subtreeUnderstandability2(make.ast("int x;")), is(0));
  }
  @Test public void understandability4() {
    azzert.that(Metrics.subtreeUnderstandability2(make.ast("{int x;}")), is(0));
  }
  @Test public void understandability5() {
    azzert.that(Metrics.subtreeUnderstandability2(make.ast("void f(){int x;}")), is(1));
  }
  @Test public void understandability6() {
    azzert.that(Metrics.subtreeUnderstandability2(make.ast("void f(){int x; int y;}")), is(3));
  }
  @Test public void understandability7() {
    azzert.that(Metrics.subtreeUnderstandability2(findFirst.typeDeclaration(make.ast("class C{public void m(){ int x; int y;}}"))), is(5));
  }
  @Test public void understandability8() {
    azzert.that(Metrics.subtreeUnderstandability2(findFirst.instanceOf(MethodDeclaration.class)
        .in(make.ast(//
            "@Override public boolean containsValue( Object value){\n" + //
                "  for (  Collection<V> collection : asMap().values()) {\n" + //
                "    if (collection.contains(value)) {\n" + //
                "      return true;\n" + //
                "    }\n" + //
                "   }\n" + //
                "   return false;\n" + //
                " }"))),
        is(7));
  }
  @Test public void vocabulary() {
    azzert.that(Metrics.vocabulary(x1), is(4));
    azzert.that(Metrics.vocabulary(x2), is(4));
    azzert.that(Metrics.vocabulary(booleans), is(0));
  }
}