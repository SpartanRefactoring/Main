package op.y;

import java.util.*;

/** Simple example of inheritance from an {@link Observable}.
 * @author Ori Roth
 * @since 2017-09-01 */
@SuppressWarnings({ "hiding", "synthetic-access" })
public class ObservableHello<Self extends ObservableHello<Self>> extends ObservableOperation<Self> {
  public String name = "unnamed";

  /** Listener for hello operation.
   * @author Ori Roth
   * @since 2017-09-01 */
  public class HelloListener extends OperationListener {
    public void sayHello() {/**/}
    public String name() {
      return ObservableHello.this.name;
    }
  }

  protected List<HelloListener> inner = new LinkedList<>();
  public HelloListener listeners = new HelloListener() {
    @Override public void begin() {
      ObservableHello.super.listeners.begin();
    }
    @Override public void end() {
      ObservableHello.super.listeners.end();
    }
    @Override public void sayHello() {
      delegate(inner, HelloListener::sayHello);
    }
  };

  public Self add(HelloListener listener) {
    inner.add(new HelloListener() {
      @Override public void sayHello() {
        listener.sayHello();
      }
    });
    ObservableHello.super.add(new HelloListener() {
      @Override public void begin() {
        listener.begin();
      }
      @Override public void end() {
        listener.end();
      }
    });
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
    h.name = "example";
    h.add(h.new OperationListener() {
      @Override public void begin() {
        System.out.println("begin");
      }
    }).add(h.new HelloListener() {
      @Override public void end() {
        System.out.println("end");
      }
      @Override public void sayHello() {
        System.out.println("hello " + name());
      }
    });
    h.go();
  }
}
