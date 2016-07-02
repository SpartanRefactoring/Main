package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static il.org.spartan.refactoring.utils.Restructure.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

@SuppressWarnings({ "javadoc", "static-method" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class SingletStatementTest {
  @Test public void declarationAndStatementIsNull() {
    that(singleStatement(s("{int a; a();}")), nullValue());
  }
  @Test public void deeplyNestedOneInCurlyIsNull() {
    that(singleStatement(s("{{{{a();}}}}")), not(nullValue()));
  }
  @Test public void emptyBlockIsNull() {
    that(singleStatement(s("{}")), nullValue());
  }
  @Test public void emptyStatementInBlockIsNull() {
    that(singleStatement(s("{;}")), nullValue());
  }
  @Test public void emptyStatementIsNull() {
    that(singleStatement(s(";")), nullValue());
  }
  @Test public void fiveIsCorrectSize() {
    that(singleStatement(s("{{a();b();}{a(); b(); {}{}{{}} c();}}")), nullValue());
  }
  @Test public void manyEmptyStatementInBlockIsNull() {
    that(singleStatement(s("{;};{;;{;;}};")), nullValue());
  }
  @Test public void manyIsNull() {
    that(singleStatement(s("a(); b(); c();")), nullValue());
  }
  @Test public void nestedTwoIsCorrectSize() {
    that(singleStatement(s("{a();b();}")), nullValue());
  }
  @Test public void nullGivesNull() {
    that(singleStatement(null), nullValue());
  }
  @Test public void oneInCurlyIsNotNull() {
    that(singleStatement(s("{a();}")), notNullValue());
  }
  @Test public void oneIsNotNull() {
    that(singleStatement(s("{a();}")), notNullValue());
  }
  @Test public void peelIf() {
    final ASTNode n = ast.STATEMENTS.ast("{if (a) return b; else return c;}");
    that(n, notNullValue());
    final List<Statement> ss = extract.statements(n);
    that(ss, notNullValue());
    that(ss.size(), is(1));
    that(extract.singleStatement(n), notNullValue());
  }
  @Test public void peelIPlusPlus() {
    final ASTNode n = ast.STATEMENTS.ast("{i++;}");
    that(n, notNullValue());
    final List<Statement> ss = extract.statements(n);
    that(ss, notNullValue());
    that(ss.size(), is(1));
    that(extract.singleStatement(n), notNullValue());
  }
  @Test public void twoFunctionCallsNullValue() {
    that(singleStatement(s("{b(); a();}")), nullValue());
  }
  @Test public void twoInCurlyIsNull() {
    that(singleStatement(s("{a();b();}")), nullValue());
  }
  @Test public void twoNullValue() {
    that(singleStatement(s("a();b();")), nullValue());
  }
}
