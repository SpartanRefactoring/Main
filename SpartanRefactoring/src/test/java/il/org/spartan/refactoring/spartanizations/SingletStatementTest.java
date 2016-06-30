package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.hamcrest.SpartanAssert.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import static org.junit.Assert.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

@SuppressWarnings({ "javadoc", "static-method" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class SingletStatementTest {
  @Test public void declarationAndStatementIsNull() {
    assertThat(singleStatement(s("{int a; a();}")), nullValue());
  }
  @Test public void deeplyNestedOneInCurlyIsNull() {
    assertThat(singleStatement(s("{{{{a();}}}}")), not(nullValue()));
  }
  @Test public void emptyBlockIsNull() {
    assertThat(singleStatement(s("{}")), nullValue());
  }
  @Test public void emptyStatementInBlockIsNull() {
    assertThat(singleStatement(s("{;}")), nullValue());
  }
  @Test public void emptyStatementIsNull() {
    assertThat(singleStatement(s(";")), nullValue());
  }
  @Test public void fiveIsCorrectSize() {
    assertThat(singleStatement(s("{{a();b();}{a(); b(); {}{}{{}} c();}}")), nullValue());
  }
  @Test public void manyEmptyStatementInBlockIsNull() {
    assertThat(singleStatement(s("{;};{;;{;;}};")), nullValue());
  }
  @Test public void manyIsNull() {
    assertThat(singleStatement(s("a(); b(); c();")), nullValue());
  }
  @Test public void nestedTwoIsCorrectSize() {
    assertThat(singleStatement(s("{a();b();}")), nullValue());
  }
  @Test public void nullGivesNull() {
    assertThat(singleStatement(null), nullValue());
  }
  @Test public void oneInCurlyIsNotNull() {
    assertThat(singleStatement(s("{a();}")), notNullValue());
  }
  @Test public void oneIsNotNull() {
    assertThat(singleStatement(s("{a();}")), notNullValue());
  }
  @Test public void peelIf() {
    final ASTNode n = As.STATEMENTS.ast("{if (a) return b; else return c;}");
    assertThat(n, notNullValue());
    final List<Statement> ss = Extract.statements(n);
    assertThat(ss, notNullValue());
    assertThat(ss.size(), is(1));
    assertThat(Extract.singleStatement(n), notNullValue());
  }
  @Test public void peelIPlusPlus() {
    final ASTNode n = As.STATEMENTS.ast("{i++;}");
    assertThat(n, notNullValue());
    final List<Statement> ss = Extract.statements(n);
    assertThat(ss, notNullValue());
    assertThat(ss.size(), is(1));
    assertThat(Extract.singleStatement(n), notNullValue());
  }
  @Test public void twoFunctionCallsNullValue() {
    assertThat(singleStatement(s("{b(); a();}")), nullValue());
  }
  @Test public void twoInCurlyIsNull() {
    assertThat(singleStatement(s("{a();b();}")), nullValue());
  }
  @Test public void twoNullValue() {
    assertThat(singleStatement(s("a();b();")), nullValue());
  }
}
