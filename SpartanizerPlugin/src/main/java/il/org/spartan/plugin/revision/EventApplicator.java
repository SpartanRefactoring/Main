package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.jdt.core.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.utils.*;

/** Possible events during spartanization process */
enum event {
  run_start, run_finish, run_pass, //
  visit_root, visit_cu, visit_node, //
}

/** An {@link Applicator} suitable for eclipse GUI.
 * @author Ori Roth
 * @since 2016 */
public class EventApplicator extends Applicator<EventListener<event>> {
  private static final int PASSES_FEW = 1;
  private static final int PASSES_MANY = 20;

  /** Spartanization process. */
  @Override public void go() {
    goTrimmer();
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

  /** Default run context configuration of {@link EventApplicator}.
   * @return this applicator */
  public EventApplicator defaultRunContext() {
    runContext(r -> {
      r.run();
    });
    return this;
  }

  /** Temporary solution. */
  private void goTrimmer() {
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
        final Trimmer trimmer = new Trimmer();
        listener().tick(event.run_pass);
        final List<ICompilationUnit> dead = new LinkedList<>();
        for (final ICompilationUnit ¢ : alive) {
          Range r = selection().textSelection == null ? new Range(0, 0)
              : new Range(selection().textSelection.getOffset(), selection().textSelection.getOffset() + selection().textSelection.getLength());
          if (!trimmer.apply(¢, r))
            dead.add(¢);
          listener().tick(event.visit_cu, ¢);
        }
        alive.removeAll(dead);
        if (alive.isEmpty())
          break;
      }
    });
    listener().tick(event.run_finish);
  }
}
