package il.org.spartan;

import static fluent.ly.___.require;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import il.org.spartan.Aggregator.Aggregation.FormatSpecifier;
import il.org.spartan.statistics.RealStatistics;

/** @author Yossi Gil
 * @since Apr 8, 2012 */
public class Aggregator {
  private static <K, V> void ensure(final Map<K, V> m, final K k, final V v) {
    m.putIfAbsent(k, v);
  }
  private static <K, V> void force(final Map<K, V> m, final K k, final V v) {
    ensure(m, k, v);
    final V u = m.get(k);
    require(v == u || v.equals(u) || v.getClass().isArray() && Arrays.equals((Object[]) u, (Object[]) v));
  }
  private static Map<Aggregation, String> toMap(final FormatSpecifier[] ss) {
    final Map<Aggregation, String> $ = new LinkedHashMap<>();
    for (final FormatSpecifier ¢ : ss)
      $.put(¢.getKey(), ¢.format());
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
  public boolean isEmpty() {
    return allAggregations.isEmpty();
  }
  public Aggregator markColumn(final String key) {
    markColumn = key;
    return this;
  }
  public void record(final String key, final double value, final FormatSpecifier... ss) {
    record(key, value, toMap(ss));
  }
  public void record(final String key, final double value, final Map<Aggregation, String> m) {
    ensure(realStatistics, key, new RealStatistics());
    force(columnSpecificAggregation, key, m);
    merge(m);
    realStatistics.get(key).record(value);
  }
  public int size() {
    return allAggregations.size();
  }
  protected void merge(final Map<Aggregation, String> m) {
    int lastFound = -1;
    for (final Aggregation a : m.keySet()) {
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
      @Override public double retreive(final RealStatistics ¢) {
        return ¢.n();
      }
    },
    MIN {
      @Override public double retreive(final RealStatistics ¢) {
        return ¢.min();
      }
      @Override public String toString() {
        return "\\textbf{\\emph{Min}}";
      }
    },
    MAX {
      @Override public double retreive(final RealStatistics ¢) {
        return ¢.max();
      }
      @Override public String toString() {
        return "\\textbf{\\emph{Max}}";
      }
    },
    MEAN {
      @Override public double retreive(final RealStatistics ¢) {
        return ¢.mean();
      }
      @Override public String toString() {
        return "\\textbf{\\emph{Mean}}";
      }
    },
    MEDIAN {
      @Override public double retreive(final RealStatistics ¢) {
        return ¢.median();
      }
      @Override public String toString() {
        return "\\textbf{\\emph{Median}}";
      }
    },
    SD {
      @Override public double retreive(final RealStatistics ¢) {
        return ¢.sd();
      }
      @Override public String toString() {
        return "$\\sigma$";
      }
    },
    TOTAL {
      @Override public double retreive(final RealStatistics ¢) {
        return ¢.sum();
      }
      @Override public String toString() {
        return "\\textbf{\\emph{Total}}";
      }
    },
    MAD {
      @Override public double retreive(final RealStatistics ¢) {
        return ¢.mad();
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
    public String retreive(final RealStatistics $, final String format) {
      try {
        return String.format(format, fluent.ly.box.it(retreive($)));
      } catch (final ArithmeticException e) {
        return ""; //
      }
    }

    public abstract static class FormatSpecifier {
      public abstract String format();
      public abstract Aggregation getKey();
    }
  }
}
