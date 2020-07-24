package il.org.spartan.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;

import il.org.spartan.utils.Example.Ignores;

/** A collection of {@link Example}
 * @author Yossi Gil
 * @since 2017-04-01 */
public class Examples implements Iterable<Example> {
  private final List<Example> inner = an.empty.list();

  public Examples add(final Example ¢) {
    inner.add(¢);
    return this;
  }
  @Override public Iterator<Example> iterator() {
    return inner.iterator();
  }
  @Override public Spliterator<Example> spliterator() {
    return inner.spliterator();
  }
  public Converter convert(final String from) {
    return to -> add(new Example.Converts() {
      @Override public String from() {
        return from;
      }
      @Override public String to() {
        return to;
      }
    });
  }
  public Examples ignores(final String code) {
    return add((Ignores) () -> code);
  }

  /** Auxiliary class for FAPI
   * @author Yossi Gil
   * @since 2017-03-07 */
  public interface Converter {
    Examples to(String to);
  }
}
