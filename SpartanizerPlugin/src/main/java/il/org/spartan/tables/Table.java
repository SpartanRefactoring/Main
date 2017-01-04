package il.org.spartan.tables;

import java.io.*;
import java.util.*;

import il.org.spartan.*;
import il.org.spartan.statistics.*;

/** A relation is just another name for a table that contains elements of type
 * {@link Row}. This class provides fluent API for generating tables,
 * including aggregation information.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-25 */
public class Table extends Row<Table> implements Closeable {
  /* @formatter:off*/ @Override protected Table self() { return this; } /*@formatter:on*/

  public Table(final String name) {
    this(name, TableRenderer.builtin.values());
  }

  @SuppressWarnings("resource") public Table(final String name, final TableRenderer... rs) {
    this.name = name.toLowerCase();
    for (final TableRenderer r : rs)
      try {
        writers.add(new TableWriter(r, path()));
      } catch (final IOException ¢) {
        close();
        throw new RuntimeException(¢);
      }
  }

  public Table(final Class<?> c) {
    this(c.getSimpleName().toLowerCase().replace('_', '-').replaceAll("^.*?-", ""));
  }

  public Table(final Object o) {
    this(o.getClass());
  }

  private int length;
  public final String name;
  private final List<TableWriter> writers = new ArrayList<>();
  Statistic[] statisics = Statistic.values();
  final Map<String, RealStatistics> stats = new LinkedHashMap<>();

  void add(final Statistic... ss) {
    final List<Statistic> a = as.list(statisics);
    a.addAll(as.list(ss));
    set(a);
  }

  public String baseName() {
    return temporariesFolder + name + ".*";
  }

  @Override public void close() {
    if (!stats.isEmpty())
      for (final Statistic s : statisics) {
        for (final String key : keySet()) {
          final RealStatistics r = getRealStatistics(key);
          col(key, r == null || r.n() == 0 ? "" : box.it(s.of(r)));
        }
        for (final TableWriter ¢ : writers) {
          col((String) null, ¢.renderer.render(s));
          ¢.writeFooter(this);
        }
      }
    for (final TableWriter ¢ : writers)
      ¢.close();
  }

  public String description() {
    String $ = "Table named " + name + " produced in " + writers.size() + " formats (versions) in " + baseName() + "\n" + //
        "The table has " + length() + " data rows, each consisting of " + size() + " columns.\n" + //
        "Table header is  " + keySet() + "\n"; //
    if (!stats.isEmpty())
      $ += "The table consists of " + stats.size() + " numerical columns: " + stats.keySet() + "\n";
    int n = 0;
    for (final TableWriter ¢ : writers)
      $ += "\t " + ++n + ". " + ¢.fileName + "\n";
    
    return $;
  }

  RealStatistics getRealStatistics(final String key) {
    if (stats.get(key) == null)
      stats.put(key, new RealStatistics());
    return stats.get(key);
  }

  public int length() {
    return length;
  }

  public void nl() {
    for (final TableWriter ¢ : writers)
      ¢.write(this);
    reset();
  }

  private String path() {
    return temporariesFolder + name;
  }

  @Override public Table col(final String key, final double value) {
    getRealStatistics(key).record(value);
    return super.col(key, value);
  }

  @Override public Table col(final String key, final int value) {
    getRealStatistics(key).record(value);
    return super.col(key, value);
  }

  @Override public Table col(final String key, final long value) {
    getRealStatistics(key).record(value);
    super.col(key, value);
    return this;
  }
  void remove(final Statistic... ss) {
    final List<Statistic> a = as.list(statisics);
    a.removeAll(as.list(ss));
    set(a);
  }

  @Override protected Table reset() {
    for (final String ¢ : keySet())
      col(¢, "");
    col((String) null, ++length + "");
    return this;
  }

  private void set(final List<Statistic> ¢) {
    set(¢.toArray(new Statistic[¢.size()]));
  }

  void set(final Statistic... ¢) {
    statisics = ¢;
  }

  private static final long serialVersionUID = 1L;
  public static final String temporariesFolder = System.getProperty("java.io.tmpdir", "/tmp") + "/";
}
