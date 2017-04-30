package il.org.spartan.spartanizer.plugin;

import java.util.function.*;

/** Listen to events, with an optional additional object. The events are defined
 * in received enum class.
 * @author Ori Roth
 * @since 2.6 */
public abstract class EventListener<E extends Enum<?>> implements Listener {
  /** Enum class, contains possible events for this listener. */
  private final Class<? extends E> enumClass;

  /** Listens to an event.
   * @param e JD */
  protected abstract void tick(E e);

  /** Listens to an event with additional object.
   * @param e JD
   * @param o JD */
  protected abstract void tick(E e, Object o);

  /** @param enumClass enum that contains the possible events for this
   *        listener */
  EventListener(final Class<? extends E> enumClass) {
    this.enumClass = enumClass;
  }

  /** @return possible events for this listener */
  E[] events() {
    return enumClass.getEnumConstants();
  }

  @Override @SuppressWarnings("unchecked") public void tick(final Object... ¢) {
    if (¢ != null && enumClass.isInstance(¢[0]))
      if (¢.length == 1)
        tick((E) ¢[0]);
      else if (¢.length == 2)
        tick((E) ¢[0], ¢[1]);
  }

  /** Simple event listener, defined by a consumer of events.
   * @param enumClass enum that contains the possible events for this listener
   * @param c operation to be conducted on accepted event
   * @return listener that send events from the enum class to the consumer */
  public static <E extends Enum<?>> EventListener<E> simpleListener(final Class<E> enumClass, final Consumer<E> c) {
    return new EventListener<E>(enumClass) {
      @Override public void tick(final E ¢) {
        c.accept(¢);
      }

      @Override public void tick(final E e, @SuppressWarnings("unused") final Object __) {
        c.accept(e);
      }
    };
  }

  /** Simple event listener, defined by consumers of events.
   * @param enumClass enum that contains the possible events for this listener
   * @param c operation to be conducted on accepted event
   * @param bc operation to be conducted on accepted event and object
   * @return listener that send events from the enum class to consumers */
  public static <E extends Enum<?>> EventListener<E> simpleListener(final Class<E> enumClass, final Consumer<E> c, final BiConsumer<E, Object> bc) {
    return new EventListener<E>(enumClass) {
      @Override public void tick(final E ¢) {
        c.accept(¢);
      }

      @Override public void tick(final E e, final Object o) {
        bc.accept(e, o);
      }
    };
  }
}
