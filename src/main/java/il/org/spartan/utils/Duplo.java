package il.org.spartan.utils;

import java.util.stream.*;

/** encapsulates elements of the type parameter, to be organizable in a
 * hierarchical structure
 * @param <T> Type stored in each element
 * @author Yossi Gil
 * @since 2017-03-27 */
public interface Duplo<T> {
  /** return the element stored in this instance; */
  T self();

  default Compounder<T> compounder() {
    return Compounder.empty();
  }

  default Stream<T> streamSelf() {
    return self() == null ? Stream.empty() : Stream.of(self());
  }

  default Stream<T> stream() {
    return Stream.empty();
  }

  interface Atomic<T> extends Duplo<T> {
    @Override default Stream<T> stream() {
      return streamSelf();
    }
  }

  interface Compound<T> extends Duplo<T> {
    Iterable<? extends Duplo<T>> next();

    @Override default Stream<T> stream() {
      return compounder().compound(self(), next());
    }
  }

  @FunctionalInterface
  interface Compounder<T> {
    Stream<T> compound(T self, Iterable<? extends Duplo<T>> others);

    static <T> Compounder<T> empty() {
      return (self, others) -> Stream.empty();
    }
  }
}
