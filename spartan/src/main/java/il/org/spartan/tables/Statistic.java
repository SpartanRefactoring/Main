package il.org.spartan.tables;

import java.util.Arrays;

import il.org.spartan.statistics.RealStatistics;

/** TODO Yossi Gil Document Class
 *
 * @author Yossi Gil
 * @since 2016-12-26 */
public enum Statistic {
  N {
    @Override public double of(final RealStatistics ¢) {
      return ¢.n();
    }
  },
  NA {
    @Override public String toString() {
      return "N/A";
    }

    @Override public double of(final RealStatistics ¢) {
      return ¢.missing();
    }
  },
  mean {
    @Override public double of(final RealStatistics ¢) {
      return ¢.mean();
    }
  },
  σ {
    @Override public double of(final RealStatistics ¢) {
      return ¢.sd();
    }
  },
  median {
    @Override public double of(final RealStatistics ¢) {
      return ¢.median();
    }
  },
  MAD {
    @Override public double of(final RealStatistics ¢) {
      return ¢.mad();
    }

    @Override public String toString() {
      return "M.A.D";
    }
  },
  min {
    @Override public double of(final RealStatistics ¢) {
      return ¢.min();
    }
  },
  max {
    @Override public double of(final RealStatistics ¢) {
      return ¢.max();
    }
  },
  range {
    @Override public double of(final RealStatistics ¢) {
      return ¢.max() - ¢.min();
    }
  },
  Q1 {
    @Override public double of(final RealStatistics ¢) {
      return quartile(25, ¢.all());
    }
  },
  Q3 {
    @Override public double of(final RealStatistics ¢) {
      return quartile(75, ¢.all());
    }
  },
  Σ {
    @Override public double of(final RealStatistics ¢) {
      return ¢.sum();
    }
  };

  public abstract double of(RealStatistics s);

  /** @param p percents of quartile (for q1 - 25, q3 - 75, median - 50) */
  static double quartile(final int p, final double[] ds) {
    Arrays.sort(ds);
    return ds[Math.round(p * ds.length / 100)];
  }
}
