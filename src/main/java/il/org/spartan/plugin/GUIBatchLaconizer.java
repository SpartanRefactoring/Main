package il.org.spartan.plugin;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import org.eclipse.jdt.core.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;

/** Possible events during spartanization process
 * <p>
 * Why do we need to make such a strong binding between the event generator and
 * the listener? Why should they agree on a common type of events? We should let
 * the listener take, say strings, and just record them. */
@Deprecated enum event {
  run_start, run_finish, run_pass, run_pass_done, //
  visit_root, visit_cu, visit_node, //
}

/** An {@link Applicator} suitable for eclipse GUI.
 * @author Ori Roth
 * @since 2.6 */
public class GUIBatchLaconizer extends Applicator {
  /** Few passes for the applicator to conduct. */
  private static final int PASSES_FEW = 1;
  /** Many passes for the applicator to conduct. */
  private static final int PASSES_MANY = 20;

  /** Spartanization process. */
  @Override public void go() {
    if (selection() == null || listener() == null || passes() <= 0 || selection().isEmpty())
      return;
    listener().push(message.run_start.get(selection().name));
    if (!shouldRun())
      return;
    final AtomicInteger totalTipsInvoked = new AtomicInteger(0);
    runContext().accept(() -> {
      final int l = passes();
      for (int pass = 1; pass <= l; ++pass) {
        listener().push(message.run_pass.get(Integer.valueOf(pass)));
        if (!shouldRun())
          break;
        final List<WrappedCompilationUnit> selected = selection().inner;
        final List<WrappedCompilationUnit> alive = new ArrayList<>(selected);
        final List<WrappedCompilationUnit> dead = new ArrayList<>();
        for (final WrappedCompilationUnit ¢ : alive) {
          final int tipsInvoked = runAction().apply(¢.build()).intValue();
          if (tipsInvoked <= 0)
            dead.add(¢);
          ¢.dispose();
          listener().tick(message.visit_cu.get(Integer.valueOf(alive.indexOf(¢)), Integer.valueOf(alive.size()), ¢.descriptor.getElementName()));
          totalTipsInvoked.addAndGet(tipsInvoked);
          if (!shouldRun())
            break;
        }
        listener().pop(message.run_pass_finish.get(Integer.valueOf(pass)));
        selected.removeAll(dead);
        if (selected.isEmpty() || !shouldRun())
          break;
      }
    });
    // TODO Roth: add metrics etc.
    listener().pop(message.run_finish.get(selection().name, totalTipsInvoked));
  }
  /** Default listener configuration of {@link GUIBatchLaconizer}. Simple
   * printing to console.
   * @return this applicator */
  public GUIBatchLaconizer defaultListenerNoisy() {
    listener(os -> {
      for (final Object ¢ : os)
        System.out.print(¢ + " ");
      System.out.println();
    });
    return this;
  }
  /** Default listener configuration of {@link GUIBatchLaconizer}. Silent
   * listener.
   * @return this applicator */
  public GUIBatchLaconizer defaultListenerSilent() {
    listener((final Object... __) -> {
      //
    });
    return this;
  }
  /** Default selection configuration of {@link GUIBatchLaconizer}. Normal
   * eclipse user selection.
   * @return this applicator */
  public GUIBatchLaconizer defaultSelection() {
    selection(Selection.Util.current());
    return this;
  }
  /** Default passes configuration of {@link GUIBatchLaconizer}, with few
   * passes.
   * @return this applicator */
  public GUIBatchLaconizer defaultPassesFew() {
    passes(PASSES_FEW);
    return this;
  }
  /** Default passes configuration of {@link GUIBatchLaconizer}, with many
   * passes.
   * @return this applicator */
  public GUIBatchLaconizer defaultPassesMany() {
    passes(PASSES_MANY);
    return this;
  }
  /** Default run context configuration of {@link GUIBatchLaconizer}. Simply
   * runs the {@link Runnable} in the current thread.
   * @return this applicator */
  public GUIBatchLaconizer defaultRunContext() {
    runContext(r -> r.run());
    return this;
  }
  // TODO Roth: use Policy / replacement for Trimmer.
  /** Default run action configuration of {@link GUIBatchLaconizer}. Spartanize
   * the {@link ICompilationUnit} using received {@link AbstractGUIApplicator}.
   * @param a JD
   * @return this applicator */
  public GUIBatchLaconizer defaultRunAction(final AbstractGUIApplicator a) {
    setRunAction(¢ -> Integer.valueOf(a.apply(¢, selection())));
    name(a.getName());
    return this;
  }
  /** Default settings for all {@link Applicator} components.
   * @return this applicator */
  public GUIBatchLaconizer defaultSettings() {
    return defaultListenerSilent().defaultPassesFew().defaultRunContext().defaultSelection().defaultRunAction(new Trimmer());
  }
  /** Factory method.
   * @return default event applicator */
  public static GUIBatchLaconizer defaultApplicator() {
    return new GUIBatchLaconizer().defaultSettings();
  }

  /** Printing definition of events that occur during spartanization.
   * @author Ori Roth
   * @since 2.6 */
  private enum message {
    run_start(1, inp -> "Spartanizing " + printableAt(inp, 0)), //
    run_pass(1, inp -> "Pass #" + printableAt(inp, 0)), //
    run_pass_finish(1, inp -> "Pass #" + printableAt(inp, 0) + " finished"), //
    visit_cu(3, inp -> printableAt(inp, 0) + "/" + printableAt(inp, 1) + "\tSpartanizing " + printableAt(inp, 2)), //
    run_finish(2, inp -> "Done spartanizing " + printableAt(inp, 0) + "\nTips accepted: " + printableAt(inp, 1));
    private final int inputCount;
    private final Function<Object[], String> printing;

    message(final int inputCount, final Function<Object[], String> printing) {
      this.inputCount = inputCount;
      this.printing = printing;
    }
    public String get(final Object... ¢) {
      assert ¢.length == inputCount;
      return printing.apply(¢);
    }
    private static String printableAt(final Object[] os, final int index) {
      return Linguistic.unknownIfNull(os, xs -> xs[index]);
    }
  }
}
