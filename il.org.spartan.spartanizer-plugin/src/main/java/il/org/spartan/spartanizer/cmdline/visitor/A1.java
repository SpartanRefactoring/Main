package il.org.spartan.spartanizer.cmdline.visitor;

import fluent.ly.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface A1 {
  void go();

  class I<S extends E.Set, Self extends I<?, ?>> implements A1, Selfie<Self> {
    public final E.Delegator.Many<S> listeners = new E.Delegator.Many<>();

    public final Self withListener(S ¢) {
      listeners.add(¢);
      return self();
    }
    @Override public final void go() {
      listeners.end();
    }
  }

  interface E {
    interface Delegator<S extends E.Set> extends E.Set {
      static <S extends Set> Delegator<S> to(S ¢) {
        return new Delegator.ToOne<>(¢);
      }
      // @formatter:off
      @Override
      default void end() {
        delegate(S::end);
      }


      void delegate(Consumer<? super S> action);

      // @formatter:on
      abstract class Abstract<S extends E.Set> implements A1.E.Delegator<S> {
        // @formatter:off
        @Override
        public final void end() {
          A1.E.Delegator.super.end();
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
      @Override default void end() { /**/}
    }

    /* Empty protocol */
    interface Set extends A0.E.Set {
      void end();
    }
  }
}
