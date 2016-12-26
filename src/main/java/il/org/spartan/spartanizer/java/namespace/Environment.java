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
  /** Dictionary with a parent. Insertions go the current node, searches start
   * at the current note and Delegate to the parent unless it is null. */
  final class Nested implements Environment {
    public final Map<String, Symbol> flat = new LinkedHashMap<>();
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
    @Override public LinkedHashSet<Map.Entry<String, Symbol>> entries() {
      return new LinkedHashSet<>(flat.entrySet());
    }

    /** @return The information about the name in current {@link Environment}
     *         . */
    @Override public Symbol get(final String name) {
      final Symbol $ = flat.get(name);
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
    @Override public Symbol put(final String name, final Symbol value) {
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
  LinkedHashSet<Entry<String, Symbol>> emptyEntries = new LinkedHashSet<>();
  /** Initializer for EMPTY */
  LinkedHashSet<String> emptySet = new LinkedHashSet<>();
  // Holds the declarations in the subtree and relevant siblings.
  LinkedHashSet<Entry<String, Symbol>> upEnv = new LinkedHashSet<>();

  static Symbol createInformation(final VariableDeclarationFragment ¢, final type t) {
    return new Symbol(¢.getParent(), getHidden(fullName(¢.getName())), ¢, t);
  }

  /** @param ¢ JD
   * @return All declarations in given {@link Statement}, without entering the
   *         contained ({@link Block}s. If the {@link Statement} is a
   *         {@link Block}, (also IfStatement, ForStatement and so on...) return
   *         empty Collection. */
  static List<Entry<String, Symbol>> declarationsOf(final Statement ¢) {
    final List<Entry<String, Symbol>> $ = new ArrayList<>();
    switch (¢.getNodeType()) {
      case VARIABLE_DECLARATION_STATEMENT:
        $.addAll(declarationsOf(az.variableDeclrationStatement(¢)));
        break;
      default:
        return $;
    }
    return $;
  }

  static List<Entry<String, Symbol>> declarationsOf(final VariableDeclarationStatement s) {
    final List<Entry<String, Symbol>> $ = new ArrayList<>();
    final type t = type.baptize(wizard.condense(s.getType()));
    final String path = fullName(s);
    $.addAll(fragments(s).stream().map(¢ -> new MapEntry<>(path + "." + ¢.getName(), createInformation(¢, t))).collect(Collectors.toList()));
    return $;
  }

  /** @return set of entries declared in the node, including all hiding. */
  static LinkedHashSet<Entry<String, Symbol>> declaresDown(final ASTNode ¢) {
    // Holds the declarations in the subtree and relevant siblings.
    final LinkedHashSet<Entry<String, Symbol>> $ = new LinkedHashSet<>();
    ¢.accept(new EnvironmentVisitor($));
    return $;
  }

  /** Gets declarations made in ASTNode's Ancestors */
  static LinkedHashSet<Entry<String, Symbol>> declaresUp(final ASTNode n) {
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

  static Symbol get(final LinkedHashSet<Entry<String, Symbol>> ss, final String s) {
    for (final Entry<String, Symbol> $ : ss)
      if (s.equals($.getKey()))
        return $.getValue();
    return null;
  }

  static Symbol getHidden(final String s) {
    final String shortName = s.substring(s.lastIndexOf(".") + 1);
    for (String ¢ = parentNameScope(s); !"".equals(¢); ¢ = parentNameScope(¢)) {
      final Symbol $ = get(upEnv, ¢ + "." + shortName);
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
  static LinkedHashSet<Entry<String, Symbol>> uses(@SuppressWarnings("unused") final ASTNode __) {
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

  default LinkedHashSet<Entry<String, Symbol>> entries() {
    return emptyEntries;
  }

  default LinkedHashSet<Entry<String, Symbol>> fullEntries() {
    final LinkedHashSet<Entry<String, Symbol>> $ = new LinkedHashSet<>(entries());
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
  default Symbol get(@SuppressWarnings("unused") final String name) {
    return null;
  }

  /** Answer the question whether the name is in use in the current
   * {@link Environment} */
  default boolean has(@SuppressWarnings("unused") final String name) {
    return false;
  }

  /** @return null iff the name is not hiding anything from outer scopes,
   *         otherwise Information about hided instance (with same name) */
  default Symbol hiding(final String name) {
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
  default Symbol put(final String name, final Symbol i) {
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