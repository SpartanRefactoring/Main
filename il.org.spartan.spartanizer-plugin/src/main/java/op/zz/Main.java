package op.zz;

import op.z.*;
import op.zz.OperationListener.*;

/** Brief example of {@link Listener}/{@link Observable} inheritence.
 * @author Ori Roth
 * @since 2017-08-31 */
public class Main {
  interface HelloListener extends OperationListener<HelloListener> {
    public default void sayHello() {/**/}
  }

  static class HelloListenerContainer extends OperationListenerContainer<HelloListener> implements HelloListener {
    private static final long serialVersionUID = -4655868501813965628L;

    @Override public void sayHello() {
      delegate(HelloListener::sayHello);
    }
  }

  static class HelloObservable extends ObservableOperation<HelloListener> {
    @Override protected HelloListenerContainer initializeContainer() {
      return new HelloListenerContainer();
    }
    @Override public void go() {
      listeners().begin();
      listeners().sayHello();
      listeners().end();
    }
  }

  public static void main(String[] args) {
    HelloObservable o = new HelloObservable();
    o.container().add(new HelloListener() {
      @Override public void begin() {
        System.out.println("BEGIN");
      }
      @Override public void sayHello() {
        System.out.println("Beginer: hello!");
      }
    });
    o.container().add(new HelloListener() {
      @Override public void end() {
        System.out.println("END");
      }
      @Override public void sayHello() {
        System.out.println("Ender: hello!");
      }
    });
    o.container().add(new HelloListener() {
      @Override public void sayHello() {
        System.out.println("Hello!");
      }
    });
    o.go();
  }
}
