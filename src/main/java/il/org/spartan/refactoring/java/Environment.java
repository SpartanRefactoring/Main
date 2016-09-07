package il.org.spartan.refactoring.java;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

/** Interface to Environment. Holds all the names defined till current PC. In
 * other words the 'names Environment' at every point of the program flow. */
@SuppressWarnings({ "unused" }) public interface Environment {
  /** Mumbo jumbo of stuff we will do later. Document it, but do not maintaing
   * it for now, this class is intentionally package level, and intenrationally
   * defined locall. For now, cients should not be messing with it */
  static class Information {
    /** The containing block, whose death marks the death of this entry; not
     * sure, but I think this entry can be shared by many nodes at the same
     * leve */
    public final ASTNode blockScope;
    /** What do we know about an entry hidden by this one */
    public final Information hiding;
    /** The node at which this entry was created */
    public final ASTNode self;
    /** What do we know about the type of this definition */
    public final PrudentType prudentType;

    // For now, nothing is known, we only maintain lists
    public Information() {
      blockScope = self = null;
      prudentType = null;
      hiding = null;
    }

    public Information(final PrudentType t) {
      blockScope = self = null;
      prudentType = t;
      hiding = null;
    }

    @Override public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final Information other = (Information) obj;
      if (blockScope == null) {
        if (other.blockScope != null)
          return false;
      } else if (!blockScope.equals(other.blockScope))
        return false;
      if (hiding == null) {
        if (other.hiding != null)
          return false;
      } else if (!hiding.equals(other.hiding))
        return false;
      /* if (prudentType != other.prudentType) //TODO how to correctly compare
       * prudentType - best case scenario - contains(). return false; */
      if (self == null) {
        if (other.self != null)
          return false;
      } else if (!self.equals(other.self))
        return false;
      return true;
    }

    @Override public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (blockScope == null ? 0 : blockScope.hashCode());
      result = prime * result + (hiding == null ? 0 : hiding.hashCode());
      // result = prime * result + ((prudentType == null) ? 0 :
      // prudentType.hashCode());
      result = prime * result + (self == null ? 0 : self.hashCode());
      return result;
    }
  }

  /** TODO: document properly, but essentially is a dictionary with a parent.
   * Insertions go the current node, searches start at the current note and
   * Delegate to the parent unless it is null. */
  /* Nested environment which has it's own Map of names 'flat', and an instance
   * to the parent scope 'nest'. */
  final class Nested implements Environment {
    public final Map<String, Information> flat = new LinkedHashMap<>();
    public final Environment nest;

    Nested(final Environment parent) {
      nest = parent;
    }

    /* @return true iff Env is empty. */
    @Override public boolean empty() {
      return flat.isEmpty() && nest.empty();
    }

    /* @return Map entries used in the current scope. */
    @Override public LinkedHashSet<Map.Entry<String, Information>> entries() {
      return new LinkedHashSet<>(flat.entrySet());
    }

    /* @return The information about the name in current Env. */
    @Override public Information get(final String name) {
      final Information $ = flat.get(name);
      return $ != null ? $ : nest.get(name);
    }

    /* Check whether the Env already has the name. */
    @Override public boolean has(final String name) {
      return flat.containsKey(name) || nest.has(name);
    }

    /* @return Names used in current scope. */
    @Override public LinkedHashSet<String> names() {
      return new LinkedHashSet<>(flat.keySet());
    }

    /* One step up in the Env tree. Funny but it even sounds like next(). */
    @Override public Environment nest() {
      return nest;
    }

    /** Add name to the current scope in the Env. */
    @Override public Information put(final String name, final Information value) {
      flat.put(name, value);
      assert !flat.isEmpty();
      return hiding(name);
    }
  }

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

  /** Spawns the first Nested Env. Should be used when the first block is
   * opened. */
  static Environment genesis() {
    return EMPTY.spawn();
  }

  /** @return set of entries used in a given node. this includes the list of
   *         entries that were defined in the node */
  static LinkedHashSet<Entry<String, Information>> uses(final ASTNode n) {
    return new LinkedHashSet<>();
  }

  /** Return true iff Env doesn't have the name. */
  default boolean doesntHave(final String name) {
    return !has(name);
  }

  /** Return true iff Env is empty. */
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

  /** Get full path of the current Env (all scope hierarchy). Used for full
   * names of the variables. */
  default String fullName() {
    final String $ = nest() == null || nest() == EMPTY ? null : nest().fullName();
    return ($ == null ? "" : $ + ".") + name();
  }

  /** @return all the full names of the Env. */
  default LinkedHashSet<String> fullNames() {
    final LinkedHashSet<String> $ = new LinkedHashSet<>(names());
    if (nest() != null)
      $.addAll(nest().fullNames());
    return $;
  }

  default int fullSize() {
    return size() + (nest() == null ? 0 : nest().fullSize());
  }

  /** @return null iff the name is not in use in the Env. */
  default Information get(final String name) {
    return null;
  }

  /** Answer the question whether the name is in use in the current Env */
  default boolean has(final String name) {
    return false;
  }

  /** @return null iff the name is not hiding anything from outer scopes. */
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

  /* Used when new block (scope) is opened. */
  default Environment spawn() {
    return new Nested(this);
  }
}