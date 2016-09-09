/**
 *
 */
package il.org.spartan.statistics;

import static il.org.spartan.statistics.Kurtosis.*;
import static il.org.spartan.statistics.MomentUtils.*;
import static il.org.spartan.statistics.Skewness.*;

import il.org.spartan.streotypes.*;

/** @author Yossi Gil
 * @since 30/04/2011 */
@Utility public enum JarqueBera {
  ;
  public static double jarqueBera(final double... vs) {
    return jarqueBeraNormalizedVector(normalize(vs.clone()));
  }

  public static double jarqueBeraNormalizedVector(final double... ds) {
    return ds.length * (sqr(skewnessNormalizedVector(ds)) + sqr(kurtosisNormalizedVector(ds) / 2)) / 6;
  }
}
