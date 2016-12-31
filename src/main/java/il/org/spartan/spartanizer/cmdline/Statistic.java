package il.org.spartan.spartanizer.cmdline;

import il.org.spartan.statistics.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-26 */
enum Statistic {
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
  };
  public abstract double of(RealStatistics s);
}