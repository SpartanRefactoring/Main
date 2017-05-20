package il.org.spatan.iteration;
import il.org.spartan.itertion.Codex;
/** A codex recording a given <i>enumeration</i>, i;.e., fixed mapping of a set
 * of n elements of a specified type into the integers 0,..., n-1.
 * @author Yossi Gil
 * @since Dec 3, 2009
 * @param <T> Type of encoded elements. */
public class Enumerating<T> extends Codex.Anchored<T> {
  private static int[] invert(final int[] is) {
    final int[] $ = new int[is.length];
    for (int ¢ = 0; ¢ < is.length; ++¢)
      $[is[¢]] = ¢;
    return $;
  }

  private final Codex.Anchored<T> codex;
  private final int[] rankOf;
  private final int[] withRank;

  /** Instantiate this class with a given codex and ranking vector.
   * @param codex an arbitrary object encoding of some set
   * @param rankOf a vector defining ranks of the objects in this set. All ranks
   *        (i.e., elements of this array) must be in the range 0,..., n-1 and
   *        no two positions in this array should have the same value. */
  public Enumerating(final Codex.Anchored<T> codex, final int[] rankOf) {
    this.codex = codex;
    this.rankOf = rankOf;
    this.withRank = invert(rankOf);
  }
  @Override public boolean contains(final T ¢) {
    return codex.contains(¢);
  }
  @Override public T decode(final int ¢) {
    return codex.decode(withRank[¢]);
  }
  @Override public Iterable<? extends T> elements() {
    return codex.elements();
  }
  @Override public int encode(final T ¢) {
    return rankOf[codex.encode(¢)];
  }
  /** Determines whether there are any elements associated with a given level
   * @param order an arbitrary integer
   * @return <code><b>true<b></code> <em>if and only if</em> there are elements
   *         associated with this level */
  public boolean hasOrder(final int order) {
    return order >= 0 && order < size();
  }
  /** Determines the level of a given object.
   * @param ¢ an arbitrary object, for which {@link #contains} returns true.
   * @return the level of this object, which is guaranteed to be an integer in
   *         the range <tt>0</tt> through <tt>{@link #size()} -1 </tt>. */
  public int order(final T ¢) {
    return encode(¢);
  }
  @Override public int size() {
    return codex.size();
  }
  /** Which element or elements have a given level.
   * @param order an integer in the range <tt>0</tt> through
   *        <tt>{@link Iterables#count}(elements())</tt>, for which
   *        {@link #hasOrder(int)} returns <code><b>true<b></code>.
   * @return the object or objects (as per subclass specification) which have
   *         (or has) this level */
  public T withOrder(final int order) {
    return decode(order);
  }
}
