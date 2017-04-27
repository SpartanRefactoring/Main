package il.org.spartan.spartanizer.plugin;

import java.util.*;
import java.util.concurrent.atomic.*;

/** An abstract listener taking events that may have any number of parameters of
 * any kind; default implementation is empty, override to specialize, or use
 * {@link Listener.S}
 * @author Ori Roth
 * @author Yossi Gil
 * @see #tick(Object...)
 * @see #push(Object...)
 * @see #pop(Object...)
 * @see TicksStack
 * @see StringBuilderListener demo implementation
 * @since 2.6 */
@FunctionalInterface
public interface Listener {
  AtomicLong eventId = new AtomicLong();

  /** Create a new id for an event
   * @param ¢ notification details
   * @return */
  static long newId() {
    return eventId.incrementAndGet();
  }

  default Listener asListener() {
    return this;
  }

  /** Used to restore a pushed listening session
   * @param ¢ notification details */
  default void pop(final Object... ¢) {
    tick(¢);
  }

  /** Begin a delimited listening session
   * @param ¢ notification details
   * @see #pop */
  default void push(final Object... ¢) {
    tick(¢);
  }

  /** Main listener function.
   * @param ¢ notification details */
  void tick(Object... os);

  /** An aggregating kind of {@link Listener} that dispatches the event it
   * receives to the multiple {@link Listener}s it stores internally.
   * @author Yossi Gil
   * @since 2.6 */
  class S extends ArrayList<Listener> implements Listener {
    private static final long serialVersionUID = 0x72E812BAFE0B3C7FL;

    /** for fluent API use, i.e., <code>
                                                                                                                                *
                                                                                                                                * <code>
                                                                                                                                        <b>public final</b> {@link Listener}  listeners = .
                                                                                                                            * </code>
     * @return an empty new instance */
    public static Listener.S empty() {
      return new Listener.S();
    }

    /** To be used in the nano found in {@link ConfigurableObjectTemplate}
     * @return {@code this} */
    public Listener listeners() {
      return this;
    }

    @Override public void tick(final Object... os) {
      asListener().tick(os);
      forEach(λ -> λ.tick(os));
    }
  }
}