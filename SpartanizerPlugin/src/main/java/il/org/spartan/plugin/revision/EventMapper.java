package il.org.spartan.plugin.revision;

import java.util.*;
import java.util.function.*;

/** A {@link Listener} that listen to {@link event}s.
 * @author Ori Roth
 * @since 2016 */
public class EventMapper extends EventListener {
  protected final Map<event, Object> eventMap;
  @SuppressWarnings("rawtypes") protected final List<EventFunctor> recorders;

  public EventMapper() {
    eventMap = new HashMap<>();
    for (event ¢ : event.values())
      eventMap.put(¢, null);
    recorders = new ArrayList<>();
  }

  @SuppressWarnings({ "unchecked", "rawtypes" }) @Override public void acknowledge(event e) {
    for (final EventFunctor ¢ : recorders)
      if (¢.domain(e))
        ¢.update(eventMap);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" }) @Override public void acknowledge(event e, Object o) {
    for (final EventFunctor ¢ : recorders)
      if (¢.domain(e))
        ¢.update(eventMap, o);
  }

  /** Expend this EventMapper by adding a recorder.
   * @param ¢ JD
   * @return this EventMapper */
  public EventMapper expend(@SuppressWarnings("rawtypes") final EventFunctor ¢) {
    recorders.add(¢);
    return this;
  }

  /** @return an empty mapper, with no recorders. */
  public static EventMapper empty() {
    return new EventMapper();
  }

  /** Extendible functor used by the {@link EventMapper}. Works for specific
   * kind of {@link event}.
   * @author Ori Roth
   * @since 2016 */
  public static class EventFunctor<O> {
    final event domain;

    public EventFunctor(final event domain) {
      this.domain = domain;
    }

    public boolean domain(final event ¢) {
      return domain != null && domain.equals(¢);
    }

    @SuppressWarnings("unused") void update(final Map<event, Object> e, O o) {
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
    BiFunction<P, O, P> biFunction;
    Function<P, P> function;

    public EventMapperFunctor(final event domain) {
      super(domain);
      biFunction = null;
      function = null;
    }

    public EventMapperFunctor<P, O> does(final BiFunction<P, O, P> ¢) {
      biFunction = ¢;
      return this;
    }

    public EventMapperFunctor<P, O> does(final Function<P, P> ¢) {
      function = ¢;
      return this;
    }

    @Override @SuppressWarnings("unchecked") public void update(final Map<event, Object> e, O o) {
      e.put(domain, biFunction.apply((P) e.get(domain), o));
    }

    /** Factory method */
    public static <P, O> EventMapperFunctor<P, O> recorderOf(final event ¢) {
      return new EventMapperFunctor<>(¢);
    }

    /** Used for casting */
    @SuppressWarnings({ "unchecked", "unused" }) public <X, Y> EventMapperFunctor<X, Y> gets(final Class<X> cp, Class<Y> co) {
      return (EventMapperFunctor<X, Y>) this;
    }

    /** Collects objects of specific type */
    @SuppressWarnings("unchecked") public <Y> EventMapperFunctor<Collection<Y>, Y> collectBy(@SuppressWarnings("unused") final Class<Y> __) {
      return ((EventMapperFunctor<Collection<Y>, Y>) this).does((l, u) -> {
        final List<Y> $ = l != null ? (List<Y>) l : new LinkedList<>();
        $.add(u);
        return $;
      });
    }

    /** Remember an object of specific type */
    @SuppressWarnings("unchecked") public <X> EventMapperFunctor<X, X> remember(@SuppressWarnings("unused") final Class<X> __) {
      return ((EventMapperFunctor<X, X>) this).does((l, u) -> {
        return u;
      });
    }

    /** Counts calls */
    @SuppressWarnings("unchecked") public EventMapperFunctor<Integer, Integer> counter() {
      return ((EventMapperFunctor<Integer, Integer>) this).does(c -> {
        return Integer.valueOf((c == null ? 0 : c.intValue()) + 1);
      });
    }
  }
}
