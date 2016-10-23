/**
 *
 */
package il.org.spartan;

import static il.org.spartan.utils.___.*;

import java.util.*;

import il.org.spartan.Aggregator.Aggregation.*;
import il.org.spartan.statistics.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;

/** @author Yossi Gil
 * @since Apr 8, 2012 */
public class Aggregator {
  private static <K, V> void ensure(@NotNull final Map<K, V> m, final K k, final V v) {
    if (!m.containsKey(k))
      m.put(k, v);
  }

  private static <K, V> void force(@NotNull final Map<K, V> m, final K k, @NotNull final V v) {
    ensure(m, k, v);
    final V u = m.get(k);
    require(v == u || v.equals(u) || v.getClass().isArray() && Arrays.equals((Object[]) u, (Object[]) v));
  }

  @NotNull
  private static Map<Aggregation, String> toMap(@NotNull final FormatSpecifier[] ss) {
    final Map<Aggregation, String> $ = new LinkedHashMap<>();
    for (final FormatSpecifier ¢ : ss)
      $.put(¢.getKey(), ¢.format());
    return $;
  }

  private final List<Aggregation> allAggregations = new ArrayList<>();
  private final Map<String, Map<Aggregation, String>> columnSpecificAggregation = new HashMap<>();
  private final Map<String, RealStatistics> realStatistics = new LinkedHashMap<>();
  private String markColumn;

  public void addAggregates(@NotNull final Iterable<String> keys, @NotNull final AbstractStringProperties to, @NotNull final Aggregation a) {
    for (final String key : keys)
      addAggregate(key, to, a);
  }

  @NotNull
  public final Iterable<Aggregation> aggregations() {
    return allAggregations;
  }

  public boolean isEmpty() {
    return allAggregations.isEmpty();
  }

  public void markColumn(final String key) {
    markColumn = key;
  }

  public void record(final String key, final double value, @NotNull final FormatSpecifier... ss) {
    record(key, value, toMap(ss));
  }

  public void record(final String key, final double value, @NotNull final Map<Aggregation, String> a) {
    ensure(realStatistics, key, new RealStatistics());
    force(columnSpecificAggregation, key, a);
    merge(a);
    realStatistics.get(key).record(value);
  }

  public int size() {
    return allAggregations.size();
  }

  protected void merge(@NotNull final Map<Aggregation, String> as) {
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

  private void addAggregate(@NotNull final String key, @NotNull final AbstractStringProperties to, @NotNull final Aggregation a) {
    to.put(key, key.equals(markColumn) ? a + "" : missing(key, a) ? "" : get(key, a));
  }

  private String get(final String key, @NotNull final Aggregation a) {
    return a.retreive(realStatistics.get(key), columnSpecificAggregation.get(key).get(a));
  }

  private boolean missing(final String key, final Aggregation a) {
    return !columnSpecificAggregation.containsKey(key) || !columnSpecificAggregation.get(key).containsKey(a);
  }

  public enum Aggregation {
    COUNT {
      @Override public double retreive(@NotNull final RealStatistics ¢) {
        return ¢.n();
      }
    },
    MIN {
      @Override public double retreive(@NotNull final RealStatistics ¢) {
        return ¢.min();
      }

      @NotNull
      @Override public String toString() {
        return "\\textbf{\\emph{Min}}";
      }
    },
    MAX {
      @Override public double retreive(@NotNull final RealStatistics ¢) {
        return ¢.max();
      }

      @NotNull
      @Override public String toString() {
        return "\\textbf{\\emph{Max}}";
      }
    },
    MEAN {
      @Override public double retreive(@NotNull final RealStatistics ¢) {
        return ¢.mean();
      }

      @NotNull
      @Override public String toString() {
        return "\\textbf{\\emph{Mean}}";
      }
    },
    MEDIAN {
      @Override public double retreive(@NotNull final RealStatistics ¢) {
        return ¢.median();
      }

      @NotNull
      @Override public String toString() {
        return "\\textbf{\\emph{Median}}";
      }
    },
    SD {
      @Override public double retreive(@NotNull final RealStatistics ¢) {
        return ¢.sd();
      }

      @NotNull
      @Override public String toString() {
        return "$\\sigma$";
      }
    },
    TOTAL {
      @Override public double retreive(@NotNull final RealStatistics ¢) {
        return ¢.sum();
      }

      @NotNull
      @Override public String toString() {
        return "\\textbf{\\emph{Total}}";
      }
    },
    MAD {
      @Override public double retreive(@NotNull final RealStatistics ¢) {
        return ¢.mad();
      }

      @NotNull
      @Override public String toString() {
        return "\\textbf{\\emph{M.A.D}}";
      }
    };
    @NotNull
    public static FormatSpecifier COUNT() {
      return COUNT.format("%d");
    }

    @NotNull
    public static FormatSpecifier MAD() {
      return MAD.format("%g");
    }

    @NotNull
    public static FormatSpecifier MAX() {
      return MAX.format("%g");
    }

    @NotNull
    public static FormatSpecifier MEAN() {
      return MEAN.format("%g");
    }

    @NotNull
    public static FormatSpecifier MEDIAN() {
      return MEDIAN.format("%g");
    }

    @NotNull
    public static FormatSpecifier MIN() {
      return MIN.format("%g");
    }

    @NotNull
    public static FormatSpecifier SD() {
      return SD.format("%g");
    }

    @NotNull
    public static FormatSpecifier TOTAL() {
      return TOTAL.format("%g");
    }

    @NotNull
    @SuppressWarnings("static-method") //
    public FormatSpecifier format(@NotNull final String format) {
      return new FormatSpecifier() {
        @NotNull
        @Override public String format() {
          return format;
        }

        @NotNull
        @Override public Aggregation getKey() {
          return Aggregation.this;
        }
      };
    }

    public abstract double retreive(RealStatistics s);

    public String retreive(final RealStatistics s, @NotNull final String format) {
      try {
        return String.format(format, Box.it(retreive(s)));
      } catch (@NotNull final ArithmeticException e) {
        return ""; //
      }
    }

    public abstract static class FormatSpecifier {
      @NotNull
      public abstract String format();

      @NotNull
      public abstract Aggregation getKey();
    }
  }
}
