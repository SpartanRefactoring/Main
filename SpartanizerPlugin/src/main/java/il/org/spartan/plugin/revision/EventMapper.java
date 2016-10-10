package il.org.spartan.plugin.revision;

import java.util.*;
import java.util.function.*;

import il.org.spartan.plugin.revision.GUIApplicator.*;

/** A {@link Listener} that listen to {@link event}s.
 * @author Ori Roth
 * @since 2016 */
public class EventMapper<E extends Enum<E>> extends EventListener<E> {
  private final Map<E, Object> eventMap;
  @SuppressWarnings("rawtypes") private final Map<E, EventFunctor> recorders;

  public EventMapper(final Class<? extends E> enumClass) {
    super(enumClass);
    eventMap = new HashMap<>();
    recorders = new HashMap<>();
    for (final E ¢ : events()) {
      eventMap.put(¢, null);
      recorders.put(¢, null);
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" }) @Override public void tick(final E e) {
    final EventFunctor f = recorders.get(e);
    if (f == null)
      return;
    if (!f.initialized)
      eventMap.put(e, f.initializeValue());
    f.update(eventMap);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" }) @Override public void tick(final E e, final Object o) {
    final EventFunctor f = recorders.get(e);
    if (f == null)
      return;
    if (!f.initialized)
      eventMap.put(e, f.initializeValue());
    f.update(eventMap, o);
  }

  /** Expend this EventMapper by adding a recorder.
   * @param ¢ JD
   * @return this EventMapper */
  @SuppressWarnings("unchecked") public EventMapper<E> expend(@SuppressWarnings("rawtypes") final EventFunctor ¢) {
    recorders.put((E) ¢.domain, ¢);
    return this;
  }

  /** @return an empty mapper, with no recorders. */
  public static <E extends Enum<E>> EventMapper<E> empty(final Class<? extends E> enumClass) {
    return new EventMapper<>(enumClass);
  }

  /** Factory method for {@link EventMapperFunctor}. */
  public static <E, P, O> EventMapperFunctor<E, P, O> recorderOf(final E ¢) {
    return new EventMapperFunctor<>(¢);
  }

  /** Factory method for {@link EventMapperFunctor}. Inspects
   * {@link EventMapper#eventMap}. [[SuppressWarningsSpartan]] */
  public static <E extends Enum<E>> EventMapperFunctor<E, Map<E, Object>, Object> inspectorOf(final E ¢) {
    return new EventMapperFunctor<E, Map<E, Object>, Object>(¢) {
      @Override public void update(final Map<E, Object> m) {
        consumer.accept(m);
      }

      @Override public void update(final Map<E, Object> m, final Object o) {
        biConsumer.accept(m, o);
      }
    };
  }

  /** Extendible functor used by the {@link EventMapper}. Works for specific
   * kind of {@link event}.
   * @author Ori Roth
   * @since 2016 */
  public static class EventFunctor<E, P, O> {
    protected final E domain;
    boolean initialized;
    protected P initialization;
    protected Supplier<P> initializationSupplier;

    public EventFunctor(final E domain) {
      this.domain = domain;
      initialized = true;
      initialization = null;
      initializationSupplier = null;
    }

    protected Object initializeValue() {
      assert !initialized;
      initialized = true;
      Object $;
      if (initializationSupplier == null) {
        $ = initialization;
        initialization = null;
      } else {
        $ = initializationSupplier.get();
        initializationSupplier = null;
      }
      return $;
    }

    @SuppressWarnings("unused") void update(final Map<E, Object> __, final O o) {
      //
    }

    @SuppressWarnings("unused") void update(final Map<E, Object> __) {
      //
    }
  }

  /** Updates the map of the {@link EventMapper} with each
   * {@link EventFunctor#update}.
   * @author Ori Roth
   * @since 2016 */
  public static class EventMapperFunctor<E, P, O> extends EventFunctor<E, P, O> {
    BiConsumer<P, O> biConsumer;
    Consumer<P> consumer;
    BiFunction<P, O, P> biFunction;
    Function<P, O> function;

    public EventMapperFunctor(final E domain) {
      super(domain);
      biConsumer = null;
      consumer = null;
      biFunction = null;
      function = null;
    }

    @SuppressWarnings("unchecked") public <X> EventMapperFunctor<E, X, O> startWith(final X ¢) {
      final EventMapperFunctor<E, X, O> $ = (EventMapperFunctor<E, X, O>) this;
      $.initialized = false;
      $.initialization = ¢;
      return $;
    }

    @SuppressWarnings("unchecked") public <X> EventMapperFunctor<E, X, O> startWithSupplyOf(final Supplier<X> ¢) {
      final EventMapperFunctor<E, X, O> $ = (EventMapperFunctor<E, X, O>) this;
      $.initialized = false;
      $.initializationSupplier = ¢;
      return $;
    }

    public EventMapperFunctor<E, P, O> does(final BiConsumer<P, O> ¢) {
      biConsumer = ¢;
      return this;
    }

    public EventMapperFunctor<E, P, O> does(final Consumer<P> ¢) {
      consumer = ¢;
      return this;
    }

    public EventMapperFunctor<E, P, O> does(final BiFunction<P, O, P> ¢) {
      biFunction = ¢;
      return this;
    }

    public EventMapperFunctor<E, P, O> does(final Function<P, O> ¢) {
      function = ¢;
      return this;
    }

    @Override @SuppressWarnings("unchecked") public void update(final Map<E, Object> e, final O o) {
      assert biConsumer == null || biFunction == null;
      if (biConsumer != null)
        biConsumer.accept((P) e.get(domain), o);
      if (biFunction != null)
        e.put(domain, biFunction.apply((P) e.get(domain), o));
    }

    @Override @SuppressWarnings("unchecked") public void update(final Map<E, Object> ¢) {
      assert consumer == null || function == null;
      if (consumer != null)
        consumer.accept((P) ¢.get(domain));
      if (function != null)
        ¢.put(domain, function.apply((P) ¢.get(domain)));
    }

    /** Used for casting */
    @SuppressWarnings({ "unchecked", "unused" }) public <X, Y> EventMapperFunctor<E, X, Y> gets(final Class<X> cp, final Class<Y> co) {
      return (EventMapperFunctor<E, X, Y>) this;
    }

    /** Used for casting TODO Roth: make it clear the casting is for O */
    @SuppressWarnings({ "unchecked", "unused" }) public <Y> EventMapperFunctor<E, P, Y> gets(final Class<Y> co) {
      return (EventMapperFunctor<E, P, Y>) this;
    }

    /** Remembers objects of specific type */
    @SuppressWarnings("unchecked") public <Y> EventMapperFunctor<E, HashSet<Y>, Y> rememberBy(@SuppressWarnings("unused") final Class<Y> __) {
      return ((EventMapperFunctor<E, HashSet<Y>, Y>) this) //
          .startWith(new HashSet<Y>()) //
          .does((l, u) -> {
            l.add(u);
          });
    }

    /** Collects objects of specific type */
    @SuppressWarnings("unchecked") public <Y> EventMapperFunctor<E, LinkedList<Y>, Y> collectBy(@SuppressWarnings("unused") final Class<Y> __) {
      return ((EventMapperFunctor<E, LinkedList<Y>, Y>) this) //
          .startWith(new LinkedList<Y>()) //
          .does((l, u) -> {
            l.add(u);
          });
    }

    /** Remember an object of specific type */
    @SuppressWarnings("unchecked") public <X> EventMapperFunctor<E, X, X> rememberLast(@SuppressWarnings("unused") final Class<X> __) {
      return ((EventMapperFunctor<E, X, X>) this) //
          .does((x, u) -> {
            return u;
          });
    }

    /** Counts calls */
    @SuppressWarnings("unchecked") public EventMapperFunctor<E, Integer, Integer> counter() {
      return ((EventMapperFunctor<E, Integer, Integer>) this) //
          .startWith(Integer.valueOf(0)) //
          .does(c -> {
            return Integer.valueOf(c.intValue() + 1);
          });
    }
  }

  private enum none {
    X
  }

  static class SimpleMapper extends EventMapper<none> {
    /** Empty enum used by {@link EventMapper#simpleMapper()} */
    public SimpleMapper(final Class<none> enumClass) {
      super(enumClass);
    }

    public static SimpleMapper get() {
      return new SimpleMapper(none.class) {
        @Override public void tick(final Object... ¢) {
          if (¢ != null)
            if (¢.length == 0)
              tick(none.X);
            else if (¢.length == 1)
              tick(none.X, ¢[1]);
        }
      };
    }

    public static <P, O> EventMapperFunctor<none, P, O> recorder() {
      return new EventMapperFunctor<>(none.X);
    }
  }
}
