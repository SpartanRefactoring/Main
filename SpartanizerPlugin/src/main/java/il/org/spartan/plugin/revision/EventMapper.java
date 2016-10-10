package il.org.spartan.plugin.revision;

import java.util.*;
import java.util.function.*;

/** A {@link Listener} that listen to {@link event}s.
 * @author Ori Roth
 * @since 2016 */
public class EventMapper extends EventListener {
  private final Map<event, Object> eventMap;
  @SuppressWarnings("rawtypes") private final Map<event, EventFunctor> recorders;

  public EventMapper() {
    eventMap = new HashMap<>();
    recorders = new HashMap<>();
    for (final event ¢ : event.values()) {
      eventMap.put(¢, null);
      recorders.put(¢, null);
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" }) @Override public void tick(final event e) {
    EventFunctor f = recorders.get(e);
    if (f == null)
      return;
    if (!f.initialized)
      eventMap.put(e, f.initializeValue());
    f.update(eventMap);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" }) @Override public void tick(final event e, final Object o) {
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
  public EventMapper expend(@SuppressWarnings("rawtypes") final EventFunctor ¢) {
    recorders.put(¢.domain, ¢);
    return this;
  }

  /** @return an empty mapper, with no recorders. */
  public static EventMapper empty() {
    return new EventMapper();
  }

  /** Factory method for {@link EventMapperFunctor}. */
  public static <P, O> EventMapperFunctor<P, O> recorderOf(final event ¢) {
    return new EventMapperFunctor<>(¢);
  }

  /** Factory method for {@link EventMapperFunctor}. Inspects
   * {@link EventMapper#eventMap}. [[SuppressWarningsSpartan]] */
  public static EventMapperFunctor<Map<event, Object>, Object> inspectorOf(final event ¢) {
    return new EventMapperFunctor<Map<event, Object>, Object>(¢) {
      @Override public void update(Map<event, Object> m) {
        consumer.accept(m);
      }
      @Override public void update(Map<event, Object> m, Object o) {
        biConsumer.accept(m, o);
      }
    };
  }

  /** Extendible functor used by the {@link EventMapper}. Works for specific
   * kind of {@link event}.
   * @author Ori Roth
   * @since 2016 */
  public static class EventFunctor<O> {
    protected final event domain;
    boolean initialized;
    protected Object initialization;
    protected Supplier<Object> initializationSupplier;

    public EventFunctor(final event domain) {
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

    @SuppressWarnings("unused") void update(final Map<event, Object> e, final O o) {
      //
    }

    @SuppressWarnings("unused") void update(final Map<event, Object> e) {
      //
    }
  }

  /** Updates the map of the {@link EventMapper} with each
   * {@link EventFunctor#update}.
   * @author Ori Roth
   * @since 2016 */
  public static class EventMapperFunctor<P, O> extends EventFunctor<O> {
    BiConsumer<P, O> biConsumer;
    Consumer<P> consumer;
    BiFunction<P, O, P> biFunction;
    Function<P, O> function;

    public EventMapperFunctor(final event domain) {
      super(domain);
      biConsumer = null;
      consumer = null;
      biFunction = null;
      function = null;
    }

    public EventMapperFunctor<P, O> startWith(final Object ¢) {
      initialized = false;
      initialization = ¢;
      return this;
    }

    public EventMapperFunctor<P, O> startWith(final Supplier<Object> ¢) {
      initialized = false;
      initializationSupplier = ¢;
      return this;
    }

    public EventMapperFunctor<P, O> does(final BiConsumer<P, O> ¢) {
      biConsumer = ¢;
      return this;
    }

    public EventMapperFunctor<P, O> does(final Consumer<P> ¢) {
      consumer = ¢;
      return this;
    }

    public EventMapperFunctor<P, O> does(final BiFunction<P, O, P> ¢) {
      biFunction = ¢;
      return this;
    }

    public EventMapperFunctor<P, O> does(final Function<P, O> ¢) {
      function = ¢;
      return this;
    }

    @Override @SuppressWarnings("unchecked") public void update(final Map<event, Object> e, final O o) {
      assert (biConsumer == null || biFunction == null);
      if (biConsumer != null)
        biConsumer.accept((P) e.get(domain), o);
      if (biFunction != null)
        e.put(domain, biFunction.apply((P) e.get(domain), o));
    }

    @Override @SuppressWarnings("unchecked") public void update(final Map<event, Object> ¢) {
      assert (consumer == null || function == null);
      if (consumer != null)
        consumer.accept((P) ¢.get(domain));
      if (function != null)
        ¢.put(domain, function.apply((P) ¢.get(domain)));
    }

    /** Used for casting */
    @SuppressWarnings({ "unchecked", "unused" }) public <X, Y> EventMapperFunctor<X, Y> gets(final Class<X> cp, final Class<Y> co) {
      return (EventMapperFunctor<X, Y>) this;
    }

    /** Remembers objects of specific type */
    @SuppressWarnings("unchecked") public <Y> EventMapperFunctor<Collection<Y>, Y> rememberBy(@SuppressWarnings("unused") final Class<Y> __) {
      return ((EventMapperFunctor<Collection<Y>, Y>) this) //
          .startWith(new HashSet<>()) //
          .does((l, u) -> {
            l.add(u);
          });
    }
    
    /** Collects objects of specific type */
    @SuppressWarnings("unchecked") public <Y> EventMapperFunctor<Collection<Y>, Y> collectBy(@SuppressWarnings("unused") final Class<Y> __) {
      return ((EventMapperFunctor<Collection<Y>, Y>) this) //
          .startWith(new LinkedList<>()) //
          .does((l, u) -> {
            l.add(u);
          });
    }

    /** Remember an object of specific type */
    @SuppressWarnings("unchecked") public <X> EventMapperFunctor<X, X> rememberLast(@SuppressWarnings("unused") final Class<X> __) {
      return ((EventMapperFunctor<X, X>) this) //
          .does((x, u) -> {
            return u;
          });
    }

    /** Counts calls */
    @SuppressWarnings("unchecked") public EventMapperFunctor<Integer, Integer> counter() {
      return ((EventMapperFunctor<Integer, Integer>) this) //
          .startWith(Integer.valueOf(0)) //
          .does(c -> {
            return Integer.valueOf(c.intValue() + 1);
          });
    }
  }
}
