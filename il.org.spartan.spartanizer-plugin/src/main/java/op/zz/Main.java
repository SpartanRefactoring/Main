package op.zz;

import op.zz.OperationListener.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-31 */
public class Main {
  interface HelloListener extends OperationListener {
    public default void sayHello() {/**/}
  }

  interface HelloListenerContainer<L extends HelloListener> extends OperationListenerContainer<L>, HelloListener {/**/}

  static class HelloListenerContainerImplementation<L extends HelloListener> extends OperationListenerContainerImplementation<L>
      implements HelloListenerContainer<L> {
    private static final long serialVersionUID = -4655868501813965628L;

    @Override public void sayHello() {
      delegate(HelloListener::sayHello);
    }
  }

  static class HelloObservable extends ObservableOperation<HelloListener> {
    @Override protected HelloListenerContainer<HelloListener> initializeContainer() {
      return new HelloListenerContainerImplementation<>();
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
