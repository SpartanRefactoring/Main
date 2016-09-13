package il.org.spartan.spartanizer.ast;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.engine.into.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;

@SuppressWarnings({ "static-method", "javadoc" }) public class metricsTest {
  private String helloWorldQuoted = "\"Hello, World!\\n\"";
  private Expression x1 = e("(-b - sqrt(b * b - 4 * a* c))/(2*a)"), x2 = e("(-b + sqrt(b * b - 4 * a* c))/(2*a)");
  private Expression booleans = e("true||false||true");
  private Expression helloWorld = e("f(" + helloWorldQuoted + ")");

  @Test public void dictionary() {
    azzert.that(metrics.dictionary(x1), hasItem("a"));
    azzert.that(metrics.dictionary(x2), hasItem("b"));
    azzert.that(metrics.dictionary(x2), hasItem("sqrt"));
  }

  @Test public void vocabulary() {
    azzert.that(metrics.vocabulary(x1), is(4));
    azzert.that(metrics.vocabulary(x2), is(4));
    azzert.that(metrics.vocabulary(booleans), is(0));
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
  @Test public void accurateLiterals() {
    azzert.that(metrics.literals(helloWorld), hasItem("Hello, World!\n"));
  }

  @Test public void issue101_5() {
    final Expression ¢ = i("3+4+5+6");
    azzert.that(metrics.nodes(¢), is(5));
  }

  @Test public void issue128_4() {
    final Expression ¢ = i("3+4*4+6*7+8");
    azzert.that(metrics.nodes(¢), is(11));
  }

  @Test public void issue128_1() {
    final Expression ¢ = i("3+4");
    azzert.that(metrics.nodes(¢), is(3));
  }

  @Test public void issue128_11() {
    final Expression ¢ = i("3+4");
    azzert.that(metrics.internals(¢), is(1));
  }

  @Test public void issue128_12() {
    final Expression ¢ = i("3+4");
    azzert.that(metrics.leaves(¢), is(2));
  }

  @Test public void issue128_13() {
    final Expression ¢ = e("a==4 ? 34 : 56");
    azzert.that(metrics.internals(¢), is(2));
  }

  @Test public void issue128_14() {
    final Expression ¢ = e("a==4 ? 34 : 56");
    azzert.that(metrics.leaves(¢), is(4));
  }

  @Test public void issue128_15() {
    final Expression ¢ = e("1+2");
    azzert.that(metrics.dexterity(¢), is(2));
  }

  @Test public void issue128_16() {
    final Expression ¢ = e("a+2");
    azzert.that(metrics.dexterity(¢), is(3));
  }

  @Test public void issue128_17() {
    final Expression ¢ = e("g(false)||a(h())");
    azzert.that(metrics.dexterity(¢), is(3));
  }

  @Test public void issue128_18() {
    final Expression ¢ = e("a==4 ? (34++) : 56+34+99");
    azzert.that(metrics.dexterity(¢), is(5));
  }

  @Test public void issue128_2() {
    final Expression ¢ = i("3+4");
    azzert.that(metrics.nodes(¢), is(3));
  }

  @Test public void issue128_3() {
    final Expression ¢ = i("5*6+43*2");
    azzert.that(metrics.nodes(¢), is(7));
  }

  @Test public void issue128_6() {
    final Expression ¢ = e("a==4 ? 34 : 56");
    azzert.that(metrics.nodes(¢), is(6));
  }

  @Test public void issue128_7() {
    final Expression ¢ = e("a==4 ? 34 : 56+34");
    azzert.that(metrics.nodes(¢), is(8));
  }

  @Test public void issue128_8() {
    final Expression ¢ = e("a==4 ? 34 : 56+34+99");
    azzert.that(metrics.nodes(¢), is(9));
  }

  @Test public void issue128_9() {
    final Expression ¢ = e("!f.g(X,false)||a.b.e(m.h())");
    azzert.that(metrics.nodes(¢), is(10));
  }

  @Test public void issue129_10() {
    final Expression ¢ = e("g(false)||a(h())");
    azzert.that(metrics.nodes(¢), is(5));
  }
}
