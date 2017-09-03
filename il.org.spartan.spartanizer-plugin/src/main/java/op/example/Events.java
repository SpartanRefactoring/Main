package op.example;

import java.util.*;
import java.util.function.*;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public interface Events {
  interface Delegator<S extends Events.Set> extends Listener, op.end.Events.Delegator<S> {
    static <S extends Events.Set> Events.Delegator<S> to(S ¢) {
      return new Events.Delegator.ToOne<>(¢);
    }
    @Override default void action1() {
      delegate(S::action1);
    }
    @Override default void action2() {
      delegate(S::action2);
    }

    abstract class Abstract<S extends Events.Set> extends op.end.Events.Delegator.Abstract<S> implements Events.Delegator<S> {
      @Override public void action1() {
        Events.Delegator.super.action1();
      }
      @Override public void action2() {
        Events.Delegator.super.action2();
      }
    }

    class Many<S extends Events.Set> extends ToAny<S> {
      List<Events.Delegator<S>> inner = new ArrayList<>();

      void add(S ¢) {
        inner.add(new Events.Delegator.ToOne<>(¢));
      }
      @Override Iterable<Events.Delegator<S>> inner() {
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
        for (Events.Delegator<S> d : inner())
          d.delegate(action);
      }
      abstract Iterable<Events.Delegator<S>> inner();
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
    interface Idle extends Events.Set, op.end.Listener.Listener.Idle {
      @Override default void action1() {/**/}
      @Override default void action2() {/**/}
    }
  }

  /* Empty protocol */
  interface Set extends Events, op.end.Protocol.Set {
    void action1();
    void action2();
  }
}
