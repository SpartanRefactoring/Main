package op;

import java.util.*;
import java.util.function.*;

import fluent.ly.*;

/** Reifies the notion of an event.
 * @author Yossi Gil
 * @since 2017-09-03 */
abstract class Event<Self extends Event<Self>> implements Selfie<Self> {
  @Override public abstract int hashCode();
  @SuppressWarnings("unchecked") @Override public final boolean equals(Object o) {
    if (o == null)
      return false;
    if (this.getClass() != o.getClass())
      return false;
    return equals((Self) o);
  }
  protected abstract boolean equals(Self e);
  Event<?> before() {
    return new Secondary() {
      @Override protected Event<?> inner() {
        return Event.this;
      }
    };
  }
  Event<?> after() {
    return new Secondary() {
      @Override protected Event<?> inner() {
        return Event.this;
      }
    };
  }
}

class Dispatcher {
  private LinkedHashMap<Event<?>, List<Runnable>> table = new LinkedHashMap<>();

  void disptach(Consumer<Listener.Protocol> c) {
    dispatch(Primary.of(c));
  }
  void dispatch(Event<?> e) {
    if (!table.containsKey(e))
      return;
    if (table.containsKey(e.before()))
      dispatch(e.before());
    for (Runnable ¢ : table.get(e))
      ¢.run();
    if (table.containsKey(e.after()))
      dispatch(e.after());
  }
  void hook(Event<?> e, Runnable r) {
    table.putIfAbsent(e, new ArrayList<>()).add(r);
  }
}

final class Primary<E extends Listener.Protocol> extends Event<Primary<E>> implements Consumer<E> {
  static <E extends Listener.Protocol> Primary<E> of(Consumer<E> inner) {
    return new Primary<>(inner);
  }

  public final Consumer<E> inner;

  public Primary(Consumer<E> inner) {
    this.inner = inner;
  }
  @Override public void accept(E t) {
    inner.accept(t);
  }
  @Override protected boolean equals(Primary<E> e) {
    return inner.equals(e.inner);
  }
  @Override public int hashCode() {
    return inner.hashCode();
  }
}

abstract class Secondary<E extends Listener.Protocol> extends Event<Secondary<E>> {
  @Override public final int hashCode() {
    return 31 * inner().hashCode() + getClass().hashCode();
  }
  protected abstract Event<?> inner();
  @Override protected final boolean equals(Secondary<E> s) {
    return inner().equals(s.inner());
  }
}

interface Disptacher {}
