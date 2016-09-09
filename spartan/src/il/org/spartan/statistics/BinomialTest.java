/**
 *
 */
package il.org.spartan.statistics;

import static org.junit.Assert.*;

import java.math.*;

import org.junit.*;

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
    assertEquals(Binomial.factorial(10), new BigInteger("3628800"));
  }

  @Test public void factorial15() {
    assertEquals(Binomial.factorial(15), new BigInteger("1307674368000"));
  }

  @Test public void factorial20() {
    assertEquals(Binomial.factorial(20), new BigInteger("2432902008176640000"));
  }

  @Test public void factorial21() {
    assertEquals(Binomial.factorial(21), new BigInteger("51090942171709440000"));
  }

  @Test public void factorial4() {
    assertEquals(Binomial.factorial(4), new BigInteger("24"));
  }

  @Test public void factorial68() {
    assertEquals(Binomial.factorial(68),
        new BigInteger("2480035542436830599600990418569171581047399201355367672371710738018221445712183296000000000000000"));
  }

  @Test public void probability() {
    assertEquals(Binomial.probability(20, 10), 0.1761970520019533, 1E-15);
  }

  @Test public void significanceAll() {
    assertEquals(1.0, Binomial.significance(20, 10), 1E-15);
  }

  @Test public void significanceSmall() {
    assertEquals(2 * 0.0002012252807617188, Binomial.significance(20, 2), 1E-15);
  }
}
