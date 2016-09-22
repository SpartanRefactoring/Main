package il.org.spartan.spartanizer.ast;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;

/** Tests of {@link NameGuess}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class NameGuessTest {
  @Test public void anonymous1() {
    azzert.that(NameGuess.of("_"), is(NameGuess.ANONYMOUS));
  }

  @Test public void anonymous2() {
    azzert.that(NameGuess.of("__"), is(NameGuess.ANONYMOUS));
  }

  @Test public void anonymous3() {
    azzert.that(NameGuess.of("___"), is(NameGuess.ANONYMOUS));
  }

  @Test public void cent1() {
    azzert.that(NameGuess.of("¢"), is(NameGuess.CENT));
  }

  @Test public void cent2() {
    azzert.that(NameGuess.of("¢¢"), is(NameGuess.CENT));
  }

  @Test public void cent3() {
    azzert.that(NameGuess.of("¢¢¢"), is(NameGuess.CENT));
  }

  @Test public void classConstant1() {
    azzert.that(NameGuess.of("ABC"), is(NameGuess.CLASS_CONSTANT));
  }

  @Test public void classConstant2() {
    azzert.that(NameGuess.of("ABC"), is(NameGuess.CLASS_CONSTANT));
  }

  @Test public void classConstant3() {
    azzert.that(NameGuess.of("ABC"), is(NameGuess.CLASS_CONSTANT));
  }

  @Test public void className1() {
    azzert.that(NameGuess.of(this.getClass().getSimpleName()), is(NameGuess.CLASS_NAME));
  }

  @Test public void className2() {
    azzert.that(NameGuess.of("Class"), is(NameGuess.CLASS_NAME));
  }

  @Test public void className3() {
    azzert.that(NameGuess.of("MyClass"), is(NameGuess.CLASS_NAME));
  }

  @Test public void className4() {
    assert NameGuess.isClassName("MyClass");
  }

  @Test public void className5() {
    assert NameGuess.isClassName("$Class");
  }

  @Test public void className6() {
    assert NameGuess.isClassName("YourClass");
  }

  @Test public void dollar1() {
    azzert.that(NameGuess.of("$"), is(NameGuess.DOLLAR));
  }

  @Test public void dollar2() {
    azzert.that(NameGuess.of("$$"), is(NameGuess.DOLLAR));
  }

  @Test public void dollar3() {
    azzert.that(NameGuess.of("$$$"), is(NameGuess.DOLLAR));
  }
}
