package il.org.spatan.iteration;

import static il.org.spartan.utils.Box.*;
import static il.org.spartan.utils.____.*;

import java.io.*;
import java.util.*;

/** Provides an encoding of an object (drawn from a given, predetermined, set of
 * objects) as an <code><b>int</b></code> value, and a representation of a set
 * of objects (which must be a subset of this given set) as a {@link BitSet}.
 * @author Yossi Gil
 * @param <T> type of encoded elements */
public class IntCodex<T> extends Codex.Anchored<T> implements Serializable {
  private static final long serialVersionUID = -6058325247331073511L;
  private final Vector<T> int2objects;
  private final Map<T, Integer> objects2ints;

  /** Constructs a translator for the specified set.
   * @param ts the set of objects/attributes that shall be translated. */
  public IntCodex(final Iterable<T> ts) {
    nonnull(ts);
    int2objects = new Vector<>();
    objects2ints = new HashMap<>();
    int position = 0;
    for (final T t : ts)
      if (!objects2ints.containsKey(t)) {
        objects2ints.put(t, box(position++));
        int2objects.add(t);
      }
  }

  @Override public boolean contains(final T t) {
    return objects2ints.containsKey(t);
  }

  /** Returns the object represented by <code>i</code> as determined by this
   * translator.
   * @param i the <code><b>int</b></code> whose translation shall be returned.
   * @return the translation of <code>i</code>. */
  @Override public T decode(final int i) {
    return int2objects.get(i);
  }

  @Override public Iterable<T> elements() {
    return int2objects;
  }

  /** Returns the <code><b>int</b></code> value of <code>c</code> as determined
   * by this translator.
   * @param t the value whose <code><b>int</b></code> translation shall be
   *        returned.
   * @return the <code><b>int</b></code> translation of <code>c</code>. */
  @Override public int encode(final T t) {
    require(objects2ints.containsKey(t));
    return objects2ints.get(t).intValue();
  }

  @Override public int size() {
    return objects2ints.size();
  }
}
