package op.zz;

import java.util.*;

import op.z.Observable;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-09-02 */
public abstract class ObservableOperation<L extends OperationListener, Self extends ObservableOperation<L, Self>> extends Observable<L, Self> {
  // TODO Roth: remove, part of the example
  {
    add(adjust(new OperationListener() {
      @Override public void begin() {
        System.out.println("begin (parent)");
      }
    }));
  }

  public final void go() {
    listeners.begin();
    listeners.end();
  }
  protected abstract L adjust(OperationListener listener);

  public class ConcreteOperationListener implements OperationListener {/**/}

  public static final class ConcreteOperationObservable extends ObservableOperation<OperationListener, ConcreteOperationObservable> {
    @Override protected OperationListener delegation(List<OperationListener> inner) {
      return new OperationListener() {
        @Override public void begin() {
          delegate(inner, OperationListener::begin);
        }
        @Override public void end() {
          delegate(inner, OperationListener::end);
        }
      };
    }
    @Override protected OperationListener adjust(OperationListener listener) {
      return listener;
    }
  }
}
