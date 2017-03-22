package il.org.spartan.spartanizer.java.namespace;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.utils.*;

/** Interface to environment. Holds all the names defined till current PC. In
 * other words the 'names Environment' at every point of the program tree.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Dec 25, 2016 */
public interface Environment {
  /** @return true iff this instance doesn't have an entry with a given name. */
  default boolean doesntHave(final String name) {
    return !has(name);
  }

  /** Return true iff this instance is empty. */
  boolean empty();

  @NotNull List<Entry<String, Binding>> entries();

  @NotNull default Collection<Entry<String, Binding>> fullEntries() {
    @NotNull final Collection<Entry<String, Binding>> $ = new ArrayList<>(entries());
    if (nest() != null)
      $.addAll(nest().fullEntries());
    return $;
  }

  /** Get full path of the current this instance (all scope hierarchy). Used for
   * full names of the variables. */
  @NotNull default String fullName() {
    @Nullable final String $ = nest() == null || nest() == NULL ? null : nest().fullName();
    return ($ == null ? "" : $ + ".") + name().replaceAll("  .*$", "");
  }

  /** @return all the full names of the this instance. */
  @NotNull default Collection<String> fullNames() {
    @NotNull final Collection<String> $ = new LinkedHashSet<>(keys());
    if (nest() != null)
      $.addAll(nest().fullNames());
    return $;
  }

  default int fullSize() {
    return size() + (nest() == null ? 0 : nest().fullSize());
  }

  /** @return null iff the name is not in use in the this instance */
  @Nullable Binding get(String name);

  /** Answer the question whether the name is in use in the current this
   * instance */
  boolean has(String name);

  /** @return null iff the name is not hiding anything from outer scopes,
   *         otherwise Information about hided instance (with same name) */
  @Nullable default Binding hiding(final String name) {
    return nest() == null ? null : nest().get(name);
  }

  /** @return The names used in the current scope. */
  @NotNull Set<String> keys();

  String name();

  /** @return null at the most outer block. This method is similar to the
   *         'next()' method in a linked list. */
  @Nullable Environment nest();

  /** Should return the hidden entry, or null if no entry hidden by this one.
   * Note: you will have to assume multiple definitions in the same block, this
   * is a compilation error, but nevertheless, let a later entry with of a
   * certain name to "hide" a former entry with the same name. */
  @Nullable default Binding put(final String name, final Binding b) {
    throw new IllegalArgumentException(name + "/" + b);
  }

  int size();

  /** Used when new block (scope) is opened. */
  Namespace spawn();

  /** The Environment structure is in some like a Linked list, where EMPTY is
   * like the NULL at the end. */
  @Nullable Environment NULL = new Environment() {
    @Override public boolean empty() {
      return true;
    }

    @Override public List<Entry<String, Binding>> entries() {
      return Collections.emptyList();
    }

    @Nullable @Override public Binding get(@SuppressWarnings("unused") final String name) {
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

    @NotNull @Override public Namespace spawn() {
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
  @NotNull static Collection<Entry<String, Binding>> declarationsOf(@NotNull final Statement ¢) {
    @NotNull final Collection<Entry<String, Binding>> $ = new ArrayList<>();
    if (¢.getNodeType() != VARIABLE_DECLARATION_STATEMENT)
      return $;
    $.addAll(declarationsOf(az.variableDeclrationStatement(¢)));
    return $;
  }

  @NotNull static Collection<Entry<String, Binding>> declarationsOf(final VariableDeclarationStatement s) {
    @NotNull final Collection<Entry<String, Binding>> $ = new ArrayList<>();
    @NotNull final type t = type.baptize(trivia.condense(type(s)));
    @NotNull final String path = fullName(s);
    $.addAll(fragments(s).stream().map(λ -> new MapEntry<>(path + "." + λ.getName(), makeBinding(λ, t))).collect(toList()));
    return $;
  }

  /** @return set of entries declared in the node, including all hiding. */
  @NotNull static Set<Entry<String, Binding>> declaresDown(@NotNull final ASTNode ¢) {
    // Holds the declarations in the subtree and relevant siblings.
    @NotNull final LinkedHashSet<Entry<String, Binding>> $ = new LinkedHashSet<>();
    ¢.accept(new EnvironmentVisitor($));
    return $;
  }

  /** Gets declarations made in ASTNode's Ancestors */
  @NotNull static LinkedHashSet<Entry<String, Binding>> declaresUp(@NotNull final ASTNode n) {
    for (@Nullable Block PB = getParentBlock(n); PB != null; PB = getParentBlock(PB))
      statements(PB).forEach(λ -> upEnv.addAll(declarationsOf(λ)));
    return upEnv;
  }

  @NotNull static String fullName(@Nullable final ASTNode ¢) {
    return ¢ == null ? "" : fullName(¢.getParent()) + name(¢);
  }

  /** Spawns the first nested this instance. Should be used when the first block
   * is opened. */
  @NotNull static Environment genesis() {
    return NULL.spawn();
  }

  static Binding get(@NotNull final LinkedHashSet<Entry<String, Binding>> ss, @NotNull final String s) {
    return ss.stream().filter(λ -> s.equals(λ.getKey())).map(Entry::getValue).findFirst().orElse(null);
  }

  static Binding getHidden(@NotNull final String s) {
    for (@NotNull String ¢ = parentNameScope(s); ¢ != null && !¢.isEmpty(); ¢ = parentNameScope(¢)) {
      final Binding $ = get(upEnv, ¢ + "." + s.substring(s.lastIndexOf(".") + 1));
      if ($ != null)
        return $;
    }
    return null;
  }

  @Nullable static Block getParentBlock(@NotNull final ASTNode ¢) {
    return az.block(¢.getParent());
  }

  @Nullable static Binding makeBinding(@NotNull final VariableDeclarationFragment ¢, final type t) {
    return new Binding(¢.getParent(), getHidden(fullName(¢.getName())), ¢, t);
  }

  static String name(@SuppressWarnings("unused") final ASTNode __) {
    return "???";
  }

  @NotNull static String name(@NotNull final VariableDeclarationFragment ¢) {
    return ¢.getName() + "";
  }

  static Namespace of(@NotNull final ASTNode n) {
    for (final ASTNode ¢ : ancestors.of(n)) {
      @NotNull final Namespace $ = property.obtain(Namespace.class).from(¢);
      if ($ != null)
        return $;
    }
    Environment.NULL.spawn().fillScope(n.getRoot());
    for (final ASTNode ¢ : ancestors.of(n)) {
      @NotNull final Namespace $ = property.obtain(Namespace.class).from(¢);
      if ($ != null)
        return $;
    }
    return null;
  }

  @NotNull static String parentNameScope(@Nullable final String ¢) {
    return ¢ == null || ¢.isEmpty() ? "" : ¢.substring(0, ¢.lastIndexOf("."));
  }

  /** @return set of entries used in a given node. this includes the list of
   *         entries that were defined in the node */
  @NotNull static Set<Entry<String, Binding>> uses(@SuppressWarnings("unused") final ASTNode __) {
    return new LinkedHashSet<>();
  }
}
