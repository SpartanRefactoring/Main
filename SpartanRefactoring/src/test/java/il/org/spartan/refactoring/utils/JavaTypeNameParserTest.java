package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

@SuppressWarnings({ "javadoc", "static-method" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class JavaTypeNameParserTest {
  @Test public void alphaNumericMid() {
    that(new JavaTypeNameParser("Base64Parser").shortName(), is("p"));
  }
  @Test public void alphaNumericPost() {
    that(new JavaTypeNameParser("Int32").shortName(), is("i"));
  }
  @Test public void ast() {
    that(new JavaTypeNameParser("AST").shortName(), is("t"));
  }
  @Test public void astNode() {
    that(new JavaTypeNameParser("ASTNode").shortName(), is("n"));
  }
  @Test public void compilationUnit() {
    that(new JavaTypeNameParser("CompilationUnit").shortName(), is("u"));
  }
  @Test public void httpSecureConnection() {
    that(new JavaTypeNameParser("HTTPSecureConnection").shortName(), is("c"));
  }
  @Test public void iCompilationUnit() {
    that(new JavaTypeNameParser("ICompilationUnit").shortName(), is("u"));
  }
  @Test public void infixExpression() {
    that(new JavaTypeNameParser("InfixExpression").shortName(), is("e"));
  }
  @Test public void jUnit() {
    that(new JavaTypeNameParser("JUnit").shortName(), is("u"));
  }
  @Test public void onlyLowerCase() {
    that(new JavaTypeNameParser("onlylowercase").shortName(), is("o"));
  }
  @Test public void onlyUpperCase() {
    that(new JavaTypeNameParser("ONLYUPPERCASE").shortName(), is("e"));
  }
  @Test public void singleChar() {
    that(new JavaTypeNameParser("Q").shortName(), is("q"));
  }
  @Test public void stringBuilder() {
    that(new JavaTypeNameParser("StringBuilder").shortName(), is("b"));
  }
}
