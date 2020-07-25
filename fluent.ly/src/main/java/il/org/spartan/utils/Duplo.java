package il.org.spartan.utils;

import java.util.stream.Stream;

/**
 * encapsulates elements of the __ parameter, to be organizable in a
 * hierarchical structure
 *
 * @param <T> Type stored in each element
 * @author Yossi Gil
 * @since 2017-03-27
 */
public interface Duplo<T> {
  default Merge<T> merge() {
    return Merge.empty();
  }

  /** return the element stored in this instance; */
  T self();

  /**
   * do not override
   *
   * @return a stream representation of the element stored in this instance
   */
  default Stream<T> selfStream() {
    return self() == null ? Stream.empty() : Stream.of(self());
  }

  /**
   * return a stream of elements encapsulated by in this instance
   *
   * @return a stream representation of the element stored in this instance
   */
  default Stream<T> fullStream() {
    return Stream.empty();
  }

  /**
   * A {@link Duplo} which has no neighbors
   *
   * @param <T>
   * @author Yossi Gil
   * @since 2017-03-30
   */
  interface Atomic<T> extends Duplo<T> {
    @Override default Stream<T> fullStream() {
      return selfStream();
    }
  }

  interface Compound<T> extends Duplo<T> {
    Iterable<? extends Duplo<T>> components();

    @Override default Stream<T> fullStream() {
      return merge().append(self(), components());
    }
  }

  @FunctionalInterface interface Merge<T> {
    static <T> Merge<T> empty() {
      return (self, others) -> Stream.empty();
    }

    Stream<T> append(T self, Iterable<? extends Duplo<T>> others);
  }
}
