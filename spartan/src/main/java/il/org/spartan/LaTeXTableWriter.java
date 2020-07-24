package il.org.spartan;

import java.util.*;

import fluent.ly.*;
import il.org.spartan.Aggregator.*;

/** import static il.org.spartan.utils.Box.*; import java.util.*; import
 * il.org.spartan.iteration.Iterables; import il.org.spartan.Aggregator.*; /**
 * @author Yossi Gil
 * @since Apr 5, 2012 */
public class LaTeXTableWriter extends CSVLineWriter {
  private static <K, V> void ensure(final Map<K, V> m, final K k, final V v) {
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
    for (final CSVLine nested : inner.values())
      $ |= nested.aggregating();
    return $;
  }
  @Override public final Iterable<Aggregation> aggregations() {
    final Set<Aggregation> $ = new LinkedHashSet<>();
    Iterables.addAll($, super.aggregations());
    for (final CSVLine nested : inner.values())
      Iterables.addAll($, nested.aggregations());
    return $;
  }
  @Override public String close() {
    if (!aggregating())
      return super.close();
    writer.writeln(super.renderer.headerEnd());
    for (final Aggregation ¢ : aggregations())
      writer.writeln(makeLine(collect(¢).values()));
    return super.close();
  }
  @Override public String header() {
    return renderer.allTop() + wrappingHeader() + makeLine(keys()) + renderer.headerEnd();
  }
  public CSVLine in(final Object innerTableName) {
    return in(innerTableName + "");
  }
  public CSVLine in(final String innerTableName) {
    ensure(inner, innerTableName, new CSVLine.Ordered());
    return inner.get(innerTableName);
  }
  @Override public Collection<String> keys() {
    final List<String> $ = new ArrayList<>(super.keys());
    for (final AbstractStringProperties nested : inner.values())
      Iterables.addAll($, nested.keys());
    return $;
  }
  @Override public Collection<String> values() {
    final List<String> $ = new ArrayList<>(super.values());
    for (final AbstractStringProperties nested : inner.values())
      Iterables.addAll($, nested.values());
    return $;
  }
  @Override protected String extension() {
    return ".tex";
  }
  private AbstractStringProperties collect(final Aggregation a) {
    final AbstractStringProperties $ = new ListProperties();
    addAggregates($, a);
    for (final CSVLine nested : inner.values())
      nested.addAggregates($, a);
    return $;
  }
  private String wrappingHeader() {
    if (inner.isEmpty())
      return "";
    final List<String> $ = new ArrayList<>();
    try (Formatter f = new Formatter()) {
      int column = size();
      $.add(String.format("\\multicolumn{%d}{c}{\\mbox{}}", idiomatic.box(column)));
      for (final String nestedTableName : inner.keySet()) {
        f.format("\\cmidrule(lr){%d-", idiomatic.box(column + 1));
        final int size = inner.get(nestedTableName).size();
        $.add(String.format("\\multicolumn{%d}{c}{%s}", idiomatic.box(size), nestedTableName));
        f.format("%d} ", idiomatic.box(column += size));
      }
      return makeLine($) + "\n" + f + "\n";
    }
  }
}
