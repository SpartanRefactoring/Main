package il.org.spartan.spartanizer.utils;
import java.util.stream.*;

import il.org.spartan.utils.*;

/** Organizes object of the type parameter in a tree, supplying a
 * {@link #descendants()} of all inner instances
 * @param <T>
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-11 */
public interface Recursive<@JD T> {
  default Stream<T> streamSelf() {
    return self() == null ? Stream.empty() : Stream.of(self());
  }

  default T self() {
    return null;
  }

  default Stream<T> descendants() {
    return Stream.empty();
  }

  /** An atomic recursive structure specializing {@link Recursive}
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-03-13 */
  interface Atomic<@JD T> extends Recursive<T> {
    @Override default Stream<T> descendants() {
      return streamSelf(); 
    } 
  }


  /** A compound recursive structure, specializing {@link Recursive} 
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-03-13 */
  interface Compound<@JD T> extends Recursive<T> {
    Iterable<Recursive<T>> children();
    @Override default T self() {
      return null;
    }
  }

  interface Postorder<E> extends Compound<E> {
    @Override default Stream<E> descendants() {
      Stream<E> $ = Stream.empty();
      for (final Recursive<E> ¢ : children())
        $ = Stream.concat(¢.descendants(), $);
       return Stream.concat($, streamSelf());
    }
  }

  /** A compound recursive structure enumerating {@link #descendants()} in
   * pre-order
   * @param <E>
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-03-13 */
  interface Preorder<E> extends Compound<E> {
    @Override default Stream<E> descendants() {
      Stream<E> $ = self() == null? Stream.empty(): Stream.of(self()); 
      for (final Recursive<E> ¢ : children())
        $ = Stream.concat($, ¢.descendants());
      return $;
    }/** A compound recursive structure enumerating {@link #descendants()} in
      * post-order
      * @param <E>
      * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
      * @since 2017-03-13 */
  }
}