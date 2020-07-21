package il.org.spartan.spartanizer.ast.engine;

import static fluent.ly.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** Tests of {@link guessName}
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
public class GuessNameTest {
  @Test public void anonymous1() {
    azzert.that(guessName.of("_"), is(guessName.ANONYMOUS));
  }
  @Test public void anonymous2() {
    azzert.that(guessName.of("__"), is(guessName.ANONYMOUS));
  }
  @Test public void anonymous3() {
    azzert.that(guessName.of("___"), is(guessName.ANONYMOUS));
  }
  @Test public void cent1() {
    azzert.that(guessName.of("¢"), is(guessName.CENT));
  }
  @Test public void cent2() {
    azzert.that(guessName.of("¢¢"), is(guessName.CENT));
  }
  @Test public void cent3() {
    azzert.that(guessName.of("¢¢¢"), is(guessName.CENT));
  }
  @Test public void classConstant1() {
    azzert.that(guessName.of("ABC"), is(guessName.STATIC_CONSTANT));
  }
  @Test public void classConstant2() {
    azzert.that(guessName.of("ABC"), is(guessName.STATIC_CONSTANT));
  }
  @Test public void classConstant3() {
    azzert.that(guessName.of("ABC"), is(guessName.STATIC_CONSTANT));
  }
  @Test public void className1() {
    azzert.that(guessName.of(getClass().getSimpleName()), is(guessName.CLASS_NAME));
  }
  @Test public void className2() {
    azzert.that(guessName.of("Class"), is(guessName.CLASS_NAME));
  }
  @Test public void className3() {
    azzert.that(guessName.of("MyClass"), is(guessName.CLASS_NAME));
  }
  @Test public void className4() {
    assert guessName.isClassName("MyClass");
  }
  @Test public void className5() {
    assert guessName.isClassName("$Class");
  }
  @Test public void className6() {
    assert guessName.isClassName("YourClass");
  }
  @Test public void dollar1() {
    azzert.that(guessName.of("$"), is(guessName.DOLLAR));
  }
  @Test public void dollar2() {
    azzert.that(guessName.of("$$"), is(guessName.DOLLAR));
  }
  @Test public void dollar3() {
    azzert.that(guessName.of("$$$"), is(guessName.DOLLAR));
  }
  @Test public void variable1() {
    azzert.that(guessName.of("multiset1"), is(guessName.METHOD_OR_VARIABLE));
  }
}
