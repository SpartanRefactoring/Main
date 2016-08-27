package il.org.spartan.refactoring.utils;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** Mumbo jumbo of stuff we will do later.Document it, but do not maintaing it
 * for now */
class Entry {
  /** The containing block, whose death marks the death of this entry; not sure,
   * but I think this entry can be shared by many nodes at the same leve */
  public final ASTNode blockScope;
  /** The node at which this entry was created */
  public final ASTNode self;
  /** What do we know about the type of this definition */
  public final Type type;
  /** What do we know about an entry hidden by this one */
  public final Entry hiding;

  // For now, nothing is known, we only maintain lists
  Entry() {
    blockScope = self = null;
    type = null;
    hiding = null;
  }
}

public interface Environment {
  boolean isEmpty();

  boolean containsKey(String key);

  Entry get(String e);

  Entry put(String key, Entry e);

  Set<String> keySet();

  default Environment newChild() {
    return new Child(this);
  }

  /** Document properly, this is the parent of all all */
  static final Environment EMPTY = new Environment() {
    @Override public boolean isEmpty() {
      return true;
    }

    @Override public boolean containsKey(String __) {
      return false;
    }

    @Override public Entry get(String e) {
      return null;
    }

    @Override public Entry put(String key, Entry e) {
      throw new IllegalArgumentException(key + "/" + e);
    }

    @Override public Set<String> keySet() {
      return null;
    }
  };

  public static Environment newRoot() {
    return EMPTY.newChild();
  }

  /** TODO: document propertly, but essentially is a dictionary with a parent.
   * Insertions go the current node, searches start at the current note and
   * deleegate to the parent unless it is null. */
  class Child implements Environment {
    public final Environment parent;
    public final Map<String, Entry> inner = new LinkedHashMap<>();

    @Override public boolean isEmpty() {
      return inner.isEmpty() || parent.isEmpty();
    }

    @Override public boolean containsKey(String key) {
      return inner.containsKey(key) || parent.containsKey(key);
    }

    @Override public Entry get(String key) {
      return inner.get(key);
    }

    @Override public Entry put(String key, Entry value) {
      return inner.put(key, value);
    }

    @Override public Set<String> keySet() {
      return inner.keySet();
    }

    Child(Environment parent) {
      this.parent = parent;
    }
  }
}