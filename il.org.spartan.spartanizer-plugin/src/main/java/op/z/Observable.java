package op.z;

import op.z.Listener.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-31 */
public class Observable<L extends Listener> {
  private ListenerContainer<L> listeners = initializeContainer();

  protected ListenerContainer<L> initializeContainer() {
    return new ListenerContainerImplementation<>();
  }
  @SuppressWarnings("unchecked") protected L listeners() {
    return (L) listeners;
  }
  public ListenerContainer<L> container() {
    return listeners;
  }
}
