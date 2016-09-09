/**
 *
 */
package il.org.spartan.statistics;

import static il.org.spartan.statistics.Skewness.*;
import static il.org.spartan.statistics.Sum.*;
import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.streotypes.*;

/** @author Yossi Gil
 * @since 2011-08-1 */
@Utility public enum Mean {
  ;
  public static double destructiveMoment(final int n, final double... ds) {
    return sum(n, shift(ds)) / ds.length;
  }

  public static double mean(final double... ds) {
    return sum(ds) / ds.length;
  }

  public static double moment(final int n, final double... ds) {
    return destructiveMoment(n, ds.clone());
  }

  public static double[] shift(final double... ds) {
    final double mean = mean(ds);
    for (int i = 0; i < ds.length; i++)
      ds[i] -= mean;
    return ds;
  }

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void moment1() {
      final double vs[] = { 1, 2, 3, 4, 5 };
      assertEquals(1, moment(0, vs), 1E-8);
      assertEquals(15, sum(1, vs), 1E-8);
      shift(vs);
      assertEquals(0, moment(1, vs), 1E-8);
      assertEquals((4 + 1 + 0 + 1 + 4) / 5.0, moment(2, vs), 1E-8);
      assertEquals((-9 - 1 + 0 + 1 + 9) / 5.0, moment(3, vs), 1E-8);
      assertEquals((16 + 1 + 0 + 1 + 16) / 5.0, moment(4, vs), 1E-8);
      assertEquals(0.0, skewness(vs), 1E-8);
    }
  }
}
