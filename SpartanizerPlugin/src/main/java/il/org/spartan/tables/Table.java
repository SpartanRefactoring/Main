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
    this(o.getClass());
  }

  public Table(final Class<?> c) {
    this(classToNormalizedFileName(c));
  }

  private Table(final String name) {
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
        final String key = lastEmptyColumn();
        for (final RecordWriter ¢ : writers) {
          put(key, ¢.renderer.render(s));
          ¢.writeFooter(this);
        }
      }
    writers.forEach(RecordWriter::close);
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
    stats.computeIfAbsent(key, k -> new RealStatistics());
    return stats.get(key);
  }

  public int length() {
    return length;
  }

  public void nl() {
    writers.forEach(¢ -> ¢.write(this));
    reset();
  }

  private String path() {
    return temporariesFolder + name;
  }

  public Table noStatistics() {
    statisics = new Statistic[0];
    return this;
  }

  public Table remove(final Statistic... ¢) {
    final List<Statistic> $ = as.list(statisics);
    $.removeAll(as.list(¢));
    return set($);
  }

  public Table add(final Statistic... ¢) {
    final List<Statistic> $ = as.list(statisics);
    $.addAll(as.list(¢));
    return set($);
  }

  @Override protected Table reset() {
    keySet().forEach(¢ -> put(¢, ""));
    put((String) null, ++length + "");
    return this;
  }

  /* @formatter:off*/ @Override protected Table self() { return this; } /*@formatter:on*/

  private Table set(final List<Statistic> ¢) {
    return set(¢.toArray(new Statistic[¢.size()]));
  }

  Table set(final Statistic... ¢) {
    statisics = ¢;
    return this;
  }

  private static final long serialVersionUID = 1L;
  public static final String temporariesFolder = System.getProperty("java.io.tmpdir", "/tmp/");

  public static String classToNormalizedFileName(final Class<?> ¢) {
    return classToNormalizedFileName(¢.getSimpleName());
  }

  static String classToNormalizedFileName(final String className) {
    return separate.these(lisp.rest(as.iterable(namer.components(className)))).by('-').toLowerCase();
  }
}
