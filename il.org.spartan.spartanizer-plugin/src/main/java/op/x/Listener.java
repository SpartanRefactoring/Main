package op.x;

import java.util.*;

/** Empty listener interface. Successors may add listening methods.
 * @author Ori Roth
 * @since 2017-08-31 */
public interface Listener {
  /** Container for other listeners. Must be itself a Listener of type {@code L}
   * (or lower), as it implements {@code AssignableFrom<L>}.
   * @param <L>
   * @author Ori Roth
   * @since 2017-08-31 */
  public class ListenerContainer<L extends Listener> implements Listener, DelegatorContainer<L, ListenerContainer<L>> {
    private List<L> inner = new LinkedList<>();

    @Override public Collection<L> inner() {
      return inner;
    }
  }
}
