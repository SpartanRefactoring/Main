package op.zzz;

import java.util.*;

import op.zz.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-09-02 */
public abstract class ObservableHello<L extends HelloListener, Self extends ObservableHello<L, Self>> extends ObservableOperation<L, Self> {
  public String myName = "";

  @Override public void go() {
    listeners.begin();
    listeners.sayHello();
    listeners.end();
  }
  protected abstract L adjust(HelloListener listener);
  @Override protected L adjust(OperationListener listener) {
    return adjust(new HelloListener() {
      @Override public void begin() {
        listener.begin();
      }
      @Override public void end() {
        listener.end();
      }
    });
  }

  public class ConcreteHelloListener implements HelloListener {
    public String myName() {
      return myName;
    }
  }

  public static final class ConcreteHelloObservable extends ObservableHello<HelloListener, ConcreteHelloObservable> {
    @Override protected HelloListener delegation(List<HelloListener> inner) {
      return new HelloListener() {
        @Override public void begin() {
          delegate(inner, HelloListener::begin);
        }
        @Override public void sayHello() {
          delegate(inner, HelloListener::sayHello);
        }
        @Override public void end() {
          delegate(inner, HelloListener::end);
        }
      };
    }
    @Override protected HelloListener adjust(HelloListener listener) {
      return listener;
    }
  }

  public static void main(String[] args) {
    ConcreteHelloObservable h = new ConcreteHelloObservable();
    h.myName = "concrete";
    h.add(new HelloListener() {
      @Override public void begin() {
        System.out.println("begin");
      }
    }).add(h.new ConcreteHelloListener() {
      @Override public void sayHello() {
        System.out.println("hello " + myName());
      }
      @Override public void end() {
        System.out.println("end");
      }
    });
    h.go();
  }
}
