package il.org.spartan.utils;

import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNull;

/**
 * @see Compound
 * @see Atomic
 * @author Yossi Gil
 * @since 2017-03-11
 */
public interface Recursive<T> extends Duplo<T> {
  /**
   * An atomic recursive structure specializing {@link Recursive}
   *
   * @author Yossi Gil
   * @since 2017-03-13
   */
  interface Atomic<T> extends Recursive<T>, Duplo.Atomic<T> {
    //
  }

  /**
   * A compound recursive structure, specializing {@link Recursive}
   *
   * @author Yossi Gil
   * @since 2017-03-13
   */
  interface Compound<T> extends Recursive<T>, Duplo.Compound<T> {
    Iterable<Recursive<T>> children();

    @Override default Iterable<? extends Duplo<T>> components() {
      return children();
    }
  }

  interface Postorder<E> extends Compound<E> {
    @Override default Merge<E> merge() {
      return (self, others) -> {
        Stream<E> $ = Stream.empty();
        for (final @NonNull Duplo<E> ¢ : others)
          $ = Stream.concat(¢.fullStream(), $);
        return self == null ? $ : Stream.concat($, Stream.of(self));
      };
    }
  }

  /**
   * A compound recursive structure enumerating {@link #components()} in pre-order
   *
   * @param <E>
   * @author Yossi Gil
   * @since 2017-03-13
   */
  interface Preorder<E> extends Compound<E> {
    @Override default Merge<E> merge() {
      return (self, others) -> {
        Stream<E> $ = self == null ? Stream.empty() : Stream.of(self);
        for (final @NonNull Duplo<E> ¢ : others)
          $ = Stream.concat($, ¢.fullStream());
        return $;
      };
    }
  }
}
