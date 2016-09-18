package il.org.spartan.spartanizer.ast;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** A test suite for class {@link spartan}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see step */
@SuppressWarnings({ "static-method", "javadoc" }) @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public final class spartanTest {
  @Test public void arrayOfInts() {
    azzert.that(spartan.shorten(t("int[][] __;")), equalTo("iss"));
  }

  @Test public void listOfInts() {
    azzert.that(spartan.shorten(t("List<Set<Integer>> __;")), equalTo("iss"));
  }

  @Test public void shortNameASTRewriter() {
    azzert.that(spartan.shorten(t("ASTRewriter __;")), equalTo("r"));
  }

  @Test public void shortNameDouble() {
    azzert.that(spartan.shorten(t("double __;")), equalTo("d"));
  }

  @Test public void shortNameExpression() {
    azzert.that(spartan.shorten(t("Expression __;")), equalTo("x"));
  }

  @Test public void shortNameExpressions() {
    azzert.that(spartan.shorten(t("Expression[] __;")), equalTo("xs"));
  }

  @Test public void shortNameExpressionsList() {
    azzert.that(spartan.shorten(t("List<Expression> __;")), equalTo("xs"));
  }

  @Test public void shortNameInfrastructure() {
    azzert.that(spartan.shorten(t("int __;")), equalTo("i"));
  }

  @Test public void shortNameQualifiedType() {
    azzert.that(spartan.shorten(t("org.eclipse.jdt.core.dom.InfixExpression __;")), equalTo("x"));
  }
}
