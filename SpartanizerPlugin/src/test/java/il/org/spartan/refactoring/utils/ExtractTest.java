package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.spartanizations.*;

@SuppressWarnings({ "static-method", "javadoc" }) public class ExtractTest {
  @Test public void core() {
    azzert.isNull(extract.core((Expression) null));
    azzert.isNull(extract.core((Statement) null));
  }

  @Test public void firstMethdoDeclaration() {
    azzert.that(extract.firstMethodDeclaration(Wrap.Method.intoCompilationUnit("int f() { return a; }")), iz("int f() { return a; }"));
  }

  @Test public void operandsCount() {
    azzert.that(hop.operands(i("a+b+c+(d+e)+f")).size(), is(5));
  }

  @Test public void operandsOfNullIsNull() {
    azzert.that(hop.operands(null), is(nullValue()));
  }

  @Test public void plus() {
    azzert.that(extract.firstPlus(into.e("a + 2 < b")), iz("a+2"));
  }

  @Test public void prefixToPostfixDecrement() {
    final String from = "for (int i = 0; i < 100;  i--)  i--;";
    final Statement s = s(from);
    assert s != null;
    azzert.that(s, iz("{" + from + "}"));
    final PostfixExpression e = extract.findFirstPostfix(s);
    assert e != null;
    azzert.that("" + e, is("i--"));
  }
}
