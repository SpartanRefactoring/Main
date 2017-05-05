package il.org.spatan.iteration;

import static fluent.ly.___.*;

import java.util.*;
import java.util.BitSet;

import org.jetbrains.annotations.*;

import il.org.spartan.collections.*;

/** An abstract encoding of an object (drawn from a given, predetermined, set of
 * objects) as an <code><b>int</b></code> value, and a representation of a set
 * of objects (which must be a subset of this given set) as a {@link BitSet}.
 * @author Yossi Gil
 * @param <T> Type of encoded objects
 * @param <U> An upper bound on the type of encoded objects */
public abstract class Codex<U, T extends U> implements Container<U, T> {
  @NotNull public final Set<T> decode(@NotNull final BitSet s) {
    @NotNull final Set<T> $ = new HashSet<>();
    for (int ¢ = s.nextSetBit(0); ¢ >= 0; ¢ = s.nextSetBit(¢ + 1))
      $.add(decode(¢));
    return $;
  }
  /** Returns the object represented by <code>i</code> as determined by this
   * translator.
   * @param i the <code><b>int</b></code> whose translation shall be returned.
   * @return the translation of <code>i</code>. */
  public abstract T decode(int i);
  @NotNull public final BitSet encode(@NotNull final Iterable<? extends U> us) {
    nonnull(us);
    @NotNull final BitSet $ = new BitSet(size());
    for (final U t : us)
      $.set(encode(t));
    return $;
  }
  /** Returns the <code><b>int</b></code> value of <code>c</code> as determined
   * by this translator.
   * @param t the whose <code><b>int</b></code> translation shall be returned.
   * @return the <code><b>int</b></code> translation of <code>c</code>. */
  public abstract int encode(U t);

  public abstract static class Anchored<T> extends Codex<T, T> {
    //
  }
}
