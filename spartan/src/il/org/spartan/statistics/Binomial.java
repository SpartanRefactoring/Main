package il.org.spartan.statistics;

import java.math.*;

/** @author Yossi Gil
 * @since 8 באוק 2011 */
public class Binomial {
  public static double coefficient(final int m, final int k) {
    return factorial(m).divide(factorial(k).multiply(factorial(m - k))).doubleValue();
  }

  public static double cumulative(final int m, final int k) {
    double $ = 0;
    for (int kʹ = 0; kʹ <= k; ++kʹ)
      $ += probability(m, kʹ);
    return $;
  }

  public static BigInteger factorial(final int m) {
    return m <= 1 ? BigInteger.ONE : factorial(m - 1).multiply(new BigInteger(m + ""));
  }

  public static double probability(final int m, final int k) {
    return Math.pow(0.5, m) * coefficient(m, k);
  }

  public static double significance(final int m, final int k) {
    return 2 * k > m ? significance(m, m - k) : 2 * k != m ? 2 * cumulative(m, k) : 2 * cumulative(m, k - 1) + probability(m, k);
  }
}
