package il.org.spartan.spartanizer.plugin;

import java.util.*;
import java.util.function.*;

import il.org.spartan.utils.*;

/** A {@link Listener} that listen to {@link event}s. Maps both the recorders
 * and the results to the events. The recorders can be {@link Function}s,
 * {@link BiFunction}s, {@link Consumers}s or {@link BiConsumer}s.
 * @author Ori Roth
 * @since 2.6 */
public class EventMapper<E extends Enum<?>> extends EventListener<E> {
  /** Factory method.
   * @return an empty mapper, with no recorders. */
  public static <E extends Enum<?>> EventMapper<E> empty(final Class<? extends E> enumClass) {
    return new EventMapper<>(enumClass);
  }

  /** Factory method for {@link EventMapperFunctor}. Inspects the
   * {@link EventMapper#eventMap}. Used to inspect the collected data, rather
   * than update it. */
  public static <E extends Enum<E>> EventMapperFunctor<E, Map<E, Object>, Object> inspectorOf(final E ¢) {
    return new EventMapperFunctor<E, Map<E, Object>, Object>(¢) {
      @Override public void update(final Map<E, Object> ¢1) {
        consumer.accept(¢1);
      }

      @Override public void update(final Map<E, Object> m, final Object o) {
        biConsumer.accept(m, o);
      }
    };
  }

  /** Factory method for {@link EventMapperFunctor}. */
  public static <E, P, O> EventMapperFunctor<E, P, O> recorderOf(final E ¢) {
    return new EventMapperFunctor<>(¢);
  }

  /** Results mapping. */
  private final Map<E, Object> eventMap = new HashMap<>();
  /** Recorders mapping. In the current implementation only one recorder is
   * available for each event, though the functions/consumers can be merged
   * together. */
  private final Map<E, EventFunctor<?, ?, ?>> recorders = new HashMap<>();

  /** Initialize mapping according to specific events defined in the enum.
   * @param enumClass contains possible events for this listener */
  EventMapper(final Class<? extends E> enumClass) {
    super(enumClass);
    for (final E ¢ : events()) { // NANO
      eventMap.put(¢, null);
      recorders.put(¢, null);
    }
  }

  /** Expend this EventMapper by adding a recorder.
   * @param ¢ JD
   * @return {@code this} EventMapper */
  @SuppressWarnings("unchecked") public EventMapper<E> expand(@SuppressWarnings("rawtypes") final EventFunctor ¢) {
    recorders.put((E) ¢.domain, ¢);
    return this;
  }

  /** @param event JD
   * @return the recorder mapped to this event */
  @SuppressWarnings("rawtypes") public EventFunctor recorder(final E event) {
    return recorders.get(event);
  }

  @Override @SuppressWarnings({ "unchecked", "rawtypes" }) public void tick(final E e) {
    final EventFunctor f = recorders.get(e);
    if (f == null)
      return;
    if (!f.initialized)
      eventMap.put(e, f.initializeValue());
    f.update(eventMap);
  }

  @Override @SuppressWarnings({ "unchecked", "rawtypes" }) public void tick(final E e, final Object o) {
    final EventFunctor f = recorders.get(e);
    if (f == null)
      return;
    if (!f.initialized)
      eventMap.put(e, f.initializeValue());
    f.update(eventMap, o);
  }

  /** Extendible functor used by the {@link EventMapper}. Works for specific
   * kind of {@link event}.
   * @author Ori Roth
   * @since 2.6 */
  public static class EventFunctor<E, P, O> {
    /** The event covered by this functor. */
    final E domain;
    /** Whether or not the value mapped in {@link EventMapper#eventMap} for
     * {@link EventFunctor#domain} has been initialized. */
    boolean initialized;
    /** Initialization value for {@link EventMapper#eventMap}. */
    P initialization;
    /** Initialization supplier for {@link EventMapper#eventMap}. */
    Supplier<P> initializationSupplier;

    /** Creates a functor for a specific event.
     * @param domain the event covered by this functor. */
    public EventFunctor(final E domain) {
      this.domain = domain;
      initialized = true;
    }

    /** @return initialization value for this functor, either from
     *         {@link EventFunctor#initialization} or from
     *         {@link EventFunctor#initializationSupplier}. */
    Object initializeValue() {
      assert !initialized;
      initialized = true;
      final Object $;
      if (initializationSupplier == null) {
        $ = initialization;
        initialization = null;
      } else {
        $ = initializationSupplier.get();
        initializationSupplier = null;
      }
      return $;
    }

    /** Update the map. Empty implementation.
     * @param __ JD */
    @SuppressWarnings("unused") void update(final Map<E, Object> __) {
      //
    }

    /** Update the map. Empty implementation.
     * @param __ JD
     * @param o object listened with the event */
    @SuppressWarnings("unused") void update(final Map<E, Object> __, final O o) {
      //
    }
  }

  /** Updates the map of the {@link EventMapper} with each
   * {@link EventFunctor#update}. Suitable for listening to events or events and
   * objects, using a {@link Function}, a {@link BiFunction}, a
   * {@link Consumers} or a {@link BiConsumer}. A possible flaw of this class is
   * the unclear override if the consumers/functions.
   * @author Ori Roth
   * @since 2.6 */
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

    /** Collects objects of specific __ in a {@link List}. Conducts casting. */
    @SuppressWarnings("unchecked") public <Y> EventMapperFunctor<E, ArrayList<Y>, Y> collectBy(@SuppressWarnings("unused") final Class<Y> __) {
      return ((EventMapperFunctor<E, ArrayList<Y>, Y>) this) //
          .startWith(new ArrayList<Y>()) //
          .does((BiConsumer<ArrayList<Y>, Y>) ArrayList::add);
    }

    /** Counts calls of this event. Conducts casting. */
    @SuppressWarnings("unchecked") public EventMapperFunctor<E, Int, Int> counter() {
      return ((EventMapperFunctor<E, Int, Int>) this) //
          .startWith(new Int()) //
          .does(λ -> λ.step());
    }

    /** Setting biconsumer for this functor. May join with existing biconsumer.
     * @param ¢ JD
     * @return {@code this} functor. */
    public EventMapperFunctor<E, P, O> does(final BiConsumer<P, O> ¢) {
      biConsumer = biConsumer == null ? ¢ : biConsumer.andThen(¢);
      return this;
    }

    /** Setting bifunction for this functor.
     * @param ¢ JD
     * @return {@code this} functor. */
    public EventMapperFunctor<E, P, O> does(final BiFunction<P, O, P> ¢) {
      biFunction = ¢;
      return this;
    }

    /** Setting consumer for this functor. May join with existing
     * consumer/biconsumer.
     * @param ¢ JD
     * @return {@code this} functor. */
    public EventMapperFunctor<E, P, O> does(final Consumer<P> ¢) {
      consumer = consumer == null ? ¢ : consumer.andThen(¢);
      biConsumer = biConsumer == null ? null : biConsumer.andThen((final P p, final O __) -> ¢.accept(p));
      return this;
    }

    /** Setting function for this functor.
     * @param ¢ JD
     * @return {@code this} functor. */
    public EventMapperFunctor<E, P, O> does(final Function<P, O> ¢) {
      function = ¢;
      return this;
    }

    /** Used for casting */
    @SuppressWarnings({ "unchecked", "unused" }) public <X, Y> EventMapperFunctor<E, X, Y> gets(final Class<X> cp, final Class<Y> co) {
      return (EventMapperFunctor<E, X, Y>) this;
    }

    // TODO Ori Roth: make it clear the casting is for O
    /** Used for casting. */
    @SuppressWarnings({ "unchecked", "unused" }) public <Y> EventMapperFunctor<E, P, Y> gets(final Class<Y> co) {
      return (EventMapperFunctor<E, P, Y>) this;
    }

    /** Remembers objects of specific __ in a {@link HashSet}. Conducts
     * casting. */
    @SuppressWarnings("unchecked") public <Y> EventMapperFunctor<E, HashSet<Y>, Y> rememberBy(@SuppressWarnings("unused") final Class<Y> __) {
      return ((EventMapperFunctor<E, HashSet<Y>, Y>) this) //
          .startWith(new HashSet<Y>()) //
          .does((BiConsumer<HashSet<Y>, Y>) HashSet::add);
    }

    /** Remember the last object received of specific __. Conducts casting. */
    @SuppressWarnings("unchecked") public <X> EventMapperFunctor<E, X, X> rememberLast(@SuppressWarnings("unused") final Class<X> __) {
      return ((EventMapperFunctor<E, X, X>) this) //
          .does((x, u) -> u);
    }

    /** Determines initialization value for this functor. Conducts casting.
     * @param ¢ JD
     * @return {@code this} functor. */
    @SuppressWarnings("unchecked") public <X> EventMapperFunctor<E, X, O> startWith(final X ¢) {
      final EventMapperFunctor<E, X, O> $ = (EventMapperFunctor<E, X, O>) this;
      $.initialized = false;
      $.initialization = ¢;
      return $;
    }

    /** Determines initialization value for this functor using a supplier.
     * Conducts casting.
     * @param ¢ JD
     * @return {@code this} functor. */
    @SuppressWarnings("unchecked") public <X> EventMapperFunctor<E, X, O> startWithSupplyOf(final Supplier<X> ¢) {
      final EventMapperFunctor<E, X, O> $ = (EventMapperFunctor<E, X, O>) this;
      $.initialized = false;
      $.initializationSupplier = ¢;
      return $;
    }

    /** Updates the map using {@link EventMapperFunctor#consumer} or
     * {@link EventMapperFunctor#function} */
    @Override @SuppressWarnings("unchecked") public void update(final Map<E, Object> ¢) {
      assert consumer == null || function == null;
      if (consumer != null)
        consumer.accept((P) ¢.get(domain));
      if (function != null)
        ¢.put(domain, function.apply((P) ¢.get(domain)));
    }

    /** Updates the map with the object using
     * {@link EventMapperFunctor#biConsumer} or
     * {@link EventMapperFunctor#biFunction} */
    @Override @SuppressWarnings("unchecked") public void update(final Map<E, Object> m, final O o) {
      assert biConsumer == null || biFunction == null;
      if (biConsumer != null)
        biConsumer.accept((P) m.get(domain), o);
      if (biFunction != null)
        m.put(domain, biFunction.apply((P) m.get(domain), o));
    }
  }

  /** Empty enum used by {@link EventMapper#simpleMapper()} */
  private enum none {
    X
  }

  /** An event mapper for anonymous, single value enum.
   * @author Ori Roth
   * @since 2.6 */
  static class SimpleMapper extends EventMapper<none> {
    /** Factory method.
     * @return empty simple mapper */
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

    SimpleMapper(final Class<none> enumClass) {
      super(enumClass);
    }
  }
}
