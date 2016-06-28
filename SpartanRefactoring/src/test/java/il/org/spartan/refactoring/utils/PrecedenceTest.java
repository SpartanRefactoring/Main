package il.org.spartan.refactoring.utils;

import static il.org.spartan.hamcrest.CoreMatchers.*;
import static il.org.spartan.hamcrest.MatcherAssert.*;
import static il.org.spartan.hamcrest.JunitHamcrestWrappper.*;
import static il.org.spartan.refactoring.utils.Into.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/**
 * @author Yossi Gil
 * @since 2015-07-17
 */
@SuppressWarnings({ "static-method", "javadoc" }) public class PrecedenceTest {
  @Test public void addition() {
    assertThat(Precedence.of(e("a+b")), is(5));
    assertThat(Precedence.of(e("a-b")), is(5));
  }
  @Test public void and() {
    assertThat(Precedence.of(e("a&b")), is(9));
  }
  @Test public void arrayAccess() {
    assertThat(Precedence.of(e("a[i]")), is(1));
  }
  @Test public void arrayCreation() {
    assertThat(Precedence.of(e("new B[]")), is(3));
  }
  @Test public void assignment() {
    assertThat(Precedence.of(a("a=b")), is(15));
    assertThat(Precedence.of(a("a +=b")), is(15));
    assertThat(Precedence.of(a("a-=b")), is(15));
    assertThat(Precedence.of(a("a*= b")), is(15));
    assertThat(Precedence.of(a("a/=b")), is(15));
    assertThat(Precedence.of(a("a%=b")), is(15));
    assertThat(Precedence.of(a("a&=b")), is(15));
    assertThat(Precedence.of(a("a^=b")), is(15));
    assertThat(Precedence.of(a("a|=b")), is(15));
    assertThat(Precedence.of(a("a<<=b")), is(15));
    assertThat(Precedence.of(a("a>>=b")), is(15));
    assertThat(Precedence.of(a("a>>>=b")), is(15));
  }
  @Test public void castExpression() {
    assertThat(Precedence.of(e("(Object) a")), is(3));
  }
  @Test public void conditional_and() {
    assertThat(Precedence.of(e("a&&b")), is(12));
  }
  @Test public void conditional_or() {
    assertThat(Precedence.of(e("a||b")), is(13));
  }
  @Test public void equality() {
    assertThat(Precedence.of(e("a==b")), is(8));
    assertThat(Precedence.of(e("a!=b")), is(8));
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
    assertThat(e("this.f"), instanceOf(FieldAccess.class));
    assertThat(Precedence.of(e("this.f")), is(1));
  }
  @Test public void instanceofOperator() {
    assertThat(Precedence.of(e("a instanceof b")), is(7));
  }
  @Test public void methodAccess() {
    assertThat(Precedence.of(e("t.f()")), is(1));
  }
  @Test public void methodInvocation() {
    assertThat(Precedence.of(e("a()")), is(1));
  }
  @Test public void methodInvocationIsNotNegative() {
    assertThat(Precedence.of(e("f(a,b,c)")), greaterThanOrEqualTo(0));
  }
  @Test public void methodInvocationIsNotTernary() {
    assertThat(Precedence.of(e("f(a,b,c)")), not(comparesEqualTo(Precedence.of(e("a?b:c")))));
  }
  @Test public void multiplication() {
    assertThat(Precedence.of(e("a*b")), is(4));
    assertThat(Precedence.of(e("a/b")), is(4));
    assertThat(Precedence.of(e("a%b")), is(4));
  }
  @Test public void objectCreation() {
    assertThat(Precedence.of(e("new B(a)")), is(3));
  }
  @Test public void or() {
    assertThat(Precedence.of(e("a|b")), is(11));
  }
  @Test public void postfix() {
    assertThat(Precedence.of(e("a++")), is(1));
    assertThat(Precedence.of(e("a--")), is(1));
  }
  @Test public void precedenceOfNulGreatherl() {
    assertThat(Precedence.greater(null, c("a?b:c")), is(true));
  }
  @Test public void precedenceOfNull() {
    assertThat(Precedence.of((Expression) null), is(Precedence.UNDEFINED));
  }
  @Test public void prefix() {
    assertThat(Precedence.of(e("++a")), is(2));
    assertThat(Precedence.of(e("--a")), is(2));
    assertThat(Precedence.of(e("+a")), is(2));
    assertThat(Precedence.of(e("-a")), is(2));
    assertThat(Precedence.of(e("!a")), is(2));
    assertThat(Precedence.of(e("~a")), is(2));
  }
  @Test public void qualifiedAccess() {
    assertThat(Precedence.of(e("a.f")), is(1));
  }
  @Test public void realtional() {
    assertThat(Precedence.of(e("a>b")), is(7));
    assertThat(Precedence.of(e("a<b")), is(7));
    assertThat(Precedence.of(e("a>=b")), is(7));
    assertThat(Precedence.of(e("a<=b")), is(7));
  }
  @Test public void shift() {
    assertThat(Precedence.of(e("a>>b")), is(6));
    assertThat(Precedence.of(e("a<<b")), is(6));
    assertThat(Precedence.of(e("a>>>b")), is(6));
  }
  @Test public void ternary() {
    assertThat(Precedence.of(c("a?b:c")), is(14));
  }
  @Test public void ternaryIsNotNegative() {
    assertThat(Precedence.of(c("A?b:c")), greaterThanOrEqualTo(0));
  }
  @Test public void xor() {
    assertThat(Precedence.of(e("a^b")), is(10));
  }
}
