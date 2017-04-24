package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import org.junit.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.nominal.*;

@SuppressWarnings({ "javadoc", "static-method" })
public final class JavaTypeNameParserTest {
  @Test public void Alex_and_Dan_test() {
    azzert.that(JavaTypeNameParser.make("Alex_and_Dan").shortName(), is("d"));
  }

  @Test public void alphaNumericMid() {
    azzert.that(JavaTypeNameParser.make("Base64Parser").shortName(), is("p"));
  }

  @Test public void alphaNumericPost() {
    azzert.that(JavaTypeNameParser.make("Int32").shortName(), is("i"));
  }

  @Test public void ast() {
    azzert.that(JavaTypeNameParser.make("AST").shortName(), is("t"));
  }

  @Test public void astNode() {
    azzert.that(JavaTypeNameParser.make("ASTNode").shortName(), is("n"));
  }

  @Test public void compilationUnit() {
    azzert.that(JavaTypeNameParser.make("CompilationUnit").shortName(), is("u"));
  }

  @Test public void httpSecureConnection() {
    azzert.that(JavaTypeNameParser.make("HTTPSecureConnection").shortName(), is("c"));
  }

  @Test public void iCompilationUnit() {
    azzert.that(JavaTypeNameParser.make("ICompilationUnit").shortName(), is("u"));
  }

  @Test public void infixExpression() {
    azzert.that(JavaTypeNameParser.make("InfixExpression").shortName(), is("x"));
  }

  @Test public void johnDoe01() {
    assert JohnDoe.property("Type", "t");
  }

  @Test public void johnDoe02() {
    assert JohnDoe.property("VariableDeclarationStatement", "s");
  }

  @Test public void johnDoe03() {
    assert !JohnDoe.property("VariableDeclarationStatement", "x");
  }

  @Test public void johnDoe04() {
    assert !JohnDoe.property("String", "word");
  }

  @Test public void johnDoe05() {
    assert JohnDoe.property("String", "s");
  }

  @Test public void jUnit() {
    azzert.that(JavaTypeNameParser.make("JUnit").shortName(), is("u"));
  }

  @Test public void onlyLowerCase() {
    azzert.that(JavaTypeNameParser.make("onlylowercase").shortName(), is("o"));
  }

  @Test public void onlyUpperCase() {
    azzert.that(JavaTypeNameParser.make("ONLYUPPERCASE").shortName(), is("e"));
  }

  @Test public void singleChar() {
    azzert.that(JavaTypeNameParser.make("Q").shortName(), is("q"));
  }

  @Test public void some_name_an_electrical_engineer_can_give() {
    azzert.that(JavaTypeNameParser.make("very_low_voltage").shortName(), is("v"));
  }

  @Test public void stringBuilder() {
    azzert.that(JavaTypeNameParser.make("StringBuilder").shortName(), is("b"));
  }
}
