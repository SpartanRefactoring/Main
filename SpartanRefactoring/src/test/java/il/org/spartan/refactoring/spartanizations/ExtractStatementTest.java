package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static org.hamcrest.collection.IsEmptyCollection.*;
import il.org.spartan.refactoring.utils.*;

import org.junit.*;
import org.junit.runners.*;

@SuppressWarnings({ "javadoc", "static-method" })//
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//
public class ExtractStatementTest {
  @Test public void declarationCorrectSize() {
    that(extract.statements(s("{int a; a();}")).size(), is(2));
  }
  @Test public void declarationIsNotEmpty() {
    that(extract.statements(s("{int a; a();}")), not(empty()));
  }
  @Test public void deeplyNestedOneInCurlyIsNotEmpty() {
    that(extract.statements(s("{{{{a();}}}}")), not(empty()));
  }
  @Test public void emptyBlockIsEmpty() {
    that(extract.statements(s("{}")), empty());
  }
  @Test public void emptyStatementInBlockIsEmpty() {
    that(extract.statements(s("{;}")), empty());
  }
  @Test public void emptyStatementIsEmpty() {
    that(extract.statements(s(";")), empty());
  }
  @Test public void fiveIsCorrectSize() {
    that(extract.statements(s("{{a();b();}{a(); b(); {}{}{{}} c();}}")).size(), is(5));
  }
  @Test public void isEmptyOfNull() {
    that(extract.statements(null), empty());
  }
  @Test public void isNotNullOfNull() {
    that(extract.statements(null), is(notNullValue()));
  }
  @Test public void isNotNullOfValidStatement() {
    that(extract.statements(s("{}")), is(notNullValue()));
  }
  @Test public void manyEmptyStatementInBlockIsEmpty() {
    that(extract.statements(s("{;};{;;{;;}};")), empty());
  }
  @Test public void manyIsNotEmpty() {
    that(extract.statements(s("a(); b(); c();")), not(empty()));
  }
  @Test public void nestedTwoIsCorrectSize() {
    that(extract.statements(s("{a();b();}")).size(), is(2));
  }
  @Test public void oneInCurlyIsNotEmpty() {
    that(extract.statements(s("{a();}")), not(empty()));
  }
  @Test public void oneIsNotEmpty() {
    that(extract.statements(s("{a();}")), not(empty()));
  }
  @Test public void twoFunctionCallsIsCorrectSize() {
    that(extract.statements(s("{b(); a();}")).size(), is(2));
  }
  @Test public void twoInCurlyIsNotEmpty() {
    that(extract.statements(s("{a();b();}")), not(empty()));
  }
  @Test public void twoIsCorrectSize() {
    that(extract.statements(s("a();b();")).size(), is(2));
  }
  @Test public void twoIsNotEmpty() {
    that(extract.statements(s("a();b();")), not(empty()));
  }
}
