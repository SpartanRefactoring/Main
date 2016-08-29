package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.java.*;

/** @author Yossi Gil
 * @since 2015-07-17 */
@SuppressWarnings({ "static-method", "javadoc" }) public class PrecedenceTest {
  @Test public void addition() {
    azzert.that(Precedence.of(e("a+b")), is(5));
    azzert.that(Precedence.of(e("a-b")), is(5));
  }

  @Test public void and() {
    azzert.that(Precedence.of(e("a&b")), is(9));
  }

  @Test public void arrayAccess() {
    azzert.that(Precedence.of(e("a[i]")), is(1));
  }

  @Test public void arrayCreation() {
    azzert.that(Precedence.of(e("new B[]")), is(3));
  }

  @Test public void assignment() {
    azzert.that(Precedence.of(a("a=b")), is(15));
    azzert.that(Precedence.of(a("a +=b")), is(15));
    azzert.that(Precedence.of(a("a-=b")), is(15));
    azzert.that(Precedence.of(a("a*= b")), is(15));
    azzert.that(Precedence.of(a("a/=b")), is(15));
    azzert.that(Precedence.of(a("a%=b")), is(15));
    azzert.that(Precedence.of(a("a&=b")), is(15));
    azzert.that(Precedence.of(a("a^=b")), is(15));
    azzert.that(Precedence.of(a("a|=b")), is(15));
    azzert.that(Precedence.of(a("a<<=b")), is(15));
    azzert.that(Precedence.of(a("a>>=b")), is(15));
    azzert.that(Precedence.of(a("a>>>=b")), is(15));
  }

  @Test public void castExpression() {
    azzert.that(Precedence.of(e("(Object) a")), is(3));
  }

  @Test public void conditional_and() {
    azzert.that(Precedence.of(e("a&&b")), is(12));
  }

  @Test public void conditional_or() {
    azzert.that(Precedence.of(e("a||b")), is(13));
  }

  @Test public void equality() {
    azzert.that(Precedence.of(e("a==b")), is(8));
    azzert.that(Precedence.of(e("a!=b")), is(8));
  }

  @Test public void exists() {
    Precedence.of(e("A+3"));
  }

  @Test public void existsPrefix() {
    Precedence.of(p("!a"));
  }

  @Test public void existsTernary() {
    Precedence.of(c("A?b:c"));
  }

  @Test public void fieldAccess() {
    azzert.that(e("this.f"), instanceOf(FieldAccess.class));
    azzert.that(Precedence.of(e("this.f")), is(1));
  }

  @Test public void instanceofOperator() {
    azzert.that(Precedence.of(e("a instanceof b")), is(7));
  }

  @Test public void methodAccess() {
    azzert.that(Precedence.of(e("t.f()")), is(1));
  }

  @Test public void methodInvocation() {
    azzert.that(Precedence.of(e("a()")), is(1));
  }

  @Test public void methodInvocationIsNotNegative() {
    azzert.that(Precedence.of(e("f(a,b,c)")), greaterThanOrEqualTo(0));
  }

  @Test public void methodInvocationIsNotTernary() {
    azzert.that(Precedence.of(e("f(a,b,c)")), not(comparesEqualTo(Precedence.of(e("a?b:c")))));
  }

  @Test public void multiplication() {
    azzert.that(Precedence.of(e("a*b")), is(4));
    azzert.that(Precedence.of(e("a/b")), is(4));
    azzert.that(Precedence.of(e("a%b")), is(4));
  }

  @Test public void objectCreation() {
    azzert.that(Precedence.of(e("new B(a)")), is(3));
  }

  @Test public void or() {
    azzert.that(Precedence.of(e("a|b")), is(11));
  }

  @Test public void postfix() {
    azzert.that(Precedence.of(e("a++")), is(1));
    azzert.that(Precedence.of(e("a--")), is(1));
  }

  @Test public void precedenceOfNulGreatherl() {
    azzert.that(Precedence.greater(null, c("a?b:c")), is(true));
  }

  @Test public void precedenceOfNull() {
    azzert.that(Precedence.of((Expression) null), is(Precedence.UNDEFINED));
  }

  @Test public void prefix() {
    azzert.that(Precedence.of(e("++a")), is(2));
    azzert.that(Precedence.of(e("--a")), is(2));
    azzert.that(Precedence.of(e("+a")), is(2));
    azzert.that(Precedence.of(e("-a")), is(2));
    azzert.that(Precedence.of(e("!a")), is(2));
    azzert.that(Precedence.of(e("~a")), is(2));
  }

  @Test public void qualifiedAccess() {
    azzert.that(Precedence.of(e("a.f")), is(1));
  }

  @Test public void realtional() {
    azzert.that(Precedence.of(e("a>b")), is(7));
    azzert.that(Precedence.of(e("a<b")), is(7));
    azzert.that(Precedence.of(e("a>=b")), is(7));
    azzert.that(Precedence.of(e("a<=b")), is(7));
  }

  @Test public void shift() {
    azzert.that(Precedence.of(e("a>>b")), is(6));
    azzert.that(Precedence.of(e("a<<b")), is(6));
    azzert.that(Precedence.of(e("a>>>b")), is(6));
  }

  @Test public void ternary() {
    azzert.that(Precedence.of(c("a?b:c")), is(14));
  }

  @Test public void ternaryIsNotNegative() {
    azzert.that(Precedence.of(c("A?b:c")), greaterThanOrEqualTo(0));
  }

  @Test public void xor() {
    azzert.that(Precedence.of(e("a^b")), is(10));
  }
}
