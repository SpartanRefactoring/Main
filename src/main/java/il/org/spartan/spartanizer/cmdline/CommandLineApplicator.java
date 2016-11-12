package il.org.spartan.spartanizer.cmdline;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;

/** An {@link Applicator} suitable for the command line.
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineApplicator extends Applicator {
  private static final int PASSES_FEW = 1;
  private static final int PASSES_MANY = 20;

  public static CommandLineApplicator defaultApplicator() {
    return new CommandLineApplicator().defaultSettings();
  }
  // private final CommandLine$Applicator a = new CommandLine$Applicator();
  /** Default listener configuration. Simple printing to console.
   * @return this applicator */
  @Override public CommandLineApplicator defaultListenerNoisy() {
    listener(os -> {
      for (final Object ¢ : os)
        System.out.print(¢ + " ");
      System.out.println();
    });
    return this;
  }
  /** @return this */
  public CommandLineApplicator defaultListenerSilent() {
    listener((final Object... __) -> {
      //
    });
    return this;
  }
  /** @return this */
  public CommandLineApplicator defaultPassesFew() {
    passes(PASSES_FEW);
    return this;
  }
  /** Default passes configuration, with many passes.
   * @return this applicator */
  public CommandLineApplicator defaultPassesMany() {
    passes(PASSES_MANY);
    return this;
  }
  // TODO Matteo: I have changed the "run action" to return number of tips
  // committed instead of whether tips were committed (Boolean -> Integer).
  // Added a quick fix to your code. Also I do not understand why you wrote this
  // - we will inspect it once we meet. --or
  /** Applies {@link Spartanizer$Applicator} by default.
   * @return this */
  public CommandLineApplicator defaultRunAction() {
    System.out.println("defaultRunAction");
    // final Trimmer t = new Trimmer();
    final Spartanizer$Applicator s = new Spartanizer$Applicator();
    setRunAction(u -> Integer.valueOf(s.apply(u, selection()) ? 1 : 0));
    return this;
  }
  // // TODO Roth: use Policy / replacement for Trimmer.
  // /** Default run action configuration of {@link GUIBatchLaconizer}.
  // Spartanize the
  // * {@link ICompilationUnit} using received {@link AbstractGUIApplicator}.
  // * @param a JD
  // * @return this applicator */
  // public GUIBatchLaconizer defaultRunAction(final AbstractGUIApplicator a) {
  // setRunAction(¢ -> Integer.valueOf(a.apply(¢, selection())));
  // name(a.getName());
  // return this;
  // }
  /** Default run action configuration of {@link CommandLineApplicator}.
   * Spartanize the {@link CompilationUnit} using received TODO maybe this
   * method are going to die (as well as Spartanize$Applicator)
   * {@link Spartanizer$Applicator}.
   * @param a JD
   * @return this applicator */
  public CommandLineApplicator defaultRunAction(final Spartanizer$Applicator a) {
    setRunAction(u -> Integer.valueOf(a.apply(u, selection()) ? 1 : 0));
    name(a.getClass().getSimpleName());
    return this;
  }
  /** Default run action configuration of {@link CommandLineApplicator}.
   * Spartanize the {@link CompilationUnit} using received
   * {@link CommandLine$Applicator}.
   * @param a JD
   * @return this applicator */
  @Override public CommandLineApplicator defaultRunAction(final CommandLine$Applicator a) {
    setRunAction(u -> Integer.valueOf(a.apply(u, selection()) ? 1 : 0));
    name(a.getClass().getSimpleName());
    return this;
  }
  /** @return this */
  public CommandLineApplicator defaultRunContext() {
    runContext(r -> r.run());
    return this;
  }
  /** @return this */
  CommandLineApplicator defaultSelection() {
    // selection(CommandLineSelection.Util.get()); // temporarily disabled
    return this;
  }
  /** @param ¢ JD
   * @return */
  @Override public CommandLineApplicator defaultSelection(@SuppressWarnings("rawtypes") final AbstractSelection ¢) {
    selection(¢);
    return this;
  }
  /** @return this */
  private CommandLineApplicator defaultSettings() {
    return defaultListenerSilent().defaultPassesFew().defaultRunContext().defaultSelection().defaultRunAction();
  }
  /* @see il.org.spartan.plugin.revision.Applicator#go() */
  @Override public void go() {
    if (selection() == null && listener() == null && passes() <= 0 && selection().isEmpty())
      return;
    listener().push(message.run_start.get(selection().name));
    // TODO Matteo: report listener -- matteo
    // listener().push(message.report_start.get());
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
          final int tipsInvoked = runAction().apply(¢).intValue();
          System.out.println("tipsInvoked: " + tipsInvoked);
          if (tipsInvoked <= 0)
            dead.add(¢);
          ¢.dispose(); // nullify the CompilationUnit associated to an
                       // ICompilationUnit
          listener().tick(message.visit_cu.get(Integer.valueOf(alive.indexOf(¢)), Integer.valueOf(alive.size()), "unknown"));
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
    listener().pop(message.run_finish.get(selection().name, totalTipsInvoked));
  }

  private enum message {
    run_start(1, inp -> "Spartanizing " + printableAt(inp, 0)), //
    run_pass(1, inp -> "Pass #" + printableAt(inp, 0)), //
    run_pass_finish(1, inp -> "Pass #" + printableAt(inp, 0) + " finished"), //
    visit_cu(3, inp -> printableAt(inp, 0) + "/" + printableAt(inp, 1) + "\tSpartanizing " + printableAt(inp, 2)), //
    run_finish(2, inp -> "Done spartanizing " + printableAt(inp, 0) + "\nTips accepted: " + printableAt(inp, 1)),
    // report
    report_start(1, inp -> "Start reporting " + printableAt(inp, 0)), //
    report_stop(1, inp -> "Stop reporting " + printableAt(inp, 0)), //
    report_metrics(1, inp -> "Report metrics " + printableAt(inp, 0)), //
    report_spectrum(1, inp -> "Report Spectrum " + printableAt(inp, 0)),//
    ;
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