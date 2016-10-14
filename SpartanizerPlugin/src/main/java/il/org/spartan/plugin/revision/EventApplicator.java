package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.jdt.core.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.utils.*;

// TODO Roth: move into separate file
/** Possible events during spartanization process */
enum event {
  run_start, run_finish, run_pass, run_pass_done, //
  visit_root, visit_cu, visit_node, //
}

/** An {@link Applicator} suitable for eclipse GUI.
 * @author Ori Roth
 * @since 2.6 */
public class EventApplicator extends Applicator<EventListener<event>> {
  /** Few passes for the applicator to conduct. */
  private static final int PASSES_FEW = 1;
  /** Many passes for the applicator to conduct. */
  private static final int PASSES_MANY = 20;

  /** Spartanization process. */
  @Override public void go() {
    if (selection() == null || listener() == null || passes() <= 0 || selection().isEmpty())
      return;
    listener().tick(event.visit_root, selection().name);
    listener().tick(event.run_start);
    if (!shouldRun())
      return;
    runContext().accept(() -> {
      final List<ICompilationUnit> alive = new LinkedList<>();
      alive.addAll(selection().compilationUnits);
      final int l = selection().textSelection != null ? 1 : passes();
      for (int pass = 0; pass < l; ++pass) {
        listener().tick(event.run_pass);
        if (!shouldRun())
          break;
        final List<ICompilationUnit> dead = new LinkedList<>();
        for (final ICompilationUnit ¢ : alive) {
          if (!runAction().apply(¢).booleanValue())
            dead.add(¢);
          listener().tick(event.visit_cu, ¢);
          if (!shouldRun())
            break;
        }
        listener().tick(event.run_pass_done);
        alive.removeAll(dead);
        if (alive.isEmpty() || !shouldRun())
          break;
      }
    });
    listener().tick(event.run_finish);
  }

  /** Default listener configuration of {@link EventApplicator}. Simple printing
   * to console.
   * @return this applicator */
  public EventApplicator defaultListener() {
    listener(EventListener.simpleListener(event.class, //
        e -> System.out.println(e), //
        (e, o) -> System.out.println(e + ":\t" + o)//
    ));
    return this;
  }

  /** Default selection configuration of {@link EventApplicator}. Normal eclipse
   * user selection.
   * @return this applicator */
  public EventApplicator defaultSelection() {
    selection(Selection.Util.get());
    return this;
  }

  /** Default passes configuration of {@link EventApplicator}, with few passes.
   * @return this applicator */
  public EventApplicator defaultPassesFew() {
    passes(PASSES_FEW);
    return this;
  }

  /** Default passes configuration of {@link EventApplicator}, with many passes.
   * @return this applicator */
  public EventApplicator defaultPassesMany() {
    passes(PASSES_MANY);
    return this;
  }

  /** Default run context configuration of {@link EventApplicator}. Simply runs
   * the {@link Runnable} in the current thread.
   * @return this applicator */
  public EventApplicator defaultRunContext() {
    runContext(r -> r.run());
    return this;
  }

  // TODO Roth: use Policy / replacement for Trimmer.
  /** Default run action configuration of {@link EventApplicator}. Spartanize
   * the {@link ICompilationUnit} using a {@link Trimmer}.
   * @return this applicator */
  public EventApplicator defaultRunAction() {
    final Trimmer t = new Trimmer();
    runAction(u -> Boolean.valueOf(t.apply(u, (selection().textSelection == null ? new Range(0, 0)
        : new Range(selection().textSelection.getOffset(), selection().textSelection.getOffset() + selection().textSelection.getLength())))));
    return this;
  }

  /** Default settings for all {@link Applicator} components.
   * @return this applicator */
  public EventApplicator defaultSettings() {
    return defaultListener().defaultPassesFew().defaultRunContext().defaultSelection();
  }
}
