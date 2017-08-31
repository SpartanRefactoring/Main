package op.z;

import op.z.Listener.*;

/** Empty observable class. Successors may add methods and use different set of
 * listeners (type parameter {@code L}).
 * @author Ori Roth
 * @since 2017-08-31 */
public class Observable<L extends Listener<L>> {
  private ListenerContainer<L> listeners = initializeContainer();

  /** Successors allocate {@link Observable#listeners}. */
  protected ListenerContainer<L> initializeContainer() {
    return new ListenerContainer<>();
  }
  /** {@link Observable#listeners} presented as a delegation listener. */
  protected L listeners() {
    return listeners.self();
  }
  /** {@link Observable#listeners} presented as listeners container. */
  public ListenerContainer<L> container() {
    return listeners;
  }
}
