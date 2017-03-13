package il.org.spartan.spartanizer.utils;
import java.util.stream.*;

import il.org.spartan.utils.*;

/** Organizes object of the type parameter in a tree, supplying a
 * {@link #descendants()} of all inner instances
 * @param <T>
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-11 */
public interface Recursive<T> {
  default Stream<T> descendants() {
    return Stream.empty();
  }

  /** An atomic recursive structure 
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2017-03-13 */
  interface Atomic<@JD T> extends Recursive<T> {
    @Override default Stream<T> descendants() {
      return Stream.of(self()); 
    }

    T self(); 
  }


  /** A compound recursive structure
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2017-03-13 */
  interface Compound<@JD T> extends Recursive<T> {
    Iterable<Recursive<T>> children();
  }

  interface Postorder<E> extends Compound<E> {
    @Override default Stream<E> descendants() {
      Stream<E> $ = Stream.empty();
      for (final Recursive<E> ¢ : children())
        $ = Stream.concat(¢.descendants(), $);
      return $;
    }
  }

  /** A compound recursive structure enumerating {@link #descendants()} in
   * pre-order
   * @param <E>
   * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
   * @since 2017-03-13 */
  interface Preorder<E> extends Compound<E> {
    @Override default Stream<E> descendants() {
      Stream<E> $ = Stream.empty();
      for (final Recursive<E> ¢ : children())
        $ = Stream.concat($, ¢.descendants());
      return $;
    }/** A compound recursive structure enumerating {@link #descendants()} in
      * post-order
      * @param <E>
      * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
      * @since 2017-03-13 */
  }
}