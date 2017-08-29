package op.a0;

import java.util.*;
import java.util.function.*;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public interface Events {
  interface Delegator<S extends Events.Set> extends Listener {
    static <S extends Set> Delegator<S> to(S ¢) {
      return new Delegator.ToOne<>(¢);
    }
    @Override default void begin() {
      delegate(S::begin);
    }
    void delegate(Consumer<? super S> action);

    abstract class Abstract<S extends Events.Set> implements Delegator<S> {
      @Override public final void begin() {
        Delegator.super.begin();
      }
    }

    class Many<S extends Events.Set> extends ToAny<S> {
      List<Delegator<S>> inner = new ArrayList<>();

      void add(S ¢) {
        inner.add(new Delegator.ToOne<>(¢));
      }
      @Override Iterable<Delegator<S>> inner() {
        return inner;
      }
    }

    final class Stub<S extends Events.Set> extends Abstract<S> {
      @Override public void delegate(Consumer<? super S> action) {
        forget.it(action);
      }
    }

    abstract class ToAny<S extends Events.Set> extends Abstract<S> {
      @Override public final void delegate(Consumer<? super S> action) {
        for (Delegator<S> d : inner())
          d.delegate(action);
      }
      abstract Iterable<Delegator<S>> inner();
    }

    class ToOne<S extends Events.Set> extends Abstract<S> {
      private S inner;

      public ToOne(S inner) {
        this.inner = inner;
      }
      @Override public void delegate(Consumer<? super S> action) {
        if (action == null)
          forget.it(inner);
        else
          action.accept(inner);
      }
    }

    class Undefined<S extends Events.Set> extends ToOne<S> {
      public Undefined() {
        super(null);
      }
    }
  }
interface Listener extends Set {
  interface Default extends Set {
    @Override default void begin() { /**/}
  }
}
  interface Idle extends Set {
    @Override default void begin() { /**/}
  }

  /* Empty protocol */
  interface Set extends Events {
    void begin();
  }
}
