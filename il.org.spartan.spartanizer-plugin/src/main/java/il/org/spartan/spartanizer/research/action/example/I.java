package il.org.spartan.spartanizer.research.action.example;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
class I<S extends E.Set, Self extends I<S, Self>> implements Selfie<Self> {
  public final E.Delegator.Many<S> listeners = new E.Delegator.Many<>();

  public final Self withListener(S ¢) {
    listeners.add(¢);
    return self();
  }
  public final void go() {
    listeners.begin();
    listeners.end();
  }

  static final class Accessor extends I<E.Set, Accessor> {/**/}

  public static void main(String[] args) {
    Accessor a = new Accessor();
    a.withListener(new E.Idle() {
      @Override public void begin() {
        System.out.println("begin");
      }
    });
    a.withListener(new E.Idle() {
      @Override public void begin() {
        //
      }
    });
    a.withListener(new E.Idle() {
      @Override public void end() {
        System.out.println("end");
      }
    });
    a.go();
  }
}
