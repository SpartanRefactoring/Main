package op.a1;

import java.util.*;
import java.util.function.*;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public interface Events {
  interface Delegator<S extends Events.Set> extends Listener, op.a0.Events.Delegator<S> {
    static <S extends Events.Set> Events.Delegator<S> to(S ¢) {
      return new Events.Delegator.ToOne<>(¢);
    }
    @Override default void end() {
      delegate(S::end);
    }

    abstract class Abstract<S extends Events.Set> extends op.a0.Events.Delegator.Abstract<S> implements Events.Delegator<S> {
      @Override public final void end() {
        Events.Delegator.super.end();
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
    interface Idle extends Events.Set, op.a0.Events.Idle {
      @Override default void end() {/**/}
    }
  }

  /* Empty protocol */
  interface Set extends Events, op.a0.Events.Set {
    void end();
  }
}
