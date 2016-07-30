package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.refactoring.utils.Into.s;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import il.org.spartan.refactoring.utils.Extract;

@SuppressWarnings({ "javadoc", "static-method" }) //
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
public class ExtractStatementTest {
  @Test public void declarationCorrectSize() {
    assertThat(Extract.statements(s("{int a; a();}")).size(), is(2));
  }
  @Test public void declarationIsNotEmpty() {
    assertThat(Extract.statements(s("{int a; a();}")), not(empty()));
  }
  @Test public void deeplyNestedOneInCurlyIsNotEmpty() {
    assertThat(Extract.statements(s("{{{{a();}}}}")), not(empty()));
  }
  @Test public void emptyBlockIsEmpty() {
    assertThat(Extract.statements(s("{}")), empty());
  }
  @Test public void emptyStatementInBlockIsEmpty() {
    assertThat(Extract.statements(s("{}")), empty());
  }
  @Test public void emptyStatementIsEmpty() {
    assertThat(Extract.statements(s(";")), empty());
  }
  @Test public void fiveIsCorrectSize() {
    assertThat(Extract.statements(s("{{a();b();}{a(); b(); {}{}{{}} c();}}")).size(), is(5));
  }
  @Test public void isEmptyOfNull() {
    assertThat(Extract.statements(null), empty());
  }
  @Test public void isNotNullOfNull() {
    assertThat(Extract.statements(null), is(notNullValue()));
  }
  @Test public void isNotNullOfValidStatement() {
    assertThat(Extract.statements(s("{}")), is(notNullValue()));
  }
  @Test public void manyEmptyStatementInBlockIsEmpty() {
    assertThat(Extract.statements(s("{};{;;{;;}};")), empty());
  }
  @Test public void manyIsNotEmpty() {
    assertThat(Extract.statements(s("a(); b(); c();")), not(empty()));
  }
  @Test public void nestedTwoIsCorrectSize() {
    assertThat(Extract.statements(s("{a();b();}")).size(), is(2));
  }
  @Test public void oneInCurlyIsNotEmpty() {
    assertThat(Extract.statements(s("{a();}")), not(empty()));
  }
  @Test public void oneIsNotEmpty() {
    assertThat(Extract.statements(s("{a();}")), not(empty()));
  }
  @Test public void twoFunctionCallsIsCorrectSize() {
    assertThat(Extract.statements(s("{b(); a();}")).size(), is(2));
  }
  @Test public void twoInCurlyIsNotEmpty() {
    assertThat(Extract.statements(s("{a();b();}")), not(empty()));
  }
  @Test public void twoIsCorrectSize() {
    assertThat(Extract.statements(s("a();b();")).size(), is(2));
  }
  @Test public void twoIsNotEmpty() {
    assertThat(Extract.statements(s("a();b();")), not(empty()));
  }
}
