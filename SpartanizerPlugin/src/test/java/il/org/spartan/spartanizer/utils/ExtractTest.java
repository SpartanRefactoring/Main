package il.org.spartan.spartanizer.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.spartanizations.*;

@SuppressWarnings({ "static-method", "javadoc" }) public final class ExtractTest {
  @Test public void core() {
    azzert.isNull(extract.core((Expression) null));
    azzert.isNull(extract.core((Statement) null));
  }

  @Test public void firstMethdoDeclaration() {
    azzert.that(findFirst.firstMethodDeclaration(Wrap.Method.intoCompilationUnit("int f() { return a; }")), iz("int f() { return a; }"));
  }

  @Test public void operandsCount() {
    azzert.that(hop.operands(i("a+b+c+(d+e)+f")).size(), is(5));
  }

  @Test public void operandsOfNullIsNull() {
    azzert.that(hop.operands(null), is(nullValue()));
  }

  @Test public void plus() {
    azzert.that(findFirst.firstPlus(into.e("a + 2 < b")), iz("a+2"));
  }

  @Test public void prefixToPostfixDecrement() {
    final String from = "for (int i = 0; i < 100;  i--)  i--;";
    final Statement s = s(from);
    assert s != null;
    azzert.that(s, iz("{" + from + "}"));
    final PostfixExpression e = findFirst.postfixExpression(s);
    assert e != null;
    azzert.that(e + "", is("i--"));
  }
}
