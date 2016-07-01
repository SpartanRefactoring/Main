package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/**
 * @author Yossi Gil
 * @since 2015-07-17
 */
@SuppressWarnings({ "static-method", "javadoc" }) public class PrecedenceTest {
  @Test public void addition() {
    that(Precedence.of(e("a+b")), is(5));
    that(Precedence.of(e("a-b")), is(5));
  }
  @Test public void and() {
    that(Precedence.of(e("a&b")), is(9));
  }
  @Test public void arrayAccess() {
    that(Precedence.of(e("a[i]")), is(1));
  }
  @Test public void arrayCreation() {
    that(Precedence.of(e("new B[]")), is(3));
  }
  @Test public void assignment() {
    that(Precedence.of(a("a=b")), is(15));
    that(Precedence.of(a("a +=b")), is(15));
    that(Precedence.of(a("a-=b")), is(15));
    that(Precedence.of(a("a*= b")), is(15));
    that(Precedence.of(a("a/=b")), is(15));
    that(Precedence.of(a("a%=b")), is(15));
    that(Precedence.of(a("a&=b")), is(15));
    that(Precedence.of(a("a^=b")), is(15));
    that(Precedence.of(a("a|=b")), is(15));
    that(Precedence.of(a("a<<=b")), is(15));
    that(Precedence.of(a("a>>=b")), is(15));
    that(Precedence.of(a("a>>>=b")), is(15));
  }
  @Test public void castExpression() {
    that(Precedence.of(e("(Object) a")), is(3));
  }
  @Test public void conditional_and() {
    that(Precedence.of(e("a&&b")), is(12));
  }
  @Test public void conditional_or() {
    that(Precedence.of(e("a||b")), is(13));
  }
  @Test public void equality() {
    that(Precedence.of(e("a==b")), is(8));
    that(Precedence.of(e("a!=b")), is(8));
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
    that(e("this.f"), instanceOf(FieldAccess.class));
    that(Precedence.of(e("this.f")), is(1));
  }
  @Test public void instanceofOperator() {
    that(Precedence.of(e("a instanceof b")), is(7));
  }
  @Test public void methodAccess() {
    that(Precedence.of(e("t.f()")), is(1));
  }
  @Test public void methodInvocation() {
    that(Precedence.of(e("a()")), is(1));
  }
  @Test public void methodInvocationIsNotNegative() {
    that(Precedence.of(e("f(a,b,c)")), greaterThanOrEqualTo(0));
  }
  @Test public void methodInvocationIsNotTernary() {
    that(Precedence.of(e("f(a,b,c)")), not(comparesEqualTo(Precedence.of(e("a?b:c")))));
  }
  @Test public void multiplication() {
    that(Precedence.of(e("a*b")), is(4));
    that(Precedence.of(e("a/b")), is(4));
    that(Precedence.of(e("a%b")), is(4));
  }
  @Test public void objectCreation() {
    that(Precedence.of(e("new B(a)")), is(3));
  }
  @Test public void or() {
    that(Precedence.of(e("a|b")), is(11));
  }
  @Test public void postfix() {
    that(Precedence.of(e("a++")), is(1));
    that(Precedence.of(e("a--")), is(1));
  }
  @Test public void precedenceOfNulGreatherl() {
    that(Precedence.greater(null, c("a?b:c")), is(true));
  }
  @Test public void precedenceOfNull() {
    that(Precedence.of((Expression) null), is(Precedence.UNDEFINED));
  }
  @Test public void prefix() {
    that(Precedence.of(e("++a")), is(2));
    that(Precedence.of(e("--a")), is(2));
    that(Precedence.of(e("+a")), is(2));
    that(Precedence.of(e("-a")), is(2));
    that(Precedence.of(e("!a")), is(2));
    that(Precedence.of(e("~a")), is(2));
  }
  @Test public void qualifiedAccess() {
    that(Precedence.of(e("a.f")), is(1));
  }
  @Test public void realtional() {
    that(Precedence.of(e("a>b")), is(7));
    that(Precedence.of(e("a<b")), is(7));
    that(Precedence.of(e("a>=b")), is(7));
    that(Precedence.of(e("a<=b")), is(7));
  }
  @Test public void shift() {
    that(Precedence.of(e("a>>b")), is(6));
    that(Precedence.of(e("a<<b")), is(6));
    that(Precedence.of(e("a>>>b")), is(6));
  }
  @Test public void ternary() {
    that(Precedence.of(c("a?b:c")), is(14));
  }
  @Test public void ternaryIsNotNegative() {
    that(Precedence.of(c("A?b:c")), greaterThanOrEqualTo(0));
  }
  @Test public void xor() {
    that(Precedence.of(e("a^b")), is(10));
  }
}
