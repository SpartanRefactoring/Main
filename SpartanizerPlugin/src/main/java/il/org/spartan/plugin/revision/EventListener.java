package il.org.spartan.plugin.revision;

/** Listen to {@link event}s, with an optional additional object.
 * @author Ori Roth
 * @since 2016 */
public abstract class EventListener implements Listener {
  /** Possible events during spartanization process */
  enum event {
    run_start, run_finish, run_pass, //
    visit_project, visit_cu, visit_node, //
  }

  public abstract void tick(event e);

  public abstract void tick(event e, Object o);

  @Override public void tick(final Object... ¢) {
    if (¢ != null && ¢[0] instanceof event)
      if (¢.length == 0)
        tick(¢[0]);
      else if (¢.length == 1)
        tick(¢[0], ¢[1]);
  }
}
