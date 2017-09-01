package op.z;

import java.util.*;

import op.yy.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-09-01 */
public class ObservableHello<Self extends ObservableHello<Self>> extends ObservableOperation<Self> implements HelloListener {
  @SuppressWarnings("hiding") protected List<HelloListener> inner = new LinkedList<>();

  public Self add(HelloListener listener) {
    inner.add(listener);
    super.add(new OperationListener() {
      @Override public void begin() {
        listener.begin();
      }
      @Override public void end() {
        listener.end();
      }
    });
    return self();
  }
  @Override public void sayHello() {
    for (HelloListener listener : inner)
      listener.sayHello();
  }
  @Override public void go() {
    begin();
    sayHello();
    end();
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
