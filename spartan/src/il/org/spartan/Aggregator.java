/**
 *
 */
package il.org.spartan;

import static il.org.spartan.utils.____.*;

import java.util.*;

import il.org.spartan.Aggregator.Aggregation.*;
import il.org.spartan.statistics.*;
import il.org.spartan.utils.*;

/** @author Yossi Gil
 * @since Apr 8, 2012 */
public class Aggregator {
  private static <K, V> void ensure(final Map<K, V> m, final K k, final V v) {
    if (!m.containsKey(k))
      m.put(k, v);
  }

  private static <K, V> void force(final Map<K, V> m, final K k, final V v) {
    ensure(m, k, v);
    final V u = m.get(k);
    require(v == u || v.equals(u) || v.getClass().isArray() && Arrays.equals((Object[]) u, (Object[]) v));
  }

  private static Map<Aggregation, String> toMap(final FormatSpecifier[] ss) {
    final Map<Aggregation, String> $ = new LinkedHashMap<>();
    for (final FormatSpecifier a : ss)
      $.put(a.getKey(), a.format());
    return $;
  }

  private final List<Aggregation> allAggregations = new ArrayList<>();
  private final Map<String, Map<Aggregation, String>> columnSpecificAggregation = new HashMap<>();
  private final Map<String, RealStatistics> realStatistics = new LinkedHashMap<>();
  private String markColumn;

  public void addAggregates(final Iterable<String> keys, final AbstractStringProperties to, final Aggregation a) {
    for (final String key : keys)
      addAggregate(key, to, a);
  }

  public final Iterable<Aggregation> aggregations() {
    return allAggregations;
  }

  public void markColumn(final String key) {
    markColumn = key;
  }

  public void record(final String key, final double value, final FormatSpecifier... ss) {
    record(key, value, toMap(ss));
  }

  public void record(final String key, final double value, final Map<Aggregation, String> as) {
    ensure(realStatistics, key, new RealStatistics());
    force(columnSpecificAggregation, key, as);
    merge(as);
    realStatistics.get(key).record(value);
  }

  public int size() {
    return allAggregations.size();
  }

  public boolean isEmpty() {
    return allAggregations.isEmpty();
  }
  protected void merge(final Map<Aggregation, String> as) {
    int lastFound = -1;
    for (final Aggregation a : as.keySet()) {
      final int j = allAggregations.indexOf(a);
      if (j < 0) {
        allAggregations.add(a);
        continue;
      }
      require(j > lastFound);
      lastFound = j;
    }
  }

  private void addAggregate(final String key, final AbstractStringProperties to, final Aggregation a) {
    to.put(key, key.equals(markColumn) ? a + "" : missing(key, a) ? "" : get(key, a));
  }

  private String get(final String key, final Aggregation a) {
    return a.retreive(realStatistics.get(key), columnSpecificAggregation.get(key).get(a));
  }

  private boolean missing(final String key, final Aggregation a) {
    return !columnSpecificAggregation.containsKey(key) || !columnSpecificAggregation.get(key).containsKey(a);
  }

  public enum Aggregation {
    COUNT {
      @Override public double retreive(final RealStatistics s) {
        return s.n();
      }
    },
    MIN {
      @Override public double retreive(final RealStatistics s) {
        return s.min();
      }

      @Override public String toString() {
        return "\\textbf{\\emph{Min}}";
      }
    },
    MAX {
      @Override public double retreive(final RealStatistics s) {
        return s.max();
      }

      @Override public String toString() {
        return "\\textbf{\\emph{Max}}";
      }
    },
    MEAN {
      @Override public double retreive(final RealStatistics s) {
        return s.mean();
      }

      @Override public String toString() {
        return "\\textbf{\\emph{Mean}}";
      }
    },
    MEDIAN {
      @Override public double retreive(final RealStatistics s) {
        return s.median();
      }

      @Override public String toString() {
        return "\\textbf{\\emph{Median}}";
      }
    },
    SD {
      @Override public double retreive(final RealStatistics s) {
        return s.sd();
      }

      @Override public String toString() {
        return "$\\sigma$";
      }
    },
    TOTAL {
      @Override public double retreive(final RealStatistics s) {
        return s.sum();
      }

      @Override public String toString() {
        return "\\textbf{\\emph{Total}}";
      }
    },
    MAD {
      @Override public double retreive(final RealStatistics s) {
        return s.mad();
      }

      @Override public String toString() {
        return "\\textbf{\\emph{M.A.D}}";
      }
    };
    public static FormatSpecifier COUNT() {
      return COUNT.format("%d");
    }

    public static FormatSpecifier MAD() {
      return MAD.format("%g");
    }

    public static FormatSpecifier MAX() {
      return MAX.format("%g");
    }

    public static FormatSpecifier MEAN() {
      return MEAN.format("%g");
    }

    public static FormatSpecifier MEDIAN() {
      return MEDIAN.format("%g");
    }

    public static FormatSpecifier MIN() {
      return MIN.format("%g");
    }

    public static FormatSpecifier SD() {
      return SD.format("%g");
    }

    public static FormatSpecifier TOTAL() {
      return TOTAL.format("%g");
    }

    @SuppressWarnings("static-method") //
    public FormatSpecifier format(final String format) {
      return new FormatSpecifier() {
        @Override public String format() {
          return format;
        }

        @Override public Aggregation getKey() {
          return Aggregation.this;
        }
      };
    }

    public abstract double retreive(RealStatistics s);

    public String retreive(final RealStatistics s, final String format) {
      try {
        return String.format(format, Box.it(retreive(s)));
      } catch (final ArithmeticException e) {
        return ""; //
      }
    }

    public static abstract class FormatSpecifier {
      public abstract String format();

      public abstract Aggregation getKey();
    }
  }
}
