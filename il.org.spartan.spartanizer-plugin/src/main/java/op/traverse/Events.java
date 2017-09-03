package op.traverse;

import java.util.*;
import java.util.function.*;

import fluent.ly.*;

/** Do not sort
 * @author Yossi Gil
 * @since 2017-08-24 */
interface Events {
  interface Delegator<S extends Events.Set> extends Listener, ParentEvents.Delegator<S> {
    static <S extends Events.Set> Events.Delegator<S> to(S ¢) {
      return new Events.Delegator.ToOne<>(¢);
    }
    @Override default void beginCorpus() {
      delegate(S::beginCorpus);
    }
    @Override default void beginFile() {
      delegate(S::beginFile);
    }
    @Override default void beginProject() {
      delegate(S::beginProject);
    }
    @Override default void endCorpus() {
      delegate(S::endCorpus);
    }
    @Override default void endFile() {
      delegate(S::endFile);
    }
    @Override default void endProject() {
      delegate(S::endProject);
    }

    abstract class Abstract<S extends Events.Set> extends ParentEvents.Delegator.Abstract<S> implements Events.Delegator<S> {
      @Override public void beginCorpus() {
        Events.Delegator.super.beginCorpus();
      }
      @Override public void beginFile() {
        Events.Delegator.super.beginFile();
      }
      @Override public void beginProject() {
        Events.Delegator.super.beginProject();
      }
      @Override public void endCorpus() {
        Events.Delegator.super.endCorpus();
      }
      @Override public void endFile() {
        Events.Delegator.super.endFile();
      }
      @Override public void endProject() {
        Events.Delegator.super.endProject();
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
    interface Idle extends Events.Set, ParentEvents.Listener.Idle {
      @Override default void beginCorpus() {/**/}
      @Override default void beginFile() {/**/}
      @Override default void beginProject() {/**/}
      @Override default void endCorpus() {/**/}
      @Override default void endFile() {/**/}
      @Override default void endProject() {/**/}
    }
  }

  /* Empty protocol */
  interface Set extends Events, ParentEvents.Protocol {
    void beginCorpus();
    void beginFile();
    void beginProject();
    void endCorpus();
    void endFile();
    void endProject();
  }
}