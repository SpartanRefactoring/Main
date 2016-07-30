package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.CoreMatchers.is;
import static il.org.spartan.hamcrest.MatcherAssert.*;
import static il.org.spartan.hamcrest.MatcherAssert.assertThat;
import static il.org.spartan.refactoring.utils.Into.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.refactoring.spartanizations.*;

@SuppressWarnings({ "static-method", "javadoc" }) public class ExtractTest {
  @Test public void core() {
    assertThat(extract.core((Expression) null), nullValue());
    assertThat(extract.core((Statement) null), nullValue());
  }
  @Test public void firstMethdoDeclaration() {
    assertThat(extract.firstMethodDeclaration(Wrap.Method.intoCompilationUnit("int f() { return a; }")),
        iz("int f() { return a; }"));
  }
  @Test public void operandsCount() {
    assertThat(extract.operands(i("a+b+c+(d+e)+f")).size(), is(5));
  }
  @Test public void operandsOfNullIsNull() {
    assertThat(extract.operands(null), is(nullValue()));
  }
  @Test public void plus() {
    assertThat(extract.firstPlus(Into.e("a + 2 < b")), iz("a+2"));
  }
  @Test public void prefixToPostfixDecrement() {
    final String from = "for (int i = 0; i < 100;  i--)  i--;";
    final Statement s = s(from);
    assertThat(s, iz("{" + from + "}"));
    assertNotNull(s);
    final PostfixExpression e = extract.findFirstPostfix(s);
    assertNotNull(e);
    assertThat(e.toString(), is("i--"));
  }
}
