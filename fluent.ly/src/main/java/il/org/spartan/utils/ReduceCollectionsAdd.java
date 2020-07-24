package il.org.spartan.utils;

import java.util.Collection;

import org.eclipse.jdt.annotation.Nullable;

/**
 * TODO Yossi Gil: document class
 *
 * @author Yossi Gil
 * @since 2017-03-19
 */
public abstract class ReduceCollectionsAdd<T, C extends Collection<T>> extends NullReduce<C> {
  @Override public final C reduce(final C c1, final C c2) {
    if (c1 == null)
      return c2;
    if (c2 == null)
      return c1;
    c1.addAll(c2);
    return c1;
  }
}
