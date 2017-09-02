package op.y;

import java.util.*;

/** Observable operation. Contains a {@link ObservableOperation#go()} method.
 * @author Ori Roth
 * @since 2017-09-01 */
public class ObservableOperation<Self extends ObservableOperation<Self>> extends Observable<Self> {
  /** Listener for an operation, records operation beginning and ending.
   * @author Ori Roth
   * @since 2017-09-01 */
  public class OperationListener extends Listener {
    public void begin() {/**/}
    public void end() {/**/}
  }

  protected List<OperationListener> inner = new LinkedList<>();
  public OperationListener listeners = new OperationListener() {
    @Override public void begin() {
      delegate(inner, OperationListener::begin);
    }
    @Override public void end() {
      delegate(inner, OperationListener::end);
    }

    // TODO Roth: remove, part of the example
    {
      add(new OperationListener() {
        @Override public void begin() {
          System.out.println("parent begins");
        };
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
