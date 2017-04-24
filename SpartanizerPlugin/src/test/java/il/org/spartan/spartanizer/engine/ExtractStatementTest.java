package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;
import static org.hamcrest.collection.IsEmptyCollection.*;

import org.junit.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** TODO Yossi Gil Document Class
 * @author Yossi Gil
 * @since Sep 7, 2016 */
@SuppressWarnings({ "javadoc", "static-method" })
public final class ExtractStatementTest {
  @Test public void declarationCorrectSize() {
    azzert.that(extract.statements(s("{int a; a();}")).size(), is(2));
  }

  @Test public void declarationIsNotEmpty() {
    azzert.that(extract.statements(s("{int a; a();}")), not(empty()));
  }

  @Test public void deeplyNestedOneInCurlyIsNotEmpty() {
    azzert.that(extract.statements(s("{{{{a();}}}}")), not(empty()));
  }

  @Test public void emptyBlockIsEmpty() {
    azzert.that(extract.statements(s("{}")), empty());
  }

  @Test public void emptyStatementInBlockIsEmpty() {
    azzert.that(extract.statements(s("{;}")), empty());
  }

  @Test public void emptyStatementIsEmpty() {
    azzert.that(extract.statements(s(";")), empty());
  }

  @Test public void fiveIsCorrectSize() {
    azzert.that(extract.statements(s("{{a();b();}{a(); b(); {}{}{{}} c();}}")).size(), is(5));
  }

  @Test public void isEmptyOfNull() {
    azzert.that(extract.statements(null), empty());
  }

  @Test public void isNotNullOfNull() {
    assert extract.statements(null) != null;
  }

  @Test public void isNotNullOfValidStatement() {
    assert extract.statements(s("{}")) != null;
  }

  @Test public void manyEmptyStatementInBlockIsEmpty() {
    azzert.that(extract.statements(s("{;};{;;{;;}};")), empty());
  }

  @Test public void manyIsNotEmpty() {
    azzert.that(extract.statements(s("a(); b(); c();")), not(empty()));
  }

  @Test public void nestedTwoIsCorrectSize() {
    azzert.that(extract.statements(s("{a();b();}")).size(), is(2));
  }

  @Test public void oneInCurlyIsNotEmpty() {
    azzert.that(extract.statements(s("{a();}")), not(empty()));
  }

  @Test public void oneIsNotEmpty() {
    azzert.that(extract.statements(s("{a();}")), not(empty()));
  }

  @Test public void twoFunctionCallsIsCorrectSize() {
    azzert.that(extract.statements(s("{b(); a();}")).size(), is(2));
  }

  @Test public void twoInCurlyIsNotEmpty() {
    azzert.that(extract.statements(s("{a();b();}")), not(empty()));
  }

  @Test public void twoIsCorrectSize() {
    azzert.that(extract.statements(s("a();b();")).size(), is(2));
  }

  @Test public void twoIsNotEmpty() {
    azzert.that(extract.statements(s("a();b();")), not(empty()));
  }
}
