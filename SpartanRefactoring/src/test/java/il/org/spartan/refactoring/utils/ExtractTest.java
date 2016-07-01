package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;
import il.org.spartan.refactoring.spartanizations.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

@SuppressWarnings({ "static-method", "javadoc" }) public class ExtractTest {
  @Test public void core() {
    that(Extract.core((Expression) null), nullValue());
    that(Extract.core((Statement) null), nullValue());
  }
  @Test public void firstMethdoDeclaration() {
    that(Extract.firstMethodDeclaration(Wrap.Method.intoCompilationUnit("int f() { return a; }")), iz("int f() { return a; }"));
  }
  @Test public void operandsCount() {
    that(Extract.operands(i("a+b+c+(d+e)+f")).size(), is(5));
  }
  @Test public void operandsOfNullIsNull() {
    that(Extract.operands(null), is(nullValue()));
  }
  @Test public void plus() {
    that(Extract.firstPlus(Into.e("a + 2 < b")), iz("a+2"));
  }
  @Test public void prefixToPostfixDecrement() {
    final String from = "for (int i = 0; i < 100;  i--)  i--;";
    final Statement s = s(from);
    that(s, iz("{" + from + "}"));
    that(s, notNullValue());
    final PostfixExpression e = Extract.findFirstPostfix(s);
    that(e, notNullValue());
    that(e.toString(), is("i--"));
  }
}
