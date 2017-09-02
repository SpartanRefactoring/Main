package op.z;

import java.util.*;

import fluent.ly.*;

/** Observable object.
 * @author Ori Roth
 * @since 2017-09-01 */
@SuppressWarnings("hiding")
public abstract class Observable<L extends Listener, Self extends Observable<L, Self>> implements Selfie<Self> {
  private List<L> inner = new LinkedList<>();
  public L listeners = delegation(inner);

  protected abstract L delegation(List<L> inner);
  public Self add(L listener) {
    inner.add(listener);
    return self();
  }

  public static final class ConcreteListener implements Listener {/**/}

  public static final class ConcreteObservable extends Observable<Listener, ConcreteObservable> {
    @Override protected Listener delegation(@SuppressWarnings("unused") List<Listener> inner) {
      return new Listener() {/**/};
    }
  }
}
