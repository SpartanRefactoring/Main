package il.org.spartan.plugin.revision;

import il.org.spartan.plugin.*;

/** An {@link Applicator} suitable for the command line.
 * @author Matteo Orru'
 * @param EventListener<event>
 * @since 2016 */
public class CommandLineApplicator extends Applicator {
  private static final int PASSES_FEW = 1;

  /* (non-Javadoc)
   * 
   * @see il.org.spartan.plugin.revision.Applicator#go() */
  @Override public void go() {
    // if(selection() == null || listener() == null || passes() <= 0 ||
    // selection().isEmpty())
    // return;
    // runContext().accept(() -> {
    // final int l = passes();
    // for (int pass = 0; pass < l; ++pass){
    // final List<? extends WrappedCompilationUnit> alive = new LinkedList<>();
    // alive.addAll(selection().getCompilationUnits());
    // final List<WrappedCompilationUnit> dead = new LinkedList<>();
    // for (final WrappedCompilationUnit ¢ : alive) {
    // if(!runAction().apply(¢.build()).booleanValue())
    // dead.add(¢);
    // }
    // }
    // });
  }

  public static CommandLineApplicator defaultApplicator() {
    return new CommandLineApplicator().defaultSettings();
  }

  /** @return this */
  private CommandLineApplicator defaultSettings() {
    return defaultListenerSilent().defaultPassesFew().defaultRunContext().defaultSelection();
  }

  /** @return this */
  private CommandLineApplicator defaultSelection() {
    selection(CommandLineSelection.Util.get());
    return this;
  }

  /** Initialize the selection of this applicator.
   * @param as JD
   * @return this applicator */
  public CommandLineApplicator selection(final AbstractSelection<CommandLineSelection> as) {
    return this;
  }

  /** @return this */
  private CommandLineApplicator defaultRunContext() {
    runContext(r -> r.run());
    return this;
  }

  /** @return this */
  private CommandLineApplicator defaultPassesFew() {
    passes(PASSES_FEW);
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
  public CommandLineApplicator defaultRunAction() {
    // final Trimmer t = new Trimmer();
    final Spartanizer$Applicator s = new Spartanizer$Applicator();
    runAction(u -> Boolean.valueOf(s.apply(u, selection())));
    return this;
  }

  /** Default run action configuration of {@link CommandLineApplicator}.
   * Spartanize the {@link CompilationUnit} using received
   * {@link Spartanizer$Applicator}.
   * @param a JD
   * @return this applicator */
  public CommandLineApplicator defaultRunAction(final Spartanizer$Applicator a) {
    runAction(u -> Boolean.valueOf(a.apply(u, selection())));
    return this;
  }
}
