package il.org.spartan.plugin.revision;

/** Possible events during spartanization process */
enum event {
  run_start, run_finish, run_pass, //
  visit_project, visit_cu, visit_node, //
}

/** An {@link Applicator} suitable for eclipse GUI.
 * @author Ori Roth
 * @since 2016 */
public class EventApplicator extends Applicator<EventListener<event>> {
  /** Spartanization process. */
  @Override public void go() {
    listener().tick(event.run_start);
    if (shouldRun())
      System.out.println(selection());
  }

  /** Default listener configuration of {@link EventApplicator}.
   * @return this applicator */
  public EventApplicator defaultListener() {
    listener(EventListener.simpleListener(event.class, e -> {
      System.out.println(e);
    }, (e, o) -> {
      System.out.println(e + ":\t" + o);
    }));
    return this;
  }

  /** Default selection configuration of {@link EventApplicator}.
   * @return this applicator */
  public EventApplicator defaultSelection() {
    selection(Selection.Util.get());
    return this;
  }
}
