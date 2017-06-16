package il.org.spartan.spartanizer.tipping.categories;

import java.util.*;
import java.util.function.*;

/** Representation of a DAG`
 * @author Yossi Gil
 * @since 2017-06-10 */
public class Hierarchy<T> {
  private final Map<T, Set<T>> children = new HashMap<>();
  private final Function<T, Set<T>> parents;

  public boolean contains(final T ¢) {
    return children.containsKey(¢);
  }
  public boolean isRoot(final T ¢) {
    return parents.apply(¢).isEmpty();
  }
  public void add(final T t) {
    if (contains(t))
      return;
    children.put(t, new LinkedHashSet<>()); // TODO yossi gil: problem with
                                            // an.empty.set() here
    for (final T parent : parents(t)) {
      if (!children.containsKey(parent))
        children.put(parent, an.empty.set());
      children.get(parent).add(t);
    }
  }
  public Hierarchy(final Function<T, Set<T>> parents) {
    this.parents = parents;
  }
  public final Set<T> children(final T ¢) {
    return children.get(¢);
  }
  public Set<T> nodes() {
    return children.keySet();
  }
  public final Set<T> parents(final T ¢) {
    return parents.apply(¢);
  }
}
