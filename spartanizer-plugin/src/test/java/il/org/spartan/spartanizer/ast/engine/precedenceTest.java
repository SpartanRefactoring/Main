package il.org.spartan.spartanizer.ast.engine;

import static fluent.ly.azzert.*;
import static il.org.spartan.spartanizer.engine.parse.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.java.*;

/** TODO Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2015-07-17 */
@SuppressWarnings({ "static-method", "javadoc" })
public final class precedenceTest {
  @Test public void addition() {
    azzert.that(precedence.of(e("a+b")), is(5));
    azzert.that(precedence.of(e("a-b")), is(5));
  }
  @Test public void and() {
    azzert.that(precedence.of(e("a&b")), is(9));
  }
  @Test public void arrayAccess() {
    azzert.that(precedence.of(e("a[i]")), is(1));
  }
  @Test public void arrayCreation() {
    azzert.that(precedence.of(e("new B[]")), is(3));
  }
  @Test public void assignment() {
    azzert.that(precedence.of(a("a=b")), is(15));
    azzert.that(precedence.of(a("a +=b")), is(15));
    azzert.that(precedence.of(a("a-=b")), is(15));
    azzert.that(precedence.of(a("a*= b")), is(15));
    azzert.that(precedence.of(a("a/=b")), is(15));
    azzert.that(precedence.of(a("a%=b")), is(15));
    azzert.that(precedence.of(a("a&=b")), is(15));
    azzert.that(precedence.of(a("a^=b")), is(15));
    azzert.that(precedence.of(a("a|=b")), is(15));
    azzert.that(precedence.of(a("a<<=b")), is(15));
    azzert.that(precedence.of(a("a>>=b")), is(15));
    azzert.that(precedence.of(a("a>>>=b")), is(15));
  }
  @Test public void castExpression() {
    azzert.that(precedence.of(e("(Object) a")), is(3));
  }
  @Test public void conditional_and() {
    azzert.that(precedence.of(e("a&&b")), is(12));
  }
  @Test public void conditional_or() {
    azzert.that(precedence.of(e("a||b")), is(13));
  }
  @Test public void equality() {
    azzert.that(precedence.of(e("a==b")), is(8));
    azzert.that(precedence.of(e("a!=b")), is(8));
  }
  @Test public void exists() {
    precedence.of(e("A+3"));
  }
  @Test public void existsPrefix() {
    precedence.of(p("!a"));
  }
  @Test public void existsTernary() {
    precedence.of(c("A?b:c"));
  }
  @Test public void fieldAccess() {
    azzert.that(e("this.f"), instanceOf(FieldAccess.class));
    azzert.that(precedence.of(e("this.f")), is(1));
  }
  @Test public void instanceofOperator() {
    azzert.that(precedence.of(e("a instanceof b")), is(7));
  }
  @Test public void methodAccess() {
    azzert.that(precedence.of(e("tipper.f()")), is(1));
  }
  @Test public void methodInvocation() {
    azzert.that(precedence.of(e("a()")), is(1));
  }
  @Test public void methodInvocationIsNotNegative() {
    azzert.that(precedence.of(e("f(a,b,c)")), greaterThanOrEqualTo(0));
  }
  @Test public void methodInvocationIsNotTernary() {
    azzert.that(precedence.of(e("f(a,b,c)")), not(comparesEqualTo(precedence.of(e("a?b:c")))));
  }
  @Test public void multiplication() {
    azzert.that(precedence.of(e("a*b")), is(4));
    azzert.that(precedence.of(e("a/b")), is(4));
    azzert.that(precedence.of(e("a%b")), is(4));
  }
  @Test public void nonAssociative() {
    assert !op.nonAssociative(e("1"));
    assert !op.nonAssociative(e("-1"));
    assert !op.nonAssociative(e("-1+2"));
    assert !op.nonAssociative(e("1+2"));
    assert op.nonAssociative(e("2-1"));
    assert op.nonAssociative(e("2/1"));
    assert op.nonAssociative(e("2%1"));
    assert !op.nonAssociative(e("2*1"));
  }
  @Test public void objectCreation() {
    azzert.that(precedence.of(e("new B(a)")), is(3));
  }
  @Test public void or() {
    azzert.that(precedence.of(e("a|b")), is(11));
  }
  @Test public void postfix() {
    azzert.that(precedence.of(e("a++")), is(1));
    azzert.that(precedence.of(e("a--")), is(1));
  }
  @Test public void precedenceOfNulGreatherl() {
    assert precedence.greater(null, c("a?b:c"));
  }
  @Test public void precedenceOfNull() {
    azzert.that(precedence.of((Expression) null), is(precedence.UNDEFINED));
  }
  @Test public void prefix() {
    azzert.that(precedence.of(e("++a")), is(2));
    azzert.that(precedence.of(e("--a")), is(2));
    azzert.that(precedence.of(e("+a")), is(2));
    azzert.that(precedence.of(e("-a")), is(2));
    azzert.that(precedence.of(e("!a")), is(2));
    azzert.that(precedence.of(e("~a")), is(2));
  }
  @Test public void qualifiedAccess() {
    azzert.that(precedence.of(e("a.f")), is(1));
  }
  @Test public void realtional() {
    azzert.that(precedence.of(e("a>b")), is(7));
    azzert.that(precedence.of(e("a<b")), is(7));
    azzert.that(precedence.of(e("a>=b")), is(7));
    azzert.that(precedence.of(e("a<=b")), is(7));
  }
  @Test public void shift() {
    azzert.that(precedence.of(e("a>>b")), is(6));
    azzert.that(precedence.of(e("a<<b")), is(6));
    azzert.that(precedence.of(e("a>>>b")), is(6));
  }
  @Test public void ternary() {
    azzert.that(precedence.of(c("a?b:c")), is(14));
  }
  @Test public void ternaryIsNotNegative() {
    azzert.that(precedence.of(c("A?b:c")), greaterThanOrEqualTo(0));
  }
  @Test public void xor() {
    azzert.that(precedence.of(e("a^b")), is(10));
  }
}
