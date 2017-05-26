package il.org.spartan.spartanizer.cmdline.applicator;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;

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
   * @return {@code this} instance */
  @Override public CommandLineApplicator defaultListenerNoisy() {
    listener(os -> {
      as.list(os).forEach(λ -> System.out.print(λ + " "));
      System.out.println();
    });
    return this;
  }
  private CommandLineApplicator defaultListenerSilent() {
    listener((final Object... __) -> {/**/});
    return this;
  }
  private CommandLineApplicator defaultPassesFew() {
    setPasses(PASSES_FEW);
    return this;
  }
  /** Default passes configuration, with many passes.
   * @return {@code this} instance */
  public CommandLineApplicator defaultPassesMany() {
    setPasses(PASSES_MANY);
    return this;
  }
  // TODO Matteo: I have changed the "run misc" to return number of tips
  // committed instead of whether tips were committed (Boolean -> Integer).
  // Added a quick fix to your code. Also I do not understand why you wrote this
  // - we will inspect it once we meet. --or
  /** Applies {@link Spartanizer$Applicator} by default.
   * @return {@code this} */
  private CommandLineApplicator defaultRunAction() {
    System.out.println("defaultRunAction");
    setRunAction(λ -> Integer.valueOf(as.bit(new Spartanizer$Applicator().apply(λ, selection()))));
    return this;
  }
  /** Default run misc configuration of . Spartanize the {@link CompilationUnit}
   * using received TODO maybe this method are going to die (as well as
   * Spartanize$Applicator) {@link Spartanizer$Applicator}.
   * @param a JD
   * @return {@code this} instance */
  public CommandLineApplicator defaultRunAction(final Spartanizer$Applicator a) {
    setRunAction(λ -> Integer.valueOf(as.bit(a.apply(λ, selection()))));
    name(a.getClass().getSimpleName());
    return this;
  }
  /** Default run misc configuration of . Spartanize the {@link CompilationUnit}
   * using received {@link CommandLine$Applicator}.
   * @param a JD
   * @return {@code this} instance */
  @Override public CommandLineApplicator defaultRunAction(final CommandLine$Applicator a) {
    CommandLine$Applicator.startingTime = new Date().getTime();
    setRunAction(λ -> Integer.valueOf(as.bit(a.apply(λ, selection()))));
    name(a.getClass().getSimpleName());
    return this;
  }
  private CommandLineApplicator defaultRunContext() {
    setContext(Runnable::run);
    return this;
  }
  private CommandLineApplicator defaultSelection() {
    // selection(CommandLineSelection.Util.get()); // temporarily disabled
    return this;
  }
  /** @param ¢ JD
   * @return */
  @Override public CommandLineApplicator defaultSelection(@SuppressWarnings("rawtypes") final AbstractSelection ¢) {
    selection(¢);
    return this;
  }
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
    final Int totalTipsInvoked = new Int();
    runContext().accept(() -> {
      for (int pass = 1; pass <= passes(); ++pass) {
        listener().push(message.run_pass.get(Integer.valueOf(pass)));
        if (!shouldRun())
          break;
        final List<WrappedCompilationUnit> selected = selection().inner, alive = as.list(selected), dead = an.empty.list();
        for (final WrappedCompilationUnit ¢ : alive) {
          final int tipsInvoked = runAction().apply(¢).intValue();
          if (tipsInvoked <= 0)
            dead.add(¢);
          ¢.dispose(); // nullify the CompilationUnit associated to an
                       // ICompilationUnit
          listener().tick(message.visit_cu.get(Integer.valueOf(alive.indexOf(¢)), Integer.valueOf(alive.size()), "unknown"));
          totalTipsInvoked.add(tipsInvoked);
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
    run_start(1, λ -> "Spartanizing " + printableAt(λ, 0)), //
    run_pass(1, λ -> "Pass #" + printableAt(λ, 0)), //
    run_pass_finish(1, λ -> "Pass #" + printableAt(λ, 0) + " finished"), //
    visit_cu(3, λ -> the.nth(printableAt(λ, 0), printableAt(λ, 1)) + "\tSpartanizing " + printableAt(λ, 2)), //
    run_finish(2, λ -> "Done spartanizing " + printableAt(λ, 0) + "\nTips accepted: " + printableAt(λ, 1)),
    // report
    report_start(1, λ -> "Start reporting " + printableAt(λ, 0)), //
    report_stop(1, λ -> "Stop reporting " + printableAt(λ, 0)), //
    report_metrics(1, λ -> "Report metrics " + printableAt(λ, 0)), //
    report_spectrum(1, λ -> "Report Spectrum " + printableAt(λ, 0)),//
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
      return English.unknownIfNull(os, λ -> λ[index]);
    }
  }
}