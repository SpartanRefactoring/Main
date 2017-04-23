package il.org.spartan.utils;

import java.util.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-03-19 */
public abstract class ReduceCollectionsAdd<T, C extends Collection<T>> extends Reduce<C> {
  @Override public final C reduce(final C c1, final C c2) {
    c1.addAll(c2);
    return c1;
  }
}
