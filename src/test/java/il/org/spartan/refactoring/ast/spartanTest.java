package il.org.spartan.refactoring.ast;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.engine.into.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.engine.*;

/** A test suite for class {@link spartan}
 * @author Yossi Gil
 * @since 2015-07-18
 * @see step */
@SuppressWarnings({ "static-method", "javadoc" }) @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class spartanTest {
  @Test public void arrayOfInts() {
    azzert.that(spartan.shorten(t("int[][] _;")), equalTo("iss"));
  }

  @Test public void listOfInts() {
    azzert.that(spartan.shorten(t("List<Set<Integer>> _;")), equalTo("iss"));
  }

  @Test public void shortNameASTRewriter() {
    azzert.that(spartan.shorten(t("ASTRewriter _;")), equalTo("r"));
  }

  @Test public void shortNameDouble() {
    azzert.that(spartan.shorten(t("double _;")), equalTo("d"));
  }

  @Test public void shortNameExpression() {
    azzert.that(spartan.shorten(t("Expression _;")), equalTo("x"));
  }

  @Test public void shortNameExpressions() {
    azzert.that(spartan.shorten(t("Expression[] _;")), equalTo("xs"));
  }

  @Test public void shortNameExpressionsList() {
    azzert.that(spartan.shorten(t("List<Expression> _;")), equalTo("xs"));
  }

  @Test public void shortNameInfrastructure() {
    azzert.that(spartan.shorten(t("int _;")), equalTo("i"));
  }

  @Test public void shortNameQualifiedType() {
    azzert.that(spartan.shorten(t("org.eclipse.jdt.core.dom.InfixExpression _;")), equalTo("x"));
  }
}
