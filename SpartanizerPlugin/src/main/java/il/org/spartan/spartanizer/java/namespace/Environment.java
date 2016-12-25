package il.org.spartan.spartanizer.java.namespace;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;
import java.util.Map.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Interface to environment. Holds all the names defined till current PC. In
 * other words the 'names Environment' at every point of the program flow. */
public interface Environment {
  /** Information about a variable in the environment - its {@link ASTNode}, its
   * parent's, its {@link type}, and which other variables does it hide. This
   * class is intentionally package level, and intentionally defined local. For
   * now, clients should not be messing with it
   * @since 2016 */
  class Information {
    public static boolean eq(final Object o1, final Object o2) {
      return o1 == o2 || o1 == null && o2 == null || o2.equals(o1);
    }

    /** For Information purposes, {@link type}s are equal if their key is
     * equal. */
    static boolean eq(final type t1, final type t2) {
      return t1 == null ? t2 == null : t2 != null && t1.key().equals(t2.key());
    }

    /** The containing block, whose death marks the death of this entry; not
     * sure, but I think this entry can be shared by many nodes at the same
     * level */
    public final ASTNode blockScope;
    /** What do we know about an entry hidden by this one */
    public final Information hiding;
    /** The node at which this entry was created */
    public final ASTNode self;
    /** What do we know about the type of this definition */
    public final type prudentType;

    // For now, nothing is known, we only maintain lists
    public Information() {
      blockScope = self = null;
      prudentType = null;
      hiding = null;
    }

    public Information(final ASTNode blockScope, final Information hiding, final ASTNode self, final type prudentType) {
      this.blockScope = blockScope;
      this.hiding = hiding;
      this.self = self;
      this.prudentType = prudentType;
    }

    public Information(final type t) {
      blockScope = self = null;
      prudentType = t;
      hiding = null;
    }

    public boolean equals(final Information ¢) {
      return eq(blockScope, ¢.blockScope) && eq(hiding, ¢.hiding) && eq(prudentType, ¢.prudentType) && eq(self, ¢.self);
    }

    /** @param ¢
     * @return <code><b>true</b></code> <em>iff</em> the ASTNode (self) and its
     *         parent (blockScope) are the same ones, the type's key() is the
     *         same, and if the Information nodes hidden are equal. */
    // Required for MapEntry equality, which is, in turn, required for Set
    // containment check, which is required for testing.
    @Override public boolean equals(final Object ¢) {
      return ¢ == this || ¢ != null && getClass() == ¢.getClass() && equals((Information) ¢);
    }

    // Required for MapEntry equality, which is, in turn, required for Set
    // containment check, which is required for testing.
    @Override public int hashCode() {
      return (self == null ? 0 : self.hashCode())
          + 31 * ((hiding == null ? 0 : hiding.hashCode()) + 31 * ((blockScope == null ? 0 : blockScope.hashCode()) + 31));
    }
  }

  /** Dictionary with a parent. Insertions go the current node, searches start
   * at the current note and Delegate to the parent unless it is null. */
  final class Nested implements Environment {
    public final Map<String, Information> flat = new LinkedHashMap<>();
    public final Environment nest;

    Nested(final Environment parent) {
      nest = parent;
    }

    /** @return <code><b>true</b></code> <em>iff</em> {@link Environment} is
     *         empty. */
    @Override public boolean empty() {
      return flat.isEmpty() && nest.empty();
    }

    /** @return Map entries used in the current scope. */
    @Override public LinkedHashSet<Map.Entry<String, Information>> entries() {
      return new LinkedHashSet<>(flat.entrySet());
    }

    /** @return The information about the name in current {@link Environment}
     *         . */
    @Override public Information get(final String name) {
      final Information $ = flat.get(name);
      return $ != null ? $ : nest.get(name);
    }

    /** Check whether the {@link Environment} already has the name. */
    @Override public boolean has(final String name) {
      return flat.containsKey(name) || nest.has(name);
    }

    /** @return names used the {@link Environment} . */
    @Override public LinkedHashSet<String> names() {
      return new LinkedHashSet<>(flat.keySet());
    }

    /** One step up in the {@link Environment} tree. Funny but it even sounds
     * like next(). */
    @Override public Environment nest() {
      return nest;
    }

    /** Add name to the current scope in the {@link Environment} . */
    @Override public Information put(final String name, final Information value) {
      flat.put(name, value);
      assert !flat.isEmpty();
      return hiding(name);
    }
  }

  /** The Environment structure is in some like a Linked list, where EMPTY is
   * like the NULL at the end. */
  Environment EMPTY = new Environment() {
    // This class is intentionally empty
  };
  /** Initializer for EMPTY */
  LinkedHashSet<Entry<String, Information>> emptyEntries = new LinkedHashSet<>();
  /** Initializer for EMPTY */
  LinkedHashSet<String> emptySet = new LinkedHashSet<>();
  // Holds the declarations in the subtree and relevant siblings.
  LinkedHashSet<Entry<String, Information>> upEnv = new LinkedHashSet<>();

  static Information createInformation(final VariableDeclarationFragment ¢, final type t) {
    return new Information(¢.getParent(), getHidden(fullName(¢.getName())), ¢, t);
  }

  /** @param ¢ JD
   * @return All declarations in given {@link Statement}, without entering the
   *         contained ({@link Block}s. If the {@link Statement} is a
   *         {@link Block}, (also IfStatement, ForStatement and so on...) return
   *         empty Collection. */
  static List<Entry<String, Information>> declarationsOf(final Statement ¢) {
    final List<Entry<String, Information>> $ = new ArrayList<>();
    switch (¢.getNodeType()) {
      case VARIABLE_DECLARATION_STATEMENT:
        $.addAll(declarationsOf(az.variableDeclrationStatement(¢)));
        break;
      default:
        return $;
    }
    return $;
  }

  static List<Entry<String, Information>> declarationsOf(final VariableDeclarationStatement s) {
    final List<Entry<String, Information>> $ = new ArrayList<>();
    final type t = type.baptize(wizard.condense(s.getType()));
    final String path = fullName(s);
    $.addAll(fragments(s).stream().map(¢ -> new MapEntry<>(path + "." + ¢.getName(), createInformation(¢, t))).collect(Collectors.toList()));
    return $;
  }

  /** @return set of entries declared in the node, including all hiding. */
  static LinkedHashSet<Entry<String, Information>> declaresDown(final ASTNode ¢) {
    // Holds the declarations in the subtree and relevant siblings.
    final LinkedHashSet<Entry<String, Information>> $ = new LinkedHashSet<>();
    ¢.accept(new EnvironmentVisitor($));
    return $;
  }

  /** Gets declarations made in ASTNode's Ancestors */
  static LinkedHashSet<Entry<String, Information>> declaresUp(final ASTNode n) {
    for (Block PB = getParentBlock(n); PB != null; PB = getParentBlock(PB))
      for (final Statement ¢ : statements(PB))
        upEnv.addAll(declarationsOf(¢));
    return upEnv;
  }

  static String fullName(final ASTNode ¢) {
    return ¢ == null ? "" : fullName(¢.getParent()) + name(¢);
  }

  /** Spawns the first nested {@link Environment}. Should be used when the first
   * block is opened. */
  static Environment genesis() {
    return EMPTY.spawn();
  }

  static Information get(final LinkedHashSet<Entry<String, Information>> ss, final String s) {
    for (final Entry<String, Information> $ : ss)
      if (s.equals($.getKey()))
        return $.getValue();
    return null;
  }

  static Information getHidden(final String s) {
    final String shortName = s.substring(s.lastIndexOf(".") + 1);
    for (String ¢ = parentNameScope(s); !"".equals(¢); ¢ = parentNameScope(¢)) {
      final Information $ = get(upEnv, ¢ + "." + shortName);
      if ($ != null)
        return $;
    }
    return null;
  }

  static Block getParentBlock(final ASTNode ¢) {
    return az.block(¢.getParent());
  }

  static String name(@SuppressWarnings("unused") final ASTNode __) {
    return "???";
  }

  static String name(final VariableDeclarationFragment ¢) {
    return ¢.getName() + "";
  }

  static String parentNameScope(final String ¢) {
    assert "".equals(¢) || ¢.lastIndexOf(".") != -1 : "nameScope malfunction!";
    return "".equals(¢) ? "" : ¢.substring(0, ¢.lastIndexOf("."));
  }

  /** @return set of entries used in a given node. this includes the list of
   *         entries that were defined in the node */
  static LinkedHashSet<Entry<String, Information>> uses(@SuppressWarnings("unused") final ASTNode __) {
    return new LinkedHashSet<>();
  }

  /** @return true iff {@link Environment} doesn't have an entry with a given
   *         name. */
  default boolean doesntHave(final String name) {
    return !has(name);
  }

  /** Return true iff {@link Environment} is empty. */
  default boolean empty() {
    return true;
  }

  default LinkedHashSet<Entry<String, Information>> entries() {
    return emptyEntries;
  }

  default LinkedHashSet<Entry<String, Information>> fullEntries() {
    final LinkedHashSet<Entry<String, Information>> $ = new LinkedHashSet<>(entries());
    if (nest() != null)
      $.addAll(nest().fullEntries());
    return $;
  }

  /** Get full path of the current {@link Environment} (all scope hierarchy).
   * Used for full names of the variables. */
  default String fullName() {
    final String $ = nest() == null || nest() == EMPTY ? null : nest().fullName();
    return ($ == null ? "" : $ + ".") + name();
  }

  /** @return all the full names of the {@link Environment}. */
  default LinkedHashSet<String> fullNames() {
    final LinkedHashSet<String> $ = new LinkedHashSet<>(names());
    if (nest() != null)
      $.addAll(nest().fullNames());
    return $;
  }

  default int fullSize() {
    return size() + (nest() == null ? 0 : nest().fullSize());
  }

  /** @return null iff the name is not in use in the {@link Environment} */
  default Information get(@SuppressWarnings("unused") final String name) {
    return null;
  }

  /** Answer the question whether the name is in use in the current
   * {@link Environment} */
  default boolean has(@SuppressWarnings("unused") final String name) {
    return false;
  }

  /** @return null iff the name is not hiding anything from outer scopes,
   *         otherwise Information about hided instance (with same name) */
  default Information hiding(final String name) {
    return nest() == null ? null : nest().get(name);
  }

  default String name() {
    return "";
  }

  /** @return The names used in the current scope. */
  default Set<String> names() {
    return emptySet;
  }

  /** @return null at the most outer block. This method is similar to the
   *         'next()' method in a linked list. */
  default Environment nest() {
    return null;
  }

  /** Should return the hidden entry, or null if no entry hidden by this one.
   * Note: you will have to assume multiple definitions in the same block, this
   * is a compilation error, but nevertheless, let a later entry with of a
   * certain name to "hide" a former entry with the same name. */
  default Information put(final String name, final Information i) {
    throw new IllegalArgumentException(name + "/" + i);
  }

  default int size() {
    return 0;
  }

  /** Used when new block (scope) is opened. */
  default Environment spawn() {
    return new Nested(this);
  }
}