package op.y;

import java.util.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-09-01 */
@SuppressWarnings("hiding")
public class ObservableHello<Self extends ObservableHello<Self>> extends ObservableOperation<Self> {
  /** TODO Ori Roth: document class
   * @author Ori Roth
   * @since 2017-09-01 */
  public interface HelloListener extends OperationListener {
    public default void sayHello() {/**/}
  }

  class HelloDelegator extends OperationDelegator implements HelloListener {
    @Override public void begin() {
      super.begin();
      for (OperationListener listener : inner)
        listener.begin();
    }
    @Override public void end() {
      super.end();
      for (OperationListener listener : inner)
        listener.end();
    }
    @Override public void sayHello() {
      for (HelloListener listener : inner)
        listener.sayHello();
    }
  }

  protected List<HelloListener> inner = new LinkedList<>();
  public HelloListener listeners = new HelloDelegator();

  public Self add(HelloListener listener) {
    inner.add(listener);
    return self();
  }
  @Override public void go() {
    listeners.begin();
    listeners.sayHello();
    listeners.end();
  }
  public static void main(String[] args) {
    class ObservableHelloEndpoint extends ObservableHello<ObservableHelloEndpoint> {/**/}
    ObservableHelloEndpoint h = new ObservableHelloEndpoint();
    h.add(new OperationListener() {
      @Override public void begin() {
        System.out.println("begin");
      }
    }).add(new HelloListener() {
      @Override public void end() {
        System.out.println("end");
      }
      @Override public void sayHello() {
        System.out.println("hello");
      }
    });
    h.go();
  }
}
