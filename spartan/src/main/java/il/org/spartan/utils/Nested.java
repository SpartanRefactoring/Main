package il.org.spartan.utils;

import java.util.stream.*;

/** nested element in a hierarchical structure
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @author Ori Roth
 * @author Oren Afek
 * @since 2017-03-27 */
public interface Nested<T> extends Duplo<T> {
  interface Root<T> extends Nested<T>, Duplo.Atomic<T> {
    //
  }

  @Override default Merge<T> merge() {
    // TODO looks buggy to me --yg
    return (self, others) -> {
      Stream<T> $ = Stream.empty();
      for (final Duplo<T> ¢ : others)
        $ = Stream.concat(¢.fullStream(), selfStream());
      return $;
    };
  }

  interface Compound<T> extends Nested<T>, Duplo.Compound<T> {
    Nested<T> parent();
    @Override default Iterable<Duplo<T>> components() {
      return a.singleton.list(parent());
    }
  }
}
