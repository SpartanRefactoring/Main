package op.z;

import java.util.*;
import java.util.function.*;

import fluent.ly.*;

/** Empty listener interface. Successors may add listening methods.
 * @author Ori Roth
 * @since 2017-08-31 */
public interface Listener<Self extends Listener<Self>> extends Selfie<Self> {
  /** Container for other listeners. Must be itself a Listener of type {@code L}
   * (or lower), as it implements {@code Selfie<L>}.
   * @author Ori Roth
   * @since 2017-08-31 */
  public interface ListenerContainer<L extends Listener<L>> extends Selfie<L>, Collection<L> {/**/}

  /** Standard, ordered implementation of {@link ListenerContainer}.
   * @param <L>
   * @author Ori Roth
   * @since 2017-08-31 */
  public class ListenerContainerImplementation<L extends Listener<L>> extends LinkedList<L> implements ListenerContainer<L> {
    private static final long serialVersionUID = -4606598464188455025L;

    protected void delegate(Consumer<L> delegation) {
      for (L listener : this)
        delegation.accept(listener);
    }
  }
}
