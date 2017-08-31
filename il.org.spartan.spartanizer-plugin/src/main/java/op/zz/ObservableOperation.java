package op.zz;

import op.z.*;
import op.zz.OperationListener.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-31 */
public class ObservableOperation<L extends OperationListener> extends Observable<L> {
  @Override protected OperationListenerContainer<L> initializeContainer() {
    return new OperationListenerContainerImplementation<>();
  }
  public void go() {
    listeners().begin();
    listeners().end();
  }
}
