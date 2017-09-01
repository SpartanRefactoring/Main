package op.xx;

import op.x.*;
import op.xx.OperationListener.*;

/** Operation that accepts {@link OperationListener}s.
 * @author Ori Roth
 * @since 2017-08-31 */
public class ObservableOperation<L extends OperationListener> extends Observable<L> {
  @Override protected OperationListenerContainer<L> initializeContainer() {
    return new OperationListenerContainer<>();
  }
  public void go() {
    listeners().begin();
    listeners().end();
  }
}
