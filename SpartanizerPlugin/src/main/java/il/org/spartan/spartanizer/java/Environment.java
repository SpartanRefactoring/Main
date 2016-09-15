package il.org.spartan.spartanizer.java;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;

/** Interface to Environment. Holds all the names defined till current PC. In
 * other words the 'names Environment' at every point of the program flow. */
/* TODO Wrings to improve once Environment is complete:
 * AssignmentToPostfixIncrement (Issue 107). Identifier renaming (Issue 121) */
@SuppressWarnings({ "unused" }) public interface Environment {
  /** The Environment structure is in some like a Linked list, where EMPTY is
   * like the NULL at the end. */
  static final Environment EMPTY = new Environment() {
    /* Empty */
  };
  /** Initializer for EMPTY */
  static final LinkedHashSet<Entry<String, Information>> emptyEntries = new LinkedHashSet<>();
  /** Initializer for EMPTY */
  static final LinkedHashSet<String> emptySet = new LinkedHashSet<>();

  /** @return set of entries defined in the node, including all hiding. */
  static LinkedHashSet<Entry<String, Information>> declares(final ASTNode n) {
    return new LinkedHashSet<>();
  }

  /** Spawns the first nested {@link Environment}. Should be used when the first
   * block is opened. */
  static Environment genesis() {
    return EMPTY.spawn();
  }

  /** @return set of entries used in a given node. this includes the list of
   *         entries that were defined in the node */
  static LinkedHashSet<Entry<String, Information>> uses(final ASTNode n) {
    return new LinkedHashSet<>();
  }

  /** Return true iff {@link Environment} doesn't have an entry with a given
   * name. */
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
  default Information get(final String name) {
    return null;
  }

  /** Answer the question whether the name is in use in the current
   * {@link Environment} */
  default boolean has(final String name) {
    return false;
  }

  /** @return null iff the name is not hiding anything from outer scopes,
   *         otherwise ?? TODO */
  default Information hiding(final String name) {
    return nest().get(name);
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

  /** Mumbo jumbo of stuff we will do later. Document it, but do not maintain it
   * for now, this class is intentionally package level, and intenationally
   * defined local. For now, clients should not be messing with it */
  static class Information {
    public static boolean eq(final Object o1, final Object o2) {
      return o1 == o2 || o1 == null && o2 == null || o2.equals(o1);
    }

    static boolean prudentTypeComparison(final type t1, final type t2) {
      return t1 == null ? t2 == null : t2 != null && (t1 == type.Odd.Types.NOTHING || t2 == type.Odd.Types.NOTHING || t1 == t2);
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

    public Information(final type t) {
      blockScope = self = null;
      prudentType = t;
      hiding = null;
    }

    public boolean equals(final Information ¢) {
      return eq(blockScope, ¢.blockScope) && eq(hiding, ¢.hiding) && eq(prudentType, ¢.prudentType) && eq(self, ¢.self);
    }

    @Override public boolean equals(final Object ¢) {
      return ¢ == this || ¢ != null && getClass() == ¢.getClass() && equals((Information) ¢);
    }

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

    /** @return true iff {@link Environment} is empty. */
    @Override public boolean empty() {
      return flat.isEmpty() && nest.empty();
    }

    /** @return Map entries used in the current scope. */
    @Override public LinkedHashSet<Map.Entry<String, Information>> entries() {
      return new LinkedHashSet<>(flat.entrySet());
    }

    /** @return The information about the name in current
     *         {@link Environment}. */
    @Override public Information get(final String name) {
      final Information $ = flat.get(name);
      return $ != null ? $ : nest.get(name);
    }

    /** Check whether the {@link Environment} already has the name. */
    @Override public boolean has(final String name) {
      return flat.containsKey(name) || nest.has(name);
    }

    /** @return Names used the {@link Environment}. */
    @Override public LinkedHashSet<String> names() {
      return new LinkedHashSet<>(flat.keySet());
    }

    /** One step up in the {@link Environment} tree. Funny but it even sounds
     * like next(). */
    @Override public Environment nest() {
      return nest;
    }

    /** Add name to the current scope in the {@link Environment}. */
    @Override public Information put(final String name, final Information value) {
      flat.put(name, value);
      assert !flat.isEmpty();
      return hiding(name);
    }
  }
}