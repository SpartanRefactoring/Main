package il.org.spatan.iteration;

import static fluent.ly.___.*;
import static fluent.ly.box.*;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.*;

/** Provides an encoding of an object (drawn from a given, predetermined, set of
 * objects) as an <code><b>int</b></code> value, and a representation of a set
 * of objects (which must be a subset of this given set) as a {@link BitSet}.
 * @author Yossi Gil
 * @param <T> type of encoded elements */
public class IntCodex<T> extends Codex.Anchored<T> implements Serializable {
  private static final long serialVersionUID = -0x54137EB6CCF2FDE7L;
  @NotNull private final Vector<T> int2objects;
  @NotNull private final Map<T, Integer> objects2ints;

  /** Constructs a translator for the specified set.
   * @param ts the set of objects/attributes that shall be translated. */
  public IntCodex(@NotNull final Iterable<T> ts) {
    nonnull(ts);
    int2objects = new Vector<>();
    objects2ints = new HashMap<>();
    int position = 0;
    for (final T ¢ : ts)
      if (!objects2ints.containsKey(¢)) {
        objects2ints.put(¢, box(position++));
        int2objects.add(¢);
      }
  }
  @Override public boolean contains(final T ¢) {
    return objects2ints.containsKey(¢);
  }
  /** Returns the object represented by <code>i</code> as determined by this
   * translator.
   * @param ¢ the <code><b>int</b></code> whose translation shall be returned.
   * @return the translation of <code>i</code>. */
  @Override public T decode(final int ¢) {
    return int2objects.get(¢);
  }
  @Override @NotNull public Iterable<T> elements() {
    return int2objects;
  }
  /** Returns the <code><b>int</b></code> value of <code>c</code> as determined
   * by this translator.
   * @param ¢ the value whose <code><b>int</b></code> translation shall be
   *        returned.
   * @return the <code><b>int</b></code> translation of <code>c</code>. */
  @Override public int encode(final T ¢) {
    require(objects2ints.containsKey(¢));
    return objects2ints.get(¢).intValue();
  }
  @Override public int size() {
    return objects2ints.size();
  }
}
