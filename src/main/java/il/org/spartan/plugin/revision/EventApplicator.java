package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.*;

import il.org.spartan.plugin.*;
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
public class EventApplicator<F, T, S extends AbstractSelection<F, T>> extends Applicator<EventListener<event>, F, T, S> {
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
      final List<F> alive = new LinkedList<>();
      alive.addAll(selection().compilationUnits);
      final int l = selection().textSelection != null ? 1 : passes();
      for (int pass = 0; pass < l; ++pass) {
        listener().tick(event.run_pass);
        if (!shouldRun())
          break;
        final List<F> dead = new LinkedList<>();
        for (final F ¢ : alive) {
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
  public EventApplicator<F, T, S> defaultListenerNoisy() {
    listener(EventListener.simpleListener(event.class, //
        e -> System.out.println(e), //
        (e, o) -> System.out.println(e + ":\t" + o)//
    ));
    return this;
  }

  /** Default listener configuration of {@link EventApplicator}. Silent
   * listener.
   * @return this applicator */
  public EventApplicator<F, T, S> defaultListenerSilent() {
    listener(EventListener.simpleListener(event.class, //
        e -> {
          //
        }, (e, o) -> {
          //
        }));
    return this;
  }

  /** Default selection configuration of {@link EventApplicator}. Normal eclipse
   * user selection.
   * @return this applicator */
  @SuppressWarnings("unchecked") public EventApplicator<ICompilationUnit, ITextSelection, Selection> defaultSelection() {
    ((EventApplicator<ICompilationUnit, ITextSelection, Selection>) this).selection(Selection.Util.get());
    return (EventApplicator<ICompilationUnit, ITextSelection, Selection>) this;
  }

  /** Default passes configuration of {@link EventApplicator}, with few passes.
   * @return this applicator */
  public EventApplicator<F, T, S> defaultPassesFew() {
    passes(PASSES_FEW);
    return this;
  }

  /** Default passes configuration of {@link EventApplicator}, with many passes.
   * @return this applicator */
  public EventApplicator<F, T, S> defaultPassesMany() {
    passes(PASSES_MANY);
    return this;
  }

  /** Default run context configuration of {@link EventApplicator}. Simply runs
   * the {@link Runnable} in the current thread.
   * @return this applicator */
  public EventApplicator<F, T, S> defaultRunContext() {
    runContext(r -> r.run());
    return this;
  }

  // TODO Roth: use Policy / replacement for Trimmer.
  /** Default run action configuration of {@link EventApplicator}. Spartanize
   * the {@link ICompilationUnit} using a {@link Trimmer}.
   * @return this applicator */
  @SuppressWarnings("unchecked") public EventApplicator<ICompilationUnit, ITextSelection, Selection> defaultRunAction() {
    final Trimmer t = new Trimmer();
    ((EventApplicator<ICompilationUnit, ITextSelection, Selection>) this).runAction(u -> Boolean.valueOf(t.apply(u,
        selection().textSelection == null ? new Range(0, 0)
            : new Range(((EventApplicator<ICompilationUnit, ITextSelection, Selection>) this).selection().textSelection.getOffset(),
                ((EventApplicator<ICompilationUnit, ITextSelection, Selection>) this).selection().textSelection.getOffset()
                    + ((EventApplicator<ICompilationUnit, ITextSelection, Selection>) this).selection().textSelection.getLength()))));
    return (EventApplicator<ICompilationUnit, ITextSelection, Selection>) this;
  }

  /** Default run action configuration of {@link EventApplicator}. Spartanize
   * the {@link ICompilationUnit} using received {@link GUI$Applicator}.
   * @param a JD
   * @return this applicator */
  @SuppressWarnings("unchecked") public EventApplicator<ICompilationUnit, ITextSelection, Selection> defaultRunAction(final GUI$Applicator a) {
    ((EventApplicator<ICompilationUnit, ITextSelection, Selection>) this).runAction(u -> Boolean.valueOf(a.apply(u,
        (selection().textSelection == null ? new Range(0, 0)
            : new Range(((EventApplicator<ICompilationUnit, ITextSelection, Selection>) this).selection().textSelection.getOffset(),
                ((EventApplicator<ICompilationUnit, ITextSelection, Selection>) this).selection().textSelection.getOffset()
                    + ((EventApplicator<ICompilationUnit, ITextSelection, Selection>) this).selection().textSelection.getLength())))));
    return (EventApplicator<ICompilationUnit, ITextSelection, Selection>) this;
  }

  /** Default settings for all {@link Applicator} components.
   * @return this applicator */
  public EventApplicator<ICompilationUnit, ITextSelection, Selection> defaultSettings() {
    return defaultListenerSilent().defaultPassesFew().defaultRunContext().defaultSelection().defaultRunAction();
  }

  /** Factory method.
   * @return default event applicator */
  public static EventApplicator<ICompilationUnit, ITextSelection, Selection> defaultApplicator() {
    return new EventApplicator<ICompilationUnit, ITextSelection, Selection>().defaultSettings();
  }
}
