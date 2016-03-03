package org.spartan.refactoring.utils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.spartan.hamcrest.CoreMatchers.is;
import static org.spartan.hamcrest.MatcherAssert.assertThat;
import static org.spartan.hamcrest.MatcherAssert.iz;
import static org.spartan.refactoring.utils.Into.i;
import static org.spartan.refactoring.utils.Into.s;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.Statement;
import org.junit.Test;
import org.spartan.refactoring.spartanizations.Wrap;

@SuppressWarnings({ "static-method", "javadoc" }) public class ExtractTest {
  @Test public void core() {
    assertThat(Extract.core((Expression) null), nullValue());
    assertThat(Extract.core((Statement) null), nullValue());
  }
  @Test public void firstMethdoDeclaration() {
    assertThat(Extract.firstMethodDeclaration(Wrap.Method.intoCompilationUnit("int f() { return a; }")), iz("int f() { return a; }"));
  }
  @Test public void operandsCount() {
    assertThat(Extract.operands(i("a+b+c+(d+e)+f")).size(), is(5));
  }
  @Test public void operandsOfNullIsNull() {
    assertThat(Extract.operands(null), is(nullValue()));
  }
  @Test public void plus() {
    assertThat(Extract.firstPlus(Into.e("a + 2 < b")), iz("a+2"));
  }
  @Test public void prefixToPostfixDecrement() {
    final String from = "for (int i = 0; i < 100;  i--)  i--;";
    final Statement s = s(from);
    assertThat(s, iz("{" + from + "}"));
    assertNotNull(s);
    final PostfixExpression e = Extract.findFirstPostfix(s);
    assertNotNull(e);
    assertThat(e.toString(), is("i--"));
  }
}
