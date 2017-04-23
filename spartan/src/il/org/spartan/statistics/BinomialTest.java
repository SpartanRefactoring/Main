package il.org.spartan.statistics;

import static il.org.spartan.azzert.*;
import static org.junit.Assert.assertEquals;

import java.math.*;

import org.junit.*;

import il.org.spartan.*;

/** @author Yossi Gil
 * @since 8 באוק 2011 */
@SuppressWarnings("static-method") public class BinomialTest {
  @Test public void coefficient() {
    assertEquals(Binomial.coefficient(10, 3), 120L, 1E-12);
  }

  @Test public void cumulative() {
    assertEquals(Binomial.cumulative(50, 18), 0.03245432353613618, 1E-15);
  }

  @Test public void factorial10() {
    azzert.that(new BigInteger("3628800"), is(Binomial.factorial(10)));
  }

  @Test public void factorial15() {
    azzert.that(new BigInteger("1307674368000"), is(Binomial.factorial(15)));
  }

  @Test public void factorial20() {
    azzert.that(new BigInteger("2432902008176640000"), is(Binomial.factorial(20)));
  }

  @Test public void factorial21() {
    azzert.that(new BigInteger("51090942171709440000"), is(Binomial.factorial(21)));
  }

  @Test public void factorial4() {
    azzert.that(new BigInteger("24"), is(Binomial.factorial(4)));
  }

  @Test public void factorial68() {
    azzert.that(new BigInteger("2480035542436830599600990418569171581047399201355367672371710738018221445712183296000000000000000"),
        is(Binomial.factorial(68)));
  }

  @Test public void probability() {
    assertEquals(Binomial.probability(20, 10), 0.1761970520019533, 1E-15);
  }

  @Test public void significanceAll() {
    assertEquals(1.0, Binomial.significance(20, 10), 1E-15);
  }

  @Test public void significanceSmall() {
    assertEquals(4.024505615234376E-4, Binomial.significance(20, 2), 1E-15);
  }
}
