package op.end;

import java.util.*;
import java.util.function.*;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
interface Parent extends op.begin.Events {/**/}

public interface Events extends Parent {
  void end();

  interface Delegator<S extends Events> extends Listener, Parent.Delegator<S> {
    static <S extends Events> Events.Delegator<S> to(S ¢) {
      return new Events.Delegator.ToOne<>(¢);
    }
    @Override default void end() {
      delegate(S::end);
    }

    abstract class Abstract<S extends Events> extends Parent.Delegator.Abstract<S> implements Events.Delegator<S> {
      @Override public final void end() {
        Events.Delegator.super.end();
      }
    }

    class Many<S extends Events> extends ToAny<S> {
      List<Events.Delegator<S>> inner = new ArrayList<>();

      void add(S ¢) {
        inner.add(new Events.Delegator.ToOne<>(¢));
      }
      @Override Iterable<Events.Delegator<S>> inner() {
        return inner;
      }
    }

    final class Stub<S extends Events> extends Abstract<S> {
      @Override public void delegate(Consumer<? super S> action) {
        forget.it(action);
      }
    }

    abstract class ToAny<S extends Events> extends Abstract<S> {
      @Override public final void delegate(Consumer<? super S> action) {
        for (Events.Delegator<S> d : inner())
          d.delegate(action);
      }
      abstract Iterable<Events.Delegator<S>> inner();
    }

    class ToOne<S extends Events> extends Abstract<S> {
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

    class Undefined<S extends Events> extends ToOne<S> {
      public Undefined() {
        super(null);
      }
    }
  }

  interface Listener extends Events, Parent.Listener {
    interface Default extends Events, Parent.Listener.Default {
      @Override default void end() {/**/}
    }
  }
}
