package il.org.spatan.iteration;

import static il.org.spartan.utils.___.*;

import java.util.*;
import java.util.BitSet;

import il.org.spartan.collections.*;

/** An abstract encoding of an object (drawn from a given, predetermined, set of
 * objects) as an <code><b>int</b></code> value, and a representation of a set
 * of objects (which must be a subset of this given set) as a {@link BitSet}.
 * @author Yossi Gil
 * @param <T> Type of encoded objects
 * @param <U> An upper bound on the type of encoded objects */
public abstract class Codex<U, T extends U> implements Container<U, T> {
  public final Set<T> decode(final BitSet b) {
    final Set<T> $ = new HashSet<>();
    for (int i = b.nextSetBit(0); i >= 0; i = b.nextSetBit(i + 1))
      $.add(decode(i));
    return $;
  }

  /** Returns the object represented by <code>i</code> as determined by this
   * translator.
   * @param i the <code><b>int</b></code> whose translation shall be returned.
   * @return the translation of <code>i</code>. */
  public abstract T decode(final int i);

  public final BitSet encode(final Iterable<? extends U> ts) {
    nonnull(ts);
    final BitSet $ = new BitSet(size());
    for (final U t : ts)
      $.set(encode(t));
    return $;
  }

  /** Returns the <code><b>int</b></code> value of <code>c</code> as determined
   * by this translator.
   * @param t the whose <code><b>int</b></code> translation shall be returned.
   * @return the <code><b>int</b></code> translation of <code>c</code>. */
  abstract public int encode(final U t);

  public static abstract class Anchored<T> extends Codex<T, T> {
    //
  }
}
