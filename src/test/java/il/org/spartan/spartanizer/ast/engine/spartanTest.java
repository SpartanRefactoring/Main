package il.org.spartan.spartanizer.ast.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;

/** A test suite for class {@link namer}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see step */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class spartanTest {
  @Test public void arrayOfInts() {
    azzert.that(namer.shorten(t("int[][] __;")), equalTo("iss"));
  }

  @Test public void listOfInts() {
    azzert.that(namer.shorten(t("List<Set<Integer>> __;")), equalTo("iss"));
  }

  @Test public void listOfIntsa() {
    azzert.that(namer.shorten(t("List<Set<Integer>> __;")), equalTo("iss"));
  }

  @Test public void shortNameASTRewriter() {
    azzert.that(namer.shorten(t("ASTRewriter __;")), equalTo("r"));
  }

  @Test public void shortNameDouble() {
    azzert.that(namer.shorten(t("double __;")), equalTo("d"));
  }

  @Test public void shortNameExpression() {
    azzert.that(namer.shorten(t("Expression __;")), equalTo("x"));
  }

  @Test public void shortNameExpressions() {
    azzert.that(namer.shorten(t("Expression[] __;")), equalTo("xs"));
  }

  @Test public void shortNameExpressionsList() {
    azzert.that(namer.shorten(t("List<Expression> __;")), equalTo("xs"));
  }

  @Test public void shortNameInfrastructure() {
    azzert.that(namer.shorten(t("int __;")), equalTo("i"));
  }

  @Test public void shortNameQualifiedType() {
    azzert.that(namer.shorten(t("org.eclipse.jdt.core.dom.InfixExpression __;")), equalTo("x"));
  }
}
