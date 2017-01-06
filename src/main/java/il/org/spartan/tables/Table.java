package il.org.spartan.tables;

import java.io.*;
import java.util.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.statistics.*;

/** A relation is just another name for a table that contains elements of type
 * {@link Record}. This class provides fluent API for generating tables,
 * including aggregation information.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-25 */
public class Table extends Row<Table> implements Closeable {
  public Table(final Object o) {
    this(classToNormalizedFileName(o.getClass()));
  }

  public Table(final String name) {
    this(name, TableRenderer.builtin.values());
  }

  @SuppressWarnings("resource") public Table(final String name, final TableRenderer... rs) {
    this.name = name.toLowerCase();
    for (final TableRenderer r : rs)
      try {
        writers.add(new RecordWriter(r, path()));
      } catch (final IOException ¢) {
        close();
        throw new RuntimeException(¢);
      }
  }

  private int length;
  public final String name;
  Statistic[] statisics = Statistic.values();
  final Map<String, RealStatistics> stats = new LinkedHashMap<>();
  private final List<RecordWriter> writers = new ArrayList<>();

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
          put(key, r == null || r.n() == 0 ? "" : box.it(s.of(r)));
        }
        String key = lastEmptyColumn();
        for (final RecordWriter ¢ : writers) {
          put(key, ¢.renderer.render(s));
          ¢.writeFooter(this);
        }
      }
    for (final RecordWriter ¢ : writers)
      ¢.close();
  }

  private String lastEmptyColumn() {
    String $ = null;
    for (final String key : keySet()) {
      final RealStatistics r = getRealStatistics(key);
      if (r != null && r.n() != 0)
        break;
      $ = key;
    }
      return $;
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

  public String description() {
    String $ = "Table named " + name + " produced in " + writers.size() + " formats (versions) in " + baseName() + "\n" + //
        "The table has " + length() + " data rows, each consisting of " + size() + " columns.\n" + //
        "Table header is  " + keySet() + "\n"; //
    if (!stats.isEmpty())
      $ += "The table consists of " + stats.size() + " numerical columns: " + stats.keySet() + "\n";
    int n = 0;
    for (final RecordWriter ¢ : writers)
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
    for (final RecordWriter ¢ : writers)
      ¢.write(this);
    reset();
  }

  private String path() {
    return temporariesFolder + name;
  }

  void remove(final Statistic... ss) {
    final List<Statistic> a = as.list(statisics);
    a.removeAll(as.list(ss));
    set(a);
  }

  @Override protected Table reset() {
    for (final String ¢ : keySet())
      put(¢, "");
    put((String) null, ++length + "");
    return this;
  }

  /* @formatter:off*/ @Override protected Table self() { return this; } /*@formatter:on*/

  private void set(final List<Statistic> ¢) {
    set(¢.toArray(new Statistic[¢.size()]));
  }

  void set(final Statistic... ¢) {
    statisics = ¢;
  }

  private static final long serialVersionUID = 1L;
  public static final String temporariesFolder = System.getProperty("java.io.tmpdir", "/tmp") + "/";

  public static String classToNormalizedFileName(final Class<? extends Object> class1) {
    return classToNormalizedFileName(class1.getSimpleName());
  }

  static String classToNormalizedFileName(final String className) {
    return separate.these(lisp.rest(as.iterable(namer.components(className)))).by('-').toLowerCase();
  }
}
