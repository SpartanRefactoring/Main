package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import il.org.spartan.refactoring.utils.JavaTypeNameParser;

@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class JavaTypeNameParserTest {
  @Test public void alphaNumericMid() {
    assertThat(new JavaTypeNameParser("Base64Parser").shortName(), is("p"));
  }
  @Test public void alphaNumericPost() {
    assertThat(new JavaTypeNameParser("Int32").shortName(), is("i"));
  }
  @Test public void ast() {
    assertThat(new JavaTypeNameParser("AST").shortName(), is("t"));
  }
  @Test public void astNode() {
    assertThat(new JavaTypeNameParser("ASTNode").shortName(), is("n"));
  }
  @Test public void compilationUnit() {
    assertThat(new JavaTypeNameParser("CompilationUnit").shortName(), is("u"));
  }
  @Test public void httpSecureConnection() {
    assertThat(new JavaTypeNameParser("HTTPSecureConnection").shortName(), is("c"));
  }
  @Test public void iCompilationUnit() {
    assertThat(new JavaTypeNameParser("ICompilationUnit").shortName(), is("u"));
  }
  @Test public void infixExpression() {
    assertThat(new JavaTypeNameParser("InfixExpression").shortName(), is("e"));
  }
  @Test public void jUnit() {
    assertThat(new JavaTypeNameParser("JUnit").shortName(), is("u"));
  }
  @Test public void onlyLowerCase() {
    assertThat(new JavaTypeNameParser("onlylowercase").shortName(), is("o"));
  }
  @Test public void onlyUpperCase() {
    assertThat(new JavaTypeNameParser("ONLYUPPERCASE").shortName(), is("e"));
  }
  @Test public void singleChar() {
    assertThat(new JavaTypeNameParser("Q").shortName(), is("q"));
  }
  @Test public void stringBuilder() {
    assertThat(new JavaTypeNameParser("StringBuilder").shortName(), is("b"));
  }
}
