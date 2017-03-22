package il.org.spartan.utils;

import java.util.stream.*;

import il.org.spartan.spartanizer.java.namespace.*;
import junit.framework.*;

/** Organizes objects of the type parameter in a tree, supplying a
 * {@link #descendants()} of all objects contained in a sub-tree. Object of the
 * type parameter may present on all nodes on of this tree, including inner
 * nodes.
 * <p>
 * TODO Dor Maayan & Oran Gilboa: first assignment TODO Oren Afek & Ori Roth &
 * Matan Peled: first assignments
 * <ol>
 * <li>Implement interface {@link Nested} for the same concept, except that
 * stream return ancestors.
 * <li>Factor commonalities between the new interface and this interface. What
 * should the new interface be called?
 * <li>Create a new interface that unifies {@link Nested} and {@link Recursive},
 * to be called what?
 * <li>Re-implement class {@link Environment} using the diamond hierarchy that
 * you are
 * <li>Revise material on design patterns. Do you think interface names should
 * be changed? How? Write your answer as JavaDOC: don't bother with formatting.
 * <li>Otherwise, don't touch any documentation, before you attend the JavaDOC
 * lecture. Do add {@code @author} and {@code @since}, but nothing more. You
 * will have to document this interface, and all methods, but only after that.
 * <li>If there are {@code }{@link Test} methods, make sure they run. Don't add
 * any tests, even if you think you know what you are doing, before attending
 * the TDD lecture.
 * <li>Apply self judgment, don't come running to me with every little dilemma
 * you have.
 * </ol>
 * @see Compound
 * @see Atomic
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-11 */
public interface Recursive<@¢ T> extends Streamer<T> {
  /** An atomic recursive structure specializing {@link Recursive}
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-03-13 */
  interface Atomic<@¢ T> extends Recursive<T>, Streamer.Atomic<T> {
    //
  }

  /** A compound recursive structure, specializing {@link Recursive}
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-03-13 */
  interface Compound<@¢ T> extends Recursive<T>, Streamer.Compound<T> {
    Iterable<Recursive<T>> children();

    @Override default Iterable<? extends Streamer<T>> next() {
      return children();
    }
  }

  interface Postorder<E> extends Compound<E> {
    @Override default Stream<E> stream() {
      Stream<E> $ = Stream.empty();
      for (final Recursive<E> ¢ : children())
        $ = Stream.concat(¢.stream(), $);
      return Stream.concat($, streamSelf());
    }
  }

  /** A compound recursive structure enumerating {@link #descendants()} in
   * pre-order
   * @param <E>
   * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @since 2017-03-13 */
  interface Preorder<E> extends Compound<E> {
    @Override default Stream<E> stream() {
      Stream<E> $ = self() == null ? Stream.empty() : Stream.of(self());
      for (final Recursive<E> ¢ : children())
        $ = Stream.concat($, ¢.stream());
      return $;
    }/* A compound recurkksive structure enumerating {@link #descendants()} in
      * post-order
      *
      * @param <E>
      *
      * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
      *
      * @since 2017-03-13 */
  }
}
