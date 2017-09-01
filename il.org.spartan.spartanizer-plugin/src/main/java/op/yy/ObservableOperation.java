package op.yy;

import java.util.*;

import op.y.Observable;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-09-01 */
public class ObservableOperation<Self extends ObservableOperation<Self>> extends Observable<Self> {
  public class OperationDelegator implements OperationListener {
    @Override public void begin() {
      for (OperationListener listener : inner)
        listener.begin();
    }
    @Override public void end() {
      for (OperationListener listener : inner)
        listener.end();
    }
  }

  protected List<OperationListener> inner = new LinkedList<>();
  public OperationListener listeners = new OperationDelegator() {
    {
      add(new OperationListener() {
        @Override public void begin() {
          System.out.println("parent begins");
        }
      });
    }
  };

  public Self add(OperationListener listener) {
    inner.add(listener);
    return self();
  }
  public void go() {
    listeners.begin();
    listeners.end();
  }
}
