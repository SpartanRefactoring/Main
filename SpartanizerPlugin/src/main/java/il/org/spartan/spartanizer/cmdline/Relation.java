package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;

import il.org.spartan.*;
import il.org.spartan.Aggregator.Aggregation.*;
import il.org.spartan.external.*;
import il.org.spartan.statistics.*;

/** A relation is just another name for a table that contains elements of type
 * {@link Row}. This class provides fluent API for generating tables, including
 * aggregation information.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-25 */
public class Relation extends Record<Relation> implements Closeable {
  /* @formatter:off*/ @Override protected Relation self() { return this; } /*@formatter:on*/

  private static final long serialVersionUID = 1L;
  private int nRecords;
  public final String name;
  static final String temporariesFolder = System.getProperty("java.io.tmpdir", "/tmp") + "/";
  private final List<RowWriter> writers = new ArrayList<>();

  public Relation(final String name) {
    this(name, Renderer.builtin.values());
  }

  @SuppressWarnings("resource") public Relation(final String name, final Renderer... rs) {
    this.name = name;
    for (final Renderer r : rs)
      try {
        writers.add(new RowWriter(r, temporariesFolder + name));
      } catch (final IOException ¢) {
        close();
        throw new RuntimeException(¢);
      }
  }

  @Override public void close() {
    for (final RowWriter ¢ : writers)
      ¢.close();
  }

  enum Summarizer {
    min, max, med, mad, sd, range, n, na,
  }

  @External Summarizer[] summaries = { Summarizer.n, Summarizer.min, Summarizer.max };

  void addSummaries(final Summarizer... ss) {
    final List<Summarizer> a = as.list(summaries);
    a.addAll(as.list(ss));
    setSummarizers(a);
  }

  void removeSummaries(final Summarizer... ss) {
    final List<Summarizer> a = as.list(summaries);
    a.removeAll(as.list(ss));
    setSummarizers(a);
  }

  private void setSummarizers(final List<Summarizer> ¢) {
    setSummarizers(¢.toArray(new Summarizer[¢.size()]));
  }

  void setSummarizers(final Summarizer... ¢) {
    summaries = ¢;
  }

  final Map<String, RealStatistics> stats = new LinkedHashMap<>();

  public void nl() {
    for (final RowWriter ¢ : writers)
      ¢.write(this);
    reset();
  }

  @Override public Relation put(final String key, final double value, final FormatSpecifier... ss) {
    getStatistics(key).record(value);
    return super.put(key, value, ss);
  }

  @Override public Relation put(final String key, final int value) {
    getStatistics(key).record(value);
    return super.put(key, value);
  }

  @Override public Relation put(final String key, final long value) {
    getStatistics(key).record(value);
    super.put(key, value);
    return this;
  }

  RealStatistics getStatistics(final String key) {
    if (stats.get(key) == null)
      stats.put(key, new RealStatistics());
    return stats.get(key);
  }

  @Override protected Relation reset() {
    for (final String ¢ : keySet())
      put(¢, "");
    put("#", ++nRecords + "");
    return this;
  }
}
