package op.yy;

import java.util.*;

import op.y.Observable;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-09-01 */
public class ObservableOperation<Self extends ObservableOperation<Self>> extends Observable<Self> implements OperationListener {
  protected List<OperationListener> inner = new LinkedList<>();

  public Self add(OperationListener listener) {
    inner.add(listener);
    return self();
  }
  @Override public void begin() {
    for (OperationListener listener : inner)
      listener.begin();
  }
  @Override public void end() {
    for (OperationListener listener : inner)
      listener.end();
  }
  public void go() {
    begin();
    end();
  }
}
