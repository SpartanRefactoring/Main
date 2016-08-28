package il.org.spartan.refactoring.utils;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;

/** TODO: Document what is it for users? how should clients use this
 * class/method, same for all TODO: Document */
public interface Environment {
  /** Document properly, this is the parent of all all */
  static final Environment EMPTY = new Environment() {
    /* Empty */
  };
  /** TODO: Document */
  static final Set<Entry<String, Information>> emptyEntries = Collections.unmodifiableSet(new HashSet<>());
  /** TODO: Document */
  static final Set<String> emptySet = Collections.unmodifiableSet(new HashSet<>());

  /** TODO: Document */
  public static Environment genesis() {
    return EMPTY.spawn();
  }

  default int size() {
    return 0;
  }

  default Set<Entry<String, Information>> entries() {
    return emptyEntries;
  }

  default int fullSize() {
    return size() + (nest() == null ? 0 : nest().fullSize());
  }

  /** TODO: Document */
  default boolean doesntHave(final String name) {
    return !has(name);
  }

  /** TODO: Document */
  default boolean empty() {
    return true;
  }

  default Set<Entry<String, Information>> fullEntries() {
    final Set<Entry<String, Information>> $ = new HashSet<>(entries());
    if (nest() != null)
      $.addAll(nest().fullEntries());
    return $;
  }

  /** May return null! Document */
  default Information get(final String name) {
    return null;
  }

  /** TODO: Document */
  default boolean has(final String name) {
    return false;
  }

  /** May return null! Document when and document it from the client side */
  default Information hiding(final String name) {
    return nest().get(name);
  }

  /** upper level only. If you want the parent, go to the parent TODO:
   * Document */
  default Set<String> names() {
    return emptySet;
  }

  /** TODO: Document */
  default String fullName() {
    final String $ = nest() == null ? null : nest().fullName();
    return ($ == null ? "" : $ + ".") + name();
  }

  default String name() {
    return "";
  }

  /** TODO: Document */
  default Set<String> fullNames() {
    final Set<String> $ = new HashSet<>(names());
    if (nest() != null)
      $.addAll(nest().fullNames());
    return $;
  }

  /** May return null! Document why */
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

  default Environment spawn() {
    return new Nested(this);
  }

  /** TODO: document propertly, but essentially is a dictionary with a parent.
   * Insertions go the current node, searches start at the current note and
   * deleegate to the parent unless it is null. */
  final class Nested implements Environment {
    public final Map<String, Information> flat = new LinkedHashMap<>();
    public final Environment nest;

    Nested(final Environment parent) {
      nest = parent;
    }

    @Override public boolean empty() {
      return flat.isEmpty() || nest.empty();
    }

    @Override public Set<Map.Entry<String, Information>> entries() {
      return flat.entrySet();
    }

    @Override public Information get(final String name) {
      final Information $ = flat.get(name);
      return $ != null ? $ : nest.get(name);
    }

    @Override public boolean has(final String name) {
      return flat.containsKey(name) || nest.has(name);
    }

    @Override public Set<String> names() {
      return flat.keySet();
    }

    @Override public Environment nest() {
      return nest;
    }

    @Override public Information put(final String name, final Information value) {
      flat.put(name, value);
      return hiding(name);
    }
  }
}

/** Mumbo jumbo of stuff we will do later. Document it, but do not maintaing it
 * for now, this class is intentionally package level, and intenrationally
 * defined locall. For now, cients should not be messing with it */
class Information {
  /** The containing block, whose death marks the death of this entry; not sure,
   * but I think this entry can be shared by many nodes at the same leve */
  public final ASTNode blockScope;
  /** What do we know about an entry hidden by this one */
  public final Information hiding;
  /** The node at which this entry was created */
  public final ASTNode self;
  /** What do we know about the type of this definition */
  public final Type type;

  // For now, nothing is known, we only maintain lists
  Information() {
    blockScope = self = null;
    type = null;
    hiding = null;
  }
}
