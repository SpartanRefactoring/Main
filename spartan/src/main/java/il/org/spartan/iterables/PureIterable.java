/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.iterables;

import java.util.Iterator;

/** A kind of {@link Iterable} which does not permit the rarely used
 * {@link Iterator#remove} operation and saves the user, i.e., whoever chooses
 * to <code><b>implements</b></code> this class, the trouble of providing a
 * vacuous implementation of this function.
 *
 * @see PureIterator
 * @author Yossi Gil
 * @since 2014-06-03
 * @param <T> some arbitrary type */
public interface PureIterable<T> extends Iterable<T> {
  @Override PureIterator<T> iterator();

  /** A kind of {@link PureIterable} which provides an additional {@link #size()}
   * service.
   * <p>
   * For the size service to be meaningful, there is an underlying contract
   * arranging the mutual expectations between two parties: the customer, and the
   * supplier. (Customers are clients of this class, a supplier is a non-abstract
   * class which extends this class):
   * <ol>
   * <li>The <i>size</i> of an instance is the number of elements it yields. It
   * should be clear that the size can never be negative.
   * <li>The instance always returns the same number of items. Or, a bit more
   * formally, all {@link Iterator} instances that method {@link #iterator()}
   * returns are exhausted after precisely the same number of calls to
   * {@link Iterator#hasNext()}
   * <li>This fixed size can be computed efficiently, specifically without
   * exhausting the sequence items that this iterable may yield.
   * </ol>
   *
   * @author Yossi Gil
   * @since 2014-06-20
   * @param <T> some arbitrary type */
  abstract class Sized<T> implements PureIterable<T> {
    /** see @see il.org.spartan.iterables.PureIterable#iterator()
     * (auto-generated) */
    @Override public PureIterator<T> iterator() {
      return null;
    }

    /** @return how many elements are there in this instance */
    public abstract int size();
  }
}