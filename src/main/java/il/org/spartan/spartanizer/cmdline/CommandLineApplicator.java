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

//  private final CommandLine$Applicator a = new CommandLine$Applicator();

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
    // listener(EventListener.simpleListener(event.class,
    // e -> {
    // // empty
    // },
    // (e, o) -> {
    // // empty
    // }));
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
  /**
   * Applies {@link Spartanizer$Applicator} by default. 
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
   * Spartanize the {@link CompilationUnit} using received
   * TODO maybe this method are going to die (as well as Spartanize$Applicator)
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
//    selection(CommandLineSelection.Util.get());
    return this;
  }

  /** Default run action configuration of {@link CommandLineApplicator}.
   * @param ¢ JD
   * @return this applicator */
//  public CommandLineApplicator defaultRunAction(final AbstractGUIApplicator ¢) {
//    setRunAction(¢¢ -> Integer.valueOf(¢.apply(¢¢, selection())));
//    name(¢.getName());
//    return this;
//  }

  /** @param ¢ JD
   * @return */
  @Override public CommandLineApplicator defaultSelection(@SuppressWarnings("rawtypes") final AbstractSelection ¢) {
    selection(¢);
    return this;
  }

  /** @return this */
  private CommandLineApplicator defaultSettings() {
    return defaultListenerSilent().defaultPassesFew()
        .defaultRunContext().defaultSelection().defaultRunAction();
  }

  /* @see il.org.spartan.plugin.revision.Applicator#go() */
  @Override public void go() {
    if (selection() == null && listener() == null && passes() <= 0 && selection().isEmpty())
      return;
    listener().push(message.run_start.get(selection().name));
    if (!shouldRun())
      return;
    final AtomicInteger totalTipsInvoked = new AtomicInteger(0);
    runContext().accept(() -> {
//      System.out.println("inside go");
      final int l = passes();
//      System.out.println("passes: " + l);
      for (int pass = 1; pass <= l; ++pass) {
//        System.out.println("pass: " + l);
        listener().push(message.run_pass.get(Integer.valueOf(pass)));
        if (!shouldRun())
          break;
        final List<WrappedCompilationUnit> selected = selection().inner;
        final List<WrappedCompilationUnit> alive = new ArrayList<>(selected);
        final List<WrappedCompilationUnit> dead = new ArrayList<>();
//        System.out.println("shouldRun: " + shouldRun());
//        int i = 0;
        for (final WrappedCompilationUnit ¢ : alive) {
//          System.out.println("i: " + i++);
//          System.out.println(alive.size());
          // System.out.println(¢);
          // System.out.println(runAction());
          final int tipsInvoked = runAction().apply(¢).intValue();
          System.out.println("tipsInvoked: " + tipsInvoked);
          if (tipsInvoked <= 0)
            dead.add(¢);
          ¢.dispose(); // nullify the CompilationUnit associated to an ICompilationUnit
          listener().tick(message.visit_cu.get(Integer.valueOf(alive.indexOf(¢)), 
              Integer.valueOf(alive.size()), "unknown"));
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
    // for (final CompilationUnit ¢ : ((CommandLineSelection)
    // selection()).getCompilationUnits()) {
    // assert ¢ != null;
    // a.go(¢);
    // }
  }

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