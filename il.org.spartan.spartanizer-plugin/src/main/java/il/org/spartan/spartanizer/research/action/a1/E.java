package il.org.spartan.spartanizer.research.action.a1;

import java.util.*;
import java.util.function.*;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public interface E {
  interface Delegator<S extends E.Set> extends E.Set, il.org.spartan.spartanizer.research.action.a0.E.Delegator<S> {
    static <S extends E.Set> E.Delegator<S> to(S ¢) {
      return new E.Delegator.ToOne<>(¢);
    }
    @Override default void end() {
      delegate(S::end);
    }

    abstract class Abstract<S extends E.Set> extends il.org.spartan.spartanizer.research.action.a0.E.Delegator.Abstract<S> implements E.Delegator<S> {
      @Override public final void end() {
        E.Delegator.super.end();
      }
    }

    class Many<S extends E.Set> extends ToAny<S> {
      List<E.Delegator<S>> inner = new ArrayList<>();

      void add(S ¢) {
        inner.add(new E.Delegator.ToOne<>(¢));
      }
      @Override Iterable<E.Delegator<S>> inner() {
        return inner;
      }
    }

    final class Stub<S extends E.Set> extends Abstract<S> {
      @Override public void delegate(Consumer<? super S> action) {
        forget.it(action);
      }
    }

    abstract class ToAny<S extends E.Set> extends Abstract<S> {
      @Override public final void delegate(Consumer<? super S> action) {
        for (E.Delegator<S> d : inner())
          d.delegate(action);
      }
      abstract Iterable<E.Delegator<S>> inner();
    }

    class ToOne<S extends E.Set> extends Abstract<S> {
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

    class Undefined<S extends E.Set> extends ToOne<S> {
      public Undefined() {
        super(null);
      }
    }
  }

  interface Idle extends Set, il.org.spartan.spartanizer.research.action.a0.E.Idle {
    @Override default void end() {/**/}
  }

  /* Empty protocol */
  interface Set extends E, il.org.spartan.spartanizer.research.action.a0.E.Set {
    void end();
  }
}
