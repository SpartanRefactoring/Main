package il.org.spartan.spartanizer.cmdline;
// TODO: Yossi: I commented this one too.

// import static il.org.spartan.utils.___.*;
import java.io.*;
import java.util.*;

import il.org.spartan.*;
import il.org.spartan.Aggregator.Aggregation.*;
import il.org.spartan.external.*;
import il.org.spartan.statistics.*;

/** Similar to {@link CSVWriter}, except that in addition to the production of
 * output to the main CSV file, this class generates a secondary CSV file,
 * recording the essential statistics (min, max, count, etc.) of each numerical
 * column in the main CSV file.
 * @author Yossi Gil
 * @since Dec 25, 2009 */
public class Tabler extends CSVLine.Ordered {
  enum Summary {
    min, max, med, mad, sd, range, n, na, common,
  };

  @External Summary[] summaries = { Summary.n, Summary.min, Summary.max };

  void addSummaries(Summary... ss) {
    List<Summary> a = as.list(summaries);
    a.addAll(as.list(ss));
    summaries = a.toArray(new Summary[a.size()]);
  }

  void removeSummaries(Summary... ss) {
    List<Summary> a = as.list(summaries);
    a.removeAll(as.list(ss));
    summaries = a.toArray(new Summary[a.size()]);
  }

  void setSummaries(Summary... ss) {
    summaries = ss;
  }

  private static final String SUMMARY_EXTENSION = ".summary";

  /** @param baseName
   * @return */
  private static String removeExtension(final String baseName) {
    return baseName.replaceFirst("\\.csv$", "");
  }

  final Map<String, RealStatistics> stats = new LinkedHashMap<>();
  final CSVWriter inner;
  final CSVWriter summarizer;

  /** Instantiate this class, setting the names of the main and secondary CSV
   * files.
   * @param baseName the name of the files into which statistics should be
   *        written; if this name ends with ".csv", this extension is removed.
   * @throws IOException */
  public Tabler(final String baseName) throws IOException {
    // TODO: Yossi: I have a compilation error also for this method (nonnull).
    // nonnull(baseName);
    // nonnull(keysHeader);
    inner = new CSVWriter(removeExtension(baseName));
    summarizer = new CSVWriter(removeExtension(baseName) + SUMMARY_EXTENSION);
  }

  public String close() {
    inner.close();
    Enum<?> keysHeader = null;
    for (final String key : stats.keySet()) {
      final CSVLine l = new CSVLine.Ordered.Separated("%");
      l.put(keysHeader, key);
      final ImmutableStatistics s = stats.get(key);
      l//
          .put("$N$", s.n()) //
          .put("\\emph{n/a}", s.missing())//
          .put("Mean", s.n() > 0 ? s.mean() : Double.NaN) //
          .put("Median", s.n() > 0 ? s.median() : Double.NaN)//
          .put("$\\sigma$", s.n() > 0 ? s.sd() : Double.NaN) //
          .put("m.a.d", s.n() > 0 ? s.mad() : Double.NaN) //
          .put("$\\min$", s.n() > 0 ? s.min() : Double.NaN) //
          .put("$\\max$", s.n() > 0 ? s.max() : Double.NaN) //
          .put("Range", s.n() <= 0 ? Double.NaN : s.max() - s.min())//
          .put("Total", s.n() > 0 ? s.sum() : Double.NaN)//
      ;
      summarizer.writeFlush(l);
    }
    return summarizer.close();
  }

  public String mainFileName() {
    return inner.fileName();
  }

  public void nl() {
    inner.writeFlush(this);
  }

  @Override public CSVLine put(final String key, final double value, final FormatSpecifier... ss) {
    getStatistics(key).record(value);
    return super.put(key, value, ss);
  }

  @Override public CSVLine put(final String key, final int value) {
    getStatistics(key).record(value);
    return super.put(key, value);
  }

  @Override public CSVLine put(final String key, final long value) {
    getStatistics(key).record(value);
    super.put(key, value);
    return this;
  }

  public String summaryFileName() {
    return summarizer.fileName();
  }

  RealStatistics getStatistics(final String key) {
    if (stats.get(key) == null)
      stats.put(key, new RealStatistics());
    return stats.get(key);
  }

  public class Line extends CSVLine.Ordered {
    public void close() {
      inner.writeFlush(this);
    }

    @Override public CSVLine put(final String key, final double value, final FormatSpecifier... ss) {
      getStatistics(key).record(value);
      return super.put(key, value, ss);
    }

    @Override public CSVLine put(final String key, final long value) {
      getStatistics(key).record(value);
      return super.put(key, value);
    }
  }
}
