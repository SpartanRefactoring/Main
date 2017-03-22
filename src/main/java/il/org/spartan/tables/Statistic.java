package il.org.spartan.tables;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.statistics.*;

/** TODO Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016-12-26 */
public enum Statistic {
  N {
    @Override public double of(@NotNull final RealStatistics ¢) {
      return ¢.n();
    }
  },
  NA {
    @NotNull @Override public String toString() {
      return "N/A";
    }

    @Override public double of(@NotNull final RealStatistics ¢) {
      return ¢.missing();
    }
  },
  mean {
    @Override public double of(@NotNull final RealStatistics ¢) {
      return ¢.mean();
    }
  },
  σ {
    @Override public double of(@NotNull final RealStatistics ¢) {
      return ¢.sd();
    }
  },
  median {
    @Override public double of(@NotNull final RealStatistics ¢) {
      return ¢.median();
    }
  },
  MAD {
    @Override public double of(@NotNull final RealStatistics ¢) {
      return ¢.mad();
    }

    @NotNull @Override public String toString() {
      return "M.A.D";
    }
  },
  min {
    @Override public double of(@NotNull final RealStatistics ¢) {
      return ¢.min();
    }
  },
  max {
    @Override public double of(@NotNull final RealStatistics ¢) {
      return ¢.max();
    }
  },
  range {
    @Override public double of(@NotNull final RealStatistics ¢) {
      return ¢.max() - ¢.min();
    }
  },
  Q1 {
    @Override public double of(@NotNull final RealStatistics ¢) {
      return quartile(25, ¢.all());
    }
  },
  Q3 {
    @Override public double of(@NotNull final RealStatistics ¢) {
      return quartile(75, ¢.all());
    }
  },
  Σ {
    @Override public double of(@NotNull final RealStatistics ¢) {
      return ¢.sum();
    }
  };
  public abstract double of(RealStatistics s);

  /** @param p percents of quartile (for q1 - 25, q3 - 75, median - 50) */
  static double quartile(final int p, @NotNull final double[] ds) {
    Arrays.sort(ds);
    return ds[Math.round(p * ds.length / 100)];
  }
}
