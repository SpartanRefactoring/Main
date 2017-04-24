package il.org.spartan;

import static nano.ly.box.*;

import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.Aggregator.*;
import il.org.spatan.iteration.*;

/** import static il.org.spartan.utils.Box.*; import java.util.*; import
 * il.org.spartan.iteration.Iterables; import il.org.spartan.Aggregator.*; /**
 * @author Yossi Gil
 * @since Apr 5, 2012 */
public class LaTeXTableWriter extends CSVLineWriter {
  private static <K, V> void ensure(@NotNull final Map<K, V> m, final K k, final V v) {
    m.putIfAbsent(k, v);
  }

  private final Map<String, CSVLine> inner = new LinkedHashMap<>();

  /** Instantiate {@link LaTeXTableWriter}. */
  public LaTeXTableWriter() {
    super(Renderer.LaTeX);
  }

  /** Instantiate {@link LaTeXTableWriter}.
   * @param fileName */
  public LaTeXTableWriter(final String fileName) {
    super(fileName, Renderer.LaTeX);
  }

  @Override public boolean aggregating() {
    boolean $ = super.aggregating();
    for (@NotNull final CSVLine nested : inner.values())
      $ |= nested.aggregating();
    return $;
  }

  @Override @NotNull public final Iterable<Aggregation> aggregations() {
    @NotNull final Set<Aggregation> $ = new LinkedHashSet<>();
    Iterables.addAll($, super.aggregations());
    for (@NotNull final CSVLine nested : inner.values())
      Iterables.addAll($, nested.aggregations());
    return $;
  }

  @Override public String close() {
    if (!aggregating())
      return super.close();
    writer.writeln(super.renderer.headerEnd());
    for (@NotNull final Aggregation ¢ : aggregations())
      writer.writeln(makeLine(collect(¢).values()));
    return super.close();
  }

  @Override @NotNull public String header() {
    return renderer.allTop() + wrappingHeader() + makeLine(keys()) + renderer.headerEnd();
  }

  public CSVLine in(final Object innerTableName) {
    return in(innerTableName + "");
  }

  public CSVLine in(final String innerTableName) {
    ensure(inner, innerTableName, new CSVLine.Ordered());
    return inner.get(innerTableName);
  }

  @Override @NotNull public Collection<String> keys() {
    @NotNull final List<String> $ = new ArrayList<>(super.keys());
    for (@NotNull final AbstractStringProperties nested : inner.values())
      Iterables.addAll($, nested.keys());
    return $;
  }

  @Override @NotNull public Collection<String> values() {
    @NotNull final List<String> $ = new ArrayList<>(super.values());
    for (@NotNull final AbstractStringProperties nested : inner.values())
      Iterables.addAll($, nested.values());
    return $;
  }

  @Override @NotNull protected String extension() {
    return ".tex";
  }

  @NotNull private AbstractStringProperties collect(@NotNull final Aggregation a) {
    @NotNull final AbstractStringProperties $ = new ListProperties();
    addAggregates($, a);
    for (@NotNull final CSVLine nested : inner.values())
      nested.addAggregates($, a);
    return $;
  }

  @NotNull private String wrappingHeader() {
    if (inner.isEmpty())
      return "";
    @NotNull final List<String> $ = new ArrayList<>();
    @NotNull final Formatter f = new Formatter();
    int column = size();
    $.add(String.format("\\multicolumn{%d}{c}{\\mbox{}}", box(column)));
    for (final String nestedTableName : inner.keySet()) {
      f.format("\\cmidrule(lr){%d-", box(column + 1));
      final int size = inner.get(nestedTableName).size();
      $.add(String.format("\\multicolumn{%d}{c}{%s}", box(size), nestedTableName));
      f.format("%d} ", box(column += size));
    }
    return makeLine($) + "\n" + f + "\n";
  }
}
