package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;

import il.org.spartan.*;
import il.org.spartan.external.*;
import il.org.spartan.statistics.*;

/** A relation is just another name for a table that contains elements of type
 * {@link Record}. This class provides fluent API for generating tables,
 * including aggregation information.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-25 */
public class Relation extends Record<Relation> implements Closeable {
  /* @formatter:off*/ @Override protected Relation self() { return this; } /*@formatter:on*/

  private static final long serialVersionUID = 1L;
  private int nRecords;
  public final String name;
  static final String temporariesFolder = System.getProperty("java.io.tmpdir", "/tmp") + "/";
  private final List<RecordWriter> writers = new ArrayList<>();

  public Relation(final String name) {
    this(name, Renderer.builtin.values());
  }

  @SuppressWarnings("resource") public Relation(final String name, final Renderer... rs) {
    this.name = name;
    for (final Renderer r : rs)
      try {
        writers.add(new RecordWriter(r, temporariesFolder + name));
      } catch (final IOException ¢) {
        close();
        throw new RuntimeException(¢);
      }
  }

  @Override public void close() {
    for (final RecordWriter ¢ : writers)
      ¢.close();
  }

  @External Statistic[] statisics = Statistic.values();

  void add(final Statistic... ss) {
    final List<Statistic> a = as.list(statisics);
    a.addAll(as.list(ss));
    set(a);
  }

  void remove(final Statistic... ss) {
    final List<Statistic> a = as.list(statisics);
    a.removeAll(as.list(ss));
    set(a);
  }

  private void set(final List<Statistic> ¢) {
    set(¢.toArray(new Statistic[¢.size()]));
  }

  void set(final Statistic... ¢) {
    statisics = ¢;
  }

  final Map<String, RealStatistics> stats = new LinkedHashMap<>();

  public void nl() {
    for (final RecordWriter ¢ : writers)
      ¢.write(this);
    reset();
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
    put((String)null, ++nRecords + "");
    return this;
  }
}
