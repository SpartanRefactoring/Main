package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.SpartanAssert.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static org.junit.Assert.*;
import il.org.spartan.refactoring.spartanizations.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

@SuppressWarnings({ "static-method", "javadoc" }) public class ExtractTest {
  @Test public void core() {
    assertThat("", Extract.core((Expression) null), nullValue());
    assertThat("", Extract.core((Statement) null), nullValue());
  }
  @Test public void firstMethdoDeclaration() {
    assertThat(Extract.firstMethodDeclaration(Wrap.Method.intoCompilationUnit("int f() { return a; }")),
        iz("int f() { return a; }"));
  }
  @Test public void operandsCount() {
    assertThat(Extract.operands(i("a+b+c+(d+e)+f")).size(), is(5));
  }
  @Test public void operandsOfNullIsNull() {
    assertThat("", Extract.operands(null), is(nullValue()));
  }
  @Test public void plus() {
    assertThat(Extract.firstPlus(Into.e("a + 2 < b")), iz("a+2"));
  }
  @Test public void prefixToPostfixDecrement() {
    final String from = "for (int i = 0; i < 100;  i--)  i--;";
    final Statement s = s(from);
    assertThat(s, iz("{" + from + "}"));
    assertThat("", s, notNullValue());
    final PostfixExpression e = Extract.findFirstPostfix(s);
    assertThat("", e, notNullValue());
    assertThat("", e.toString(), is("i--"));
  }
}
