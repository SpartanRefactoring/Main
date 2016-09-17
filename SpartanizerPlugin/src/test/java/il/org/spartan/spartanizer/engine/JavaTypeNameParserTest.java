package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.JavaTypeNameParser.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public final class JavaTypeNameParserTest {
  // some upper, some lower case characters - the upper are more representative.
  @Test public void Alex_and_Dan_test() {
    azzert.that(new JavaTypeNameParser("Alex_and_Dan").shortName(), is("d"));
  }

  @Test public void alphaNumericMid() {
    azzert.that(new JavaTypeNameParser("Base64Parser").shortName(), is("p"));
  }

  @Test public void alphaNumericPost() {
    azzert.that(new JavaTypeNameParser("Int32").shortName(), is("i"));
  }

  @Test public void ast() {
    azzert.that(new JavaTypeNameParser("AST").shortName(), is("t"));
  }

  @Test public void astNode() {
    azzert.that(new JavaTypeNameParser("ASTNode").shortName(), is("n"));
  }

  @Test public void compilationUnit() {
    azzert.that(new JavaTypeNameParser("CompilationUnit").shortName(), is("u"));
  }

  @Test public void httpSecureConnection() {
    azzert.that(new JavaTypeNameParser("HTTPSecureConnection").shortName(), is("c"));
  }

  @Test public void iCompilationUnit() {
    azzert.that(new JavaTypeNameParser("ICompilationUnit").shortName(), is("u"));
  }

  @Test public void infixExpression() {
    azzert.that(new JavaTypeNameParser("InfixExpression").shortName(), is("x"));
  }

  @Test public void johnDoe01() {
    assert isJohnDoe("Type", "t");
  }

  @Test public void johnDoe02() {
    assert isJohnDoe("VariableDeclarationStatement", "s");
  }

  @Test public void johnDoe03() {
    assert !isJohnDoe("VariableDeclarationStatement", "x");
  }

  @Test public void johnDoe04() {
    assert !isJohnDoe("String", "word");
  }

  @Test public void johnDoe05() {
    assert isJohnDoe("String", "s");
  }

  @Test public void jUnit() {
    azzert.that(new JavaTypeNameParser("JUnit").shortName(), is("u"));
  }

  @Test public void onlyLowerCase() {
    azzert.that(new JavaTypeNameParser("onlylowercase").shortName(), is("o"));
  }

  @Test public void onlyUpperCase() {
    azzert.that(new JavaTypeNameParser("ONLYUPPERCASE").shortName(), is("e"));
  }

  @Test public void singleChar() {
    azzert.that(new JavaTypeNameParser("Q").shortName(), is("q"));
  }

  // all lower case characters - not sure how you want to shorten it.
  @Test public void some_name_an_electrical_engineer_can_give() {
    azzert.that(new JavaTypeNameParser("very_low_voltage").shortName(), is("v"));
  }

  @Test public void stringBuilder() {
    azzert.that(new JavaTypeNameParser("StringBuilder").shortName(), is("b"));
  }
}
