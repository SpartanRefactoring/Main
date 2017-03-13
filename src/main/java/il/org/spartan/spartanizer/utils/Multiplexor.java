package il.org.spartan.spartanizer.utils;

import java.util.stream.*;

import il.org.spartan.spartanizer.tipping.*;

/** Organizes {@link Rule}s in a tree, supplying a stream of rules ready to be
 * applied.
 * @param <N>
 * @param <T>
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-11 */
public interface Multiplexor<E> {
  default Stream<E> stream() {
    return Stream.empty();
  }

  class Linear<E> implements Multiplexor<E> {
    protected final Multiplexor<E>[] inner;

    @SafeVarargs public Linear(final Multiplexor<E>... inner) {
      this.inner = inner;
    }

    @Override public Stream<E> stream() {
      Stream<E> $ = Stream.empty();
      for (final Multiplexor<E> ¢ : inner)
        $ = Stream.concat($, ¢.stream());
      return $;
    }
  }
}