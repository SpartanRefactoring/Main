package il.org.spartan.utils;

import java.util.*;
import java.util.stream.*;

/** @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @author Ori Roth
 * @author Oren Afek
 * @since 2017-03-27 */
public interface Nested<@¢ T> extends Bolt<T> {
  interface Root<@¢ T> extends Nested<T>, Bolt.Atomic<T> {
    //
  }

  @Override default Compounder<T> compounder() {
    return (self, others) -> {
      Stream<T> $ = Stream.empty();
      for (final Bolt<T> ¢ : others)
        $ = Stream.concat(¢.stream(), streamSelf());
      return $;
    };
  }

  interface Compound<@¢ T> extends Nested<T>, Bolt.Compound<T> {
    Nested<T> parent();

    @Override default Iterable<Bolt<T>> next() {
      return Arrays.asList(parent());
    }
  }
}
