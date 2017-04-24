package il.org.spartan.utils;

import java.util.stream.*;

/** encapsulates elements of the __ parameter, to be organizable in a
 * hierarchical structure
 * @param <T> Type stored in each element
 * @author Yossi Gil
 * @since 2017-03-27 */
public interface Duplo<T> {
  default NeighborsMerger<T> neighborsMerger() {
    return NeighborsMerger.empty();
  }

  /** return the element stored in this instance; */
  T self();

  /** do not override
   * @return a stream representation of the element stored in this instance */
  default Stream<T> selfStream() {
    return self() == null ? Stream.empty() : Stream.of(self());
  }

  /** return a stream of elements encapsulated by in this instance
   * @return a stream representation of the element stored in this instance */
  default Stream<T> neighborsStream() {
    return Stream.empty();
  }

  /** A {@link Duplo} which has no neighbors
   * @param <T>
   * @author Yossi Gil
   * @since 2017-03-30 */
  interface Atomic<T> extends Duplo<T> {
    @Override default Stream<T> neighborsStream() {
      return selfStream();
    }
  }

  interface Compound<T> extends Duplo<T> {
    Iterable<? extends Duplo<T>> neighbors();

    @Override default Stream<T> neighborsStream() {
      return neighborsMerger().append(self(), neighbors());
    }
  }

  @FunctionalInterface interface NeighborsMerger<T> {
    static <T> NeighborsMerger<T> empty() {
      return (self, others) -> Stream.empty();
    }

    Stream<T> append(T self, Iterable<? extends Duplo<T>> others);
  }
}
