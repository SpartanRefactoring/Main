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

  public abstract void acknowledge(event e);

  public abstract void acknowledge(event e, Object o);

  @Override public void acknowledge(Object... ¢) {
    if (¢ != null && ¢[0] instanceof event)
      if (¢.length == 0)
        acknowledge(¢[0]);
      else if (¢.length == 1)
        acknowledge(¢[0], ¢[1]);
  }
}
