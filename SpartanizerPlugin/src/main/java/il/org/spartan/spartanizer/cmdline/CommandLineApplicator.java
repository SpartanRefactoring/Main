package il.org.spartan.spartanizer.cmdline;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.plugin.*;

/** An {@link Applicator} suitable for the command line.
 * @author Matteo Orru'
 * @since 2016 */
public class CommandLineApplicator extends Applicator {
  private static final int PASSES_FEW = 1;
  private static final int PASSES_MANY = 20;

  public static Applicator defaultApplicator() {
    return new CommandLineApplicator().defaultSettings();
  }

  private final CommandLine$Applicator a = new CommandLine$Applicator();

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
  private CommandLineApplicator defaultListenerSilent() {
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
  private CommandLineApplicator defaultPassesFew() {
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
  /** @return this */
  public CommandLineApplicator defaultRunAction() {
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
   * {@link GUIBatchLaconizer$Applicator}.
   * @param a JD
   * @return this applicator */
  public CommandLineApplicator defaultRunAction(@SuppressWarnings("hiding") final Spartanizer$Applicator a) {
    setRunAction(u -> Integer.valueOf(a.apply(u, selection()) ? 1 : 0));
    name(a.getClass().getSimpleName());
    return this;
  }
  /** @return this */
  private CommandLineApplicator defaultRunContext() {
    runContext(r -> r.run());
    return this;
  }
  /** @return this */
  CommandLineApplicator defaultSelection() {
    selection(CommandLineSelection.Util.get());
    return this;
  }
  /** Default run action configuration of {@link CommandLineApplicator}.
   * @param ¢ JD
   * @return this applicator */
  public CommandLineApplicator defaultRunAction(final AbstractGUIApplicator ¢) {
    setRunAction(¢¢ -> Integer.valueOf(¢.apply(¢¢, selection())));
    name(¢.getName());
    return this;
  }
  /** @param ¢ JD
   * @return */
  @Override public CommandLineApplicator defaultSelection(@SuppressWarnings("rawtypes") final AbstractSelection ¢) {
    selection(¢);
    return this;
  }
  /** @return this */
  private Applicator defaultSettings() {
    return defaultListenerSilent().defaultPassesFew().defaultRunContext().defaultSelection().defaultRunAction();
  }
  /* (non-Javadoc)
   *
   * @see il.org.spartan.plugin.revision.Applicator#go() */
  @Override public void go() {
    if (selection() != null && listener() != null && passes() > 0 && !selection().isEmpty())
      for (final CompilationUnit ¢ : ((CommandLineSelection) selection()).getCompilationUnits()) {
        assert ¢ != null;
        a.go(¢);
      }
  }
}
