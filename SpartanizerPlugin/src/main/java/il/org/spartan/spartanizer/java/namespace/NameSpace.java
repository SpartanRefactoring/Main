package il.org.spartan.spartanizer.java.namespace;

import java.util.*;

/** Dictionary with a parent. Insertions go the current node, searches start at
 * the current note and Delegate to the parent unless it is null.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-28 */
final class NameSpace implements Environment {
  NameSpace(final Environment nest) {
    this(nest, "");
  }

  public final String name;
  public final Environment nest;

  public NameSpace(final Environment nest, final String name) {
    this.nest = nest;
    this.name = name;
  }

  public final Map<String, Binding> flat = new LinkedHashMap<>();

  /** @return <code><b>true</b></code> <em>iff</em> {@link Environment} is
   *         empty. */
  @Override public boolean empty() {
    return flat.isEmpty() && nest.empty();
  }

  /** @return Map entries used in the current scope. */
  @Override public List<Map.Entry<String, Binding>> entries() {
    return new ArrayList<>(flat.entrySet());
  }

  /** @return The information about the name in current {@link Environment} */
  @Override public Binding get(final String identifier) {
    final Binding $ = flat.get(identifier);
    return $ != null ? $ : nest.get(identifier);
  }

  /** Check whether the {@link Environment} already has the name. */
  @Override public boolean has(final String identifier) {
    return flat.containsKey(identifier) || nest.has(identifier);
  }

  /** @return names used the {@link Environment} . */
  @Override public LinkedHashSet<String> keys() {
    return new LinkedHashSet<>(flat.keySet());
  }

  /** One step up in the {@link Environment} tree. Funny but it even sounds like
   * next(). */
  @Override public Environment nest() {
    return nest;
  }

  /** Add name to the current scope in the {@link Environment} . */
  @Override public Binding put(final String identifier, final Binding value) {
    flat.put(identifier, value);
    assert !flat.isEmpty();
    return hiding(identifier);
  }

  @Override public String name() {
    return name;
  }

  @Override public int size() {
    return flat.size();
  }
}