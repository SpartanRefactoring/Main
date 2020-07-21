package il.org.spartan.spartanizer.ast.engine.nominal;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.parse.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** A test suite for class {@link cCamelCase}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see step */
@SuppressWarnings({ "static-method", "javadoc" })
public final class namerTest {
  @Test public void arrayOfInts() {
    azzert.that(abbreviate.it(t("int[][] __;")), equalTo("iss"));
  }
  @Test public void components01() {
    azzert.that(cCamelCase.components("ConfusingASTNode"), equalTo(new String[] { "Confusing", "AST", "Node" }));
  }
  @Test public void components02() {
    azzert.that(cCamelCase.components("camelCaseXML"), equalTo(new String[] { "camel", "Case", "XML" }));
  }
  @Test public void components03() {
    azzert.that(cCamelCase.components("with_bTable"), equalTo(new String[] { "with", "b", "Table" }));
  }
  @Test public void streamOfInts() {
    azzert.that(abbreviate.it(t("Stream<Integer> __;")), is("is"));
  }
  @Test public void streamOfIntsFail() {
    azzert.that(abbreviate.it(t("StRe1am<Integer> __;")), is("i"));
  }
  @Test public void listOfInts() {
    azzert.that(abbreviate.it(t("List<Set<Integer>> __;")), is("iss"));
  }
  @Test public void shortNameASTRewriter() {
    azzert.that(abbreviate.it(t("ASTRewriter __;")), is("r"));
  }
  @Test public void shortNameChar() {
    azzert.that(abbreviate.it(t("char __;")), equalTo("c"));
  }
  @Test public void shortNameDouble() {
    azzert.that(abbreviate.it(t("double __;")), equalTo("d"));
  }
  @Test public void shortNameExpression() {
    azzert.that(abbreviate.it(t("Expression __;")), equalTo("x"));
  }
  @Test public void shortNameExpressions() {
    azzert.that(abbreviate.it(t("Expression[] __;")), equalTo("xs"));
  }
  @Test public void shortNameExpressionsIterable() {
    azzert.that(abbreviate.it(t("Iterable<Iterable<Expression>> __;")), equalTo("xss"));
  }
  @Test public void shortNameExpressionsList() {
    azzert.that(abbreviate.it(t("List<Expression> __;")), equalTo("xs"));
  }
  @Test public void shortNameInt() {
    azzert.that(abbreviate.it(t("int __;")), equalTo("i"));
  }
  @Test public void shortNameQualifiedType() {
    azzert.that(abbreviate.it(t("org.eclipse.jdt.core.dom.InfixExpression __;")), equalTo("x"));
  }
  @Test public void test2() {
    final String[] components = cCamelCase.components("Table_NanosByCategories");
    azzert.that(components, is(new String[] { "Table", "Nanos", "By", "Categories" }));
    azzert.that(separate.these(the.tailOf(components)).by('-').toLowerCase(), is("nanos-by-categories"));
  }
}
