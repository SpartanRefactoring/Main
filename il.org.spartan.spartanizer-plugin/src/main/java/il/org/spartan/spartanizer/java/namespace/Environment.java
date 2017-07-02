package il.org.spartan.spartanizer.java.namespace;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.utils.*;

/** Interface to environment. Holds all the names defined till current PC. In
 * other words the 'names Environment' at every point of the program tree.
 * @author Yossi Gil
 * @since Dec 25, 2016 */
public interface Environment {
  /** @return true iff this instance doesn't have an entry with a given name. */
  default boolean doesntHave(final String name) {
    return !has(name);
  }
  /** @return true iff this instance is empty. */
  boolean empty();
  List<Entry<String, Binding>> entries();
  default Collection<Entry<String, Binding>> fullEntries() {
    final Collection<Entry<String, Binding>> $ = as.list(entries());
    if (nest() != null)
      $.addAll(nest().fullEntries());
    return $;
  }
  /** Get full path of the current this instance (all scope hierarchy). Used for
   * full names of the variables. */
  default String fullName() {
    final String $ = nest() == null || nest() == NULL ? null : nest().fullName();
    return ($ == null ? "" : $ + ".") + name().replaceAll("  .*$", "");
  }
  /** @return all the full names of the this instance. */
  default Collection<String> fullNames() {
    final Collection<String> $ = new LinkedHashSet<>(keys());
    if (nest() != null)
      $.addAll(nest().fullNames());
    return $;
  }
  default int fullSize() {
    return size() + (nest() == null ? 0 : nest().fullSize());
  }
  /** @return null iff the name is not in use in the this instance */
  Binding get(String name);
  /** Answer the question whether the name is in use in the current this
   * instance */
  boolean has(String name);
  /** @return null iff the name is not hiding anything from outer scopes,
   *         otherwise Information about hided instance (with same name) */
  default Binding hiding(final String name) {
    return nest() == null ? null : nest().get(name);
  }
  /** @return The names used in the current scope. */
  Set<String> keys();
  String name();
  /** @return null at the most outer block. This method is similar to the
   *         'next()' method in a linked list. */
  Environment nest();
  /** Should return the hidden entry, or null if no entry hidden by this one.
   * Note: you will have to assume multiple definitions in the same block, this
   * is a compilation error, but nevertheless, let a later entry with of a
   * certain name to "hide" a former entry with the same name. */
  default Binding put(final String name, final Binding b) {
    throw new IllegalArgumentException(name + "/" + b);
  }
  int size();
  /** Used when new block (scope) is opened. */
  Namespace spawn();

  /** The Environment structure is in some like a Linked list, where EMPTY is
   * like the NULL at the end. */
  Environment NULL = new Environment() {
    @Override public boolean empty() {
      return true;
    }
    @Override public List<Entry<String, Binding>> entries() {
      return Collections.emptyList();
    }
    @Override public Binding get(@SuppressWarnings("unused") final String name) {
      return null;
    }
    @Override public boolean has(@SuppressWarnings("unused") final String name) {
      return false;
    }
    @Override public Set<String> keys() {
      return Collections.emptySet();
    }
    @Override public String name() {
      return "";
    }
    @Override public Environment nest() {
      return null;
    }
    @Override public int size() {
      return 0;
    }
    @Override public Namespace spawn() {
      return new Namespace(this);
    }
    @Override public String toString() {
      return "null";
    }
  };
  LinkedHashSet<Entry<String, Binding>> upEnv = new LinkedHashSet<>();

  /** @param ¢ JD
   * @return All declarations in given {@link Statement}, without entering the
   *         contained ({@link Block}s. If the {@link Statement} is a
   *         {@link Block}, (also IfStatement, ForStatement and so on...) return
   *         empty Collection. */
  static Collection<Entry<String, Binding>> declarationsOf(final Statement ¢) {
    final Collection<Entry<String, Binding>> $ = an.empty.list();
    if (¢.getNodeType() != VARIABLE_DECLARATION_STATEMENT)
      return $;
    $.addAll(declarationsOf(az.variableDeclrationStatement(¢)));
    return $;
  }
  static Collection<Entry<String, Binding>> declarationsOf(final VariableDeclarationStatement s) {
    final Collection<Entry<String, Binding>> $ = an.empty.list();
    final type t = type.baptize(Trivia.condense(type(s)));
    final String path = fullName(s);
    $.addAll(fragments(s).stream().map(λ -> new MapEntry<>(path + "." + λ.getName(), makeBinding(λ, t))).collect(toList()));
    return $;
  }
  /** @return set of entries declared in the node, including all hiding. */
  static Set<Entry<String, Binding>> declaresDown(final ASTNode ¢) {
    // Holds the declarations in the subtree and relevant siblings.
    final LinkedHashSet<Entry<String, Binding>> $ = new LinkedHashSet<>();
    ¢.accept(new EnvironmentVisitor($));
    return $;
  }
  /** Gets declarations made in ASTNode's Ancestors */
  static LinkedHashSet<Entry<String, Binding>> declaresUp(final ASTNode n) {
    for (Block PB = getParentBlock(n); PB != null; PB = getParentBlock(PB))
      statements(PB).forEach(λ -> upEnv.addAll(declarationsOf(λ)));
    return upEnv;
  }
  static String fullName(final ASTNode ¢) {
    return ¢ == null ? "" : fullName(¢.getParent()) + name(¢);
  }
  /** Spawns the first nested this instance. Should be used when the first block
   * is opened. */
  static Environment genesis() {
    return NULL.spawn();
  }
  static Binding get(final LinkedHashSet<Entry<String, Binding>> ss, final String s) {
    return ss.stream().filter(λ -> s.equals(λ.getKey())).map(Entry::getValue).findFirst().orElse(null);
  }
  static Binding getHidden(final String s) {
    for (String ¢ = parentNameScope(s); ¢ != null && !¢.isEmpty(); ¢ = parentNameScope(¢)) {
      final Binding $ = get(upEnv, ¢ + "." + s.substring(s.lastIndexOf(".") + 1));
      if ($ != null)
        return $;
    }
    return null;
  }
  static Block getParentBlock(final ASTNode ¢) {
    return az.block(¢.getParent());
  }
  static Binding makeBinding(final VariableDeclarationFragment ¢, final type t) {
    return new Binding(¢.getParent(), getHidden(fullName(¢.getName())), ¢, t);
  }
  static String name(@SuppressWarnings("unused") final ASTNode __) {
    return "???";
  }
  static String name(final VariableDeclarationFragment ¢) {
    return ¢.getName() + "";
  }
  static Namespace of(final ASTNode n) {
    if (n == null)
      return null;
    for (final ASTNode ¢ : ancestors.of(n)) {
      final Namespace ret = property.obtain(Namespace.class).from(¢);
      if (ret != null)
        return ret;
    }
    Environment.NULL.spawn().fillScope(n.getRoot());
    for (final ASTNode ¢ : ancestors.of(n)) {
      final Namespace $ = property.obtain(Namespace.class).from(¢);
      if ($ != null)
        return $;
    }
    return null;
  }
  static String parentNameScope(final String ¢) {
    return ¢ == null || ¢.isEmpty() ? "" : ¢.substring(0, ¢.lastIndexOf("."));
  }
  /** @return set of entries used in a given node. this includes the list of
   *         entries that were defined in the node */
  static Set<Entry<String, Binding>> uses(@SuppressWarnings("unused") final ASTNode __) {
    return new LinkedHashSet<>();
  }
}
