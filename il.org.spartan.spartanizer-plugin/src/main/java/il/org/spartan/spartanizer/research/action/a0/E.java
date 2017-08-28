package il.org.spartan.spartanizer.research.action.a0;

import java.util.*;
import java.util.function.*;

import fluent.ly.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-28 */
public interface E {
  interface Delegator<S extends E.Set> extends E.Set {
    static <S extends Set> Delegator<S> to(S ¢) {
      return new Delegator.ToOne<>(¢);
    }
    // @formatter:off
    @Override
    default void begin() {
      delegate(S::begin);
    }


    void delegate(Consumer<? super S> action);

    // @formatter:on
    abstract class Abstract<S extends E.Set> implements Delegator<S> {
      // @formatter:off
      @Override
      public final void begin() {
        Delegator.super.begin();
      }

      // @formatter:on
    }

    class Many<S extends E.Set> extends ToAny<S> {
      List<Delegator<S>> inner = new ArrayList<>();

      void add(S ¢) {
        inner.add(new Delegator.ToOne<>(¢));
      }
      @Override Iterable<Delegator<S>> inner() {
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
        for (Delegator<S> d : inner())
          d.delegate(action);
      }
      abstract Iterable<Delegator<S>> inner();
    }

    class ToOne<S extends E.Set> extends Abstract<S> {
      private S inner;

      public ToOne(S inner) {
        this.inner = inner;
      }
      @Override public void delegate(Consumer<? super S> action) {
        action.accept(inner);
      }
    }

    class Undefined<S extends E.Set> extends ToOne<S> {
      public Undefined() {
        super(null);
      }
    }
  }

  interface Idle extends Set {
    @Override default void begin() { /**/}
  }

  /* Empty protocol */
  interface Set extends E {
    void begin();
  }
}
