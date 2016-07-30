package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.refactoring.utils.Into.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.refactoring.utils.*;

@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class ExtractStatementTest {
  @Test public void declarationCorrectSize() {
    assertThat(extract.statements(s("{int a; a();}")).size(), is(2));
  }
  @Test public void declarationIsNotEmpty() {
    assertThat(extract.statements(s("{int a; a();}")), not(empty()));
  }
  @Test public void deeplyNestedOneInCurlyIsNotEmpty() {
    assertThat(extract.statements(s("{{{{a();}}}}")), not(empty()));
  }
  @Test public void emptyBlockIsEmpty() {
    assertThat(extract.statements(s("{}")), empty());
  }
  @Test public void emptyStatementInBlockIsEmpty() {
    assertThat(extract.statements(s("{;}")), empty());
  }
  @Test public void emptyStatementIsEmpty() {
    assertThat(extract.statements(s(";")), empty());
  }
  @Test public void fiveIsCorrectSize() {
    assertThat(extract.statements(s("{{a();b();}{a(); b(); {}{}{{}} c();}}")).size(), is(5));
  }
  @Test public void isEmptyOfNull() {
    assertThat(extract.statements(null), empty());
  }
  @Test public void isNotNullOfNull() {
    assertThat(extract.statements(null), is(notNullValue()));
  }
  @Test public void isNotNullOfValidStatement() {
    assertThat(extract.statements(s("{}")), is(notNullValue()));
  }
  @Test public void manyEmptyStatementInBlockIsEmpty() {
    assertThat(extract.statements(s("{;};{;;{;;}};")), empty());
  }
  @Test public void manyIsNotEmpty() {
    assertThat(extract.statements(s("a(); b(); c();")), not(empty()));
  }
  @Test public void nestedTwoIsCorrectSize() {
    assertThat(extract.statements(s("{a();b();}")).size(), is(2));
  }
  @Test public void oneInCurlyIsNotEmpty() {
    assertThat(extract.statements(s("{a();}")), not(empty()));
  }
  @Test public void oneIsNotEmpty() {
    assertThat(extract.statements(s("{a();}")), not(empty()));
  }
  @Test public void twoFunctionCallsIsCorrectSize() {
    assertThat(extract.statements(s("{b(); a();}")).size(), is(2));
  }
  @Test public void twoInCurlyIsNotEmpty() {
    assertThat(extract.statements(s("{a();b();}")), not(empty()));
  }
  @Test public void twoIsCorrectSize() {
    assertThat(extract.statements(s("a();b();")).size(), is(2));
  }
  @Test public void twoIsNotEmpty() {
    assertThat(extract.statements(s("a();b();")), not(empty()));
  }
}
