package il.org.spartan.statistics;

import static il.org.spartan.statistics.Mean.*;
import static il.org.spartan.statistics.StandardDeviation.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;

/** @author Yossi Gil
 * @since 2011-08-1 */
@Utility public enum Kurtosis {
  ;
  public static double kurotsis(@NotNull final double... ¢) {
    return kurtosisNormalizedVector(normalize(¢.clone()));
  }

  public static double kurtosisNormalizedVector(final double... ¢) {
    return moment(4, ¢) - 3;
  }
}
