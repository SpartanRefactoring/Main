package il.org.spartan.spartanizer.ast;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings({ "static-method", "javadoc" }) public final class metricsTest {
  private final String helloWorldQuoted = "\"Hello, World!\\n\"";
  private final Expression x1 = e("(-b - sqrt(b * b - 4 * a* c))/(2*a)"), x2 = e("(-b + sqrt(b * b - 4 * a* c))/(2*a)");
  private final Expression booleans = e("true||false||true");
  private final Expression helloWorld = e("f(" + helloWorldQuoted + ")");

  @Test public void accurateLiterals() {
    azzert.that(metrics.literals(helloWorld), hasItem("Hello, World!\n"));
  }

  @Test public void dictionary() {
    azzert.that(metrics.dictionary(x1), hasItem("a"));
    azzert.that(metrics.dictionary(x2), hasItem("b"));
    azzert.that(metrics.dictionary(x2), hasItem("sqrt"));
  }

  @Test public void issue101_5() {
    azzert.that(metrics.nodes(i("3+4+5+6")), is(5));
  }

  @Test public void issue128_1() {
    azzert.that(metrics.nodes(i("3+4")), is(3));
  }

  @Test public void issue128_11() {
    azzert.that(metrics.internals(i("3+4")), is(1));
  }

  @Test public void issue128_12() {
    azzert.that(metrics.leaves(i("3+4")), is(2));
  }

  @Test public void issue128_13() {
    azzert.that(metrics.internals(e("a==4 ? 34 : 56")), is(2));
  }

  @Test public void issue128_14() {
    azzert.that(metrics.leaves(e("a==4 ? 34 : 56")), is(4));
  }

  @Test public void issue128_15() {
    azzert.that(metrics.dexterity(e("1+2")), is(2));
  }

  @Test public void issue128_16() {
    azzert.that(metrics.dexterity(e("a+2")), is(3));
  }

  @Test public void issue128_17() {
    azzert.that(metrics.dexterity(e("g(false)||a(h())")), is(3));
  }

  @Test public void issue128_18() {
    azzert.that(metrics.dexterity(e("a==4 ? (34++) : 56+34+99")), is(5));
  }

  @Test public void issue128_2() {
    azzert.that(metrics.nodes(i("3+4")), is(3));
  }

  @Test public void issue128_3() {
    azzert.that(metrics.nodes(i("5*6+43*2")), is(7));
  }

  @Test public void issue128_4() {
    azzert.that(metrics.nodes(i("3+4*4+6*7+8")), is(11));
  }

  @Test public void issue128_6() {
    azzert.that(metrics.nodes(e("a==4 ? 34 : 56")), is(6));
  }

  @Test public void issue128_7() {
    azzert.that(metrics.nodes(e("a==4 ? 34 : 56+34")), is(8));
  }

  @Test public void issue128_8() {
    azzert.that(metrics.nodes(e("a==4 ? 34 : 56+34+99")), is(9));
  }

  @Test public void issue128_9() {
    azzert.that(metrics.nodes(e("!f.g(X,false)||a.b.e(m.h())")), is(10));
  }

  @Test public void issue129_10() {
    azzert.that(metrics.nodes(e("g(false)||a(h())")), is(5));
  }

  @Test public void literacy() {
    azzert.that(metrics.literacy(x1), is(2));
    azzert.that(metrics.literacy(x2), is(2));
    azzert.that(metrics.literacy(i("3+4+5+6")), is(4));
  }

  @Test public void literals() {
    azzert.that(metrics.literals(x1), hasItem("2"));
    azzert.that(metrics.literals(x2), hasItem("4"));
    azzert.that(metrics.literals(i("3+4+5+6")), hasItem("6"));
  }

  @Test public void literalsBoolean() {
    azzert.that(metrics.literals(booleans), hasItem("true"));
    azzert.that(metrics.literals(booleans), hasItem("false"));
    azzert.that(metrics.vocabulary(booleans), is(0));
  }

  @Test public void nullIsLiteral() {
    azzert.that(metrics.literals(e("null")), hasItem("null"));
  }

  @Test public void stringAreLiterals() {
    azzert.that(metrics.literacy(helloWorld), is(1));
  }

  @Test public void vocabulary() {
    azzert.that(metrics.vocabulary(x1), is(4));
    azzert.that(metrics.vocabulary(x2), is(4));
    azzert.that(metrics.vocabulary(booleans), is(0));
  }
}
