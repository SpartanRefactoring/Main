package il.org.spartan.utils;

import java.util.*;

import org.jetbrains.annotations.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-19 */
public abstract class ReduceCollectionsAdd<T, C extends Collection<T>> extends Reduce<C> {
  @Override @NotNull public final C reduce(@NotNull final C c1, @NotNull final C c2) {
    c1.addAll(c2);
    return c1;
  }
}
