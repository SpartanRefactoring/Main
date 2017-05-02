package il.org.spartan.statistics;

import static il.org.spartan.statistics.Kurtosis.*;
import static il.org.spartan.statistics.MomentUtils.*;
import static il.org.spartan.statistics.Skewness.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;

/** @author Yossi Gil
 * @since 30/04/2011 */
@Utility public enum JarqueBera {
  ;
  public static double jarqueBera(@NotNull final double... vs) {
    return jarqueBeraNormalizedVector(normalize(vs.clone()));
  }

  public static double jarqueBeraNormalizedVector(@NotNull final double... ¢) {
    return ¢.length * (sqr(skewnessNormalizedVector(¢)) + sqr(kurtosisNormalizedVector(¢) / 2)) / 6;
  }
}
