package il.org.spartan.spartanizer.tipping.categories;

import java.util.*;
import java.util.function.*;

/** Representation of a DAG`
 * @author Yossi Gil
 * @since 2017-06-10 */
public class Hierarchy<T> {
  public final Map<T, Set<T>> children = new HashMap<>();
  private final Function<T, Set<T>> parents;
  
  public boolean contains (T ¢) {
    return children.containsKey(¢);
  }
  public void add(T t) {
    if (contains(t))
      return;
    children.put(t, an.empty.set());
    for (T parent: parents(t)) {
      if (!children.containsKey(parent))
        children.put(parent, an.empty.set());
      children.get(parent).add(t);
    }
  } 

  public Hierarchy(Function<T, Set<T>> parents) {
    this.parents = parents;
  }
  public final Set<T> children(T ¢) {
    return children.get(¢);
  }
  public Set<T> nodes() {
    return children.keySet();
  }
  public final Set<T> parents(T ¢) {
    return parents.apply(¢);
  }
}
