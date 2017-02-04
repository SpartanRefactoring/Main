package il.org.spartan.tables;

import java.io.*;
import java.util.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.statistics.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A relation is just another name for a table that contains elements of type
 * {@link Record}. This class provides fluent API for generating tables,
 * including aggregation information.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-25 */
public class Table extends Row<Table> implements Closeable {
  public Table(@NotNull final Object o) {
    this(o.getClass());
  }

  public Table(@NotNull final Class<?> c) {
    this(classToNormalizedFileName(c));
  }

  public Table(@NotNull final String name) {
    this(name, TableRenderer.builtin.values());
  }

  @SuppressWarnings("resource") public Table(@NotNull final String name, final TableRenderer... rs) {
    this.name = name.toLowerCase();
    as.list(rs).forEach(r -> {
      try {
        writers.add(new RecordWriter(r, path()));
      } catch (@NotNull final IOException ¢) {
        close();
        throw new RuntimeException(¢);
      }
    });
  }

  private int length;
  @NotNull public final String name;
  Statistic[] statisics = Statistic.values();
  final Map<String, RealStatistics> stats = new LinkedHashMap<>();
  private final List<RecordWriter> writers = new ArrayList<>();

  @NotNull public String baseName() {
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

  @Nullable private String lastEmptyColumn() {
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

  @Override @NotNull public Table col(final String key, final long value) {
    getRealStatistics(key).record(value);
    super.col(key, value);
    return this;
  }

  @NotNull public String description() {
    String $ = "Table named " + name + " produced in " + writers.size() + " formats (versions) in " + baseName() + "\n" + //
        "The table has " + length() + " data rows, each consisting of " + size() + " columns.\n" + //
        "Table header is  " + keySet() + "\n"; //
    if (!stats.isEmpty())
      $ += "The table consists of " + stats.size() + " numerical columns: " + stats.keySet() + "\n";
    final Int n = new Int();
    return $ += writers.stream().map(λ -> "\t " + ++n.inner + ". " + λ.fileName + "\n").reduce((x, y) -> x + y).get();
  }

  RealStatistics getRealStatistics(final String key) {
    stats.computeIfAbsent(key, λ -> new RealStatistics());
    return stats.get(key);
  }

  public int length() {
    return length;
  }

  public void nl() {
    writers.forEach(λ -> λ.write(this));
    reset();
  }

  @NotNull private String path() {
    return temporariesFolder + name;
  }

  @NotNull public Table noStatistics() {
    statisics = new Statistic[0];
    return this;
  }

  @NotNull public Table remove(final Statistic... ¢) {
    final List<Statistic> $ = as.list(statisics);
    $.removeAll(as.list(¢));
    return set($);
  }

  @NotNull public Table add(final Statistic... ¢) {
    final List<Statistic> $ = as.list(statisics);
    $.addAll(as.list(¢));
    return set($);
  }

  @Override @NotNull protected Table reset() {
    keySet().forEach(λ -> put(λ, ""));
    put(null, ++length + "");
    return this;
  }

  /* @formatter:off*/ @Override
  @NotNull protected Table self() { return this; } /*@formatter:on*/

  @NotNull private Table set(@NotNull final List<Statistic> ¢) {
    return set(¢.toArray(new Statistic[¢.size()]));
  }

  @NotNull Table set(final Statistic... ¢) {
    statisics = ¢;
    return this;
  }

  private static final long serialVersionUID = 1L;
  public static final String temporariesFolder = System.getProperty("java.io.tmpdir", "/tmp") + System.getProperty("file.separator", "/");

  public static String classToNormalizedFileName(@NotNull final Class<?> ¢) {
    return classToNormalizedFileName(¢.getSimpleName());
  }

  static String classToNormalizedFileName(final String className) {
    return separate.these(lisp.rest(as.iterable(namer.components(className)))).by('-').toLowerCase();
  }
}
