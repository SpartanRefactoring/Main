package il.org.spartan.plugin.revision;

import il.org.spartan.plugin.revision.GUIApplicator.*;

/** Listen to {@link event}s, with an optional additional object.
 * @author Ori Roth
 * @since 2016 */
public abstract class EventListener<E extends Enum<E>> implements Listener {
  private final Class<? extends E> enumClass;

  public abstract void tick(E e);

  public abstract void tick(E e, Object o);

  protected EventListener(final Class<? extends E> enumClass) {
    this.enumClass = enumClass;
  }

  protected E[] events() {
    return enumClass.getEnumConstants();
  }

  @SuppressWarnings("unchecked") @Override public void tick(final Object... ¢) {
    if (¢ != null && enumClass.isInstance(¢[0]))
      if (¢.length == 1)
        tick((E) ¢[0]);
      else if (¢.length == 2)
        tick((E) ¢[0], ¢[1]);
  }
}
