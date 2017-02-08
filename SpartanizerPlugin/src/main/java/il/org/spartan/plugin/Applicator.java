package il.org.spartan.plugin;

import java.util.function.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.nominal.*;

/** Configurable applicator.
 * @param <L> I think we do not need this one. It couples classes too much.
 * @author Ori Roth
 * @since 2.6 */
public abstract class Applicator {
  /** Generic listener. */
  private Listener listener;
  /** The selection covered by this applicator. */
  private AbstractSelection<?> selection;
  /** The context in which the application runs. The bulk of the application
   * will run in this context, thus supporting tracking and monitoring. */
  private Consumer<Runnable> runContext;
  /** The modification process for each {@link ICU} in {@link Selection}. May
   * activate, for instance, a {@link AbstractGUIApplicator}. The return value
   * determines whether the compilation unit should continue to the next pass or
   * not. */
  private Function<WrappedCompilationUnit, Integer> runAction;
  /** How many passes this applicator conducts. May vary according to
   * {@link Applicator#selection}. */
  private int passes;
  /** Whether or not the applicator should run. May be checked/change multiple
   * times during main application run. */
  private boolean shouldRun = true;
  /** Applicator's name. */
  private String name;
  /** Applicator's operation namer. */
  private Linguistic.Activity operationName;

  /** Tell this applicator it should not run. */
  public void stop() {
    shouldRun = false;
  }

  /** @return <code><b>true</b></code> <em>iff</em> this applicator should
   *         run. */
  protected boolean shouldRun() {
    return shouldRun;
  }

  /** @return run context for this applicator. */
  protected Consumer<Runnable> runContext() {
    return runContext;
  }

  /** Determines run context for this applicator.
   * @param ¢ JD
   * @return <code><b>this</b></code> applicator */
  public Applicator runContext(final Consumer<Runnable> ¢) {
    runContext = ¢;
    return this;
  }

  /** @return run action for this applicator */
  protected Function<WrappedCompilationUnit, Integer> runAction() {
    return runAction;
  }

  /** Determines run action for this applicator.
   * @param ¢ JD
   * @return <code><b>this</b></code> applicator */
  public Applicator setRunAction(final Function<WrappedCompilationUnit, Integer> ¢) {
    runAction = ¢;
    return this;
  }

  /** @return number of iterations for this applicator */
  protected int passes() {
    return passes;
  }

  /** Determines number of iterations for this applicator.
   * @param ¢ JD
   * @return <code><b>this</b></code> applicator */
  public Applicator passes(final int ¢) {
    passes = ¢;
    return this;
  }

  /** @return selection of the applicator, ready to be configured. */
  protected Listener listener() {
    return listener;
  }

  /** Initialize the listener of this applicator.
   * @param ¢ JD
   * @return <code><b>this</b></code> applicator */
  public Applicator listener(final Listener ¢) {
    listener = ¢;
    return this;
  }

  /** @return selection of the applicator, ready to be configured. */
  public AbstractSelection<?> selection() {
    return selection;
  }

  /** Initialize the selection of this applicator.
   * @param ¢ JD
   * @return <code><b>this</b></code> applicator */
  public Applicator selection(final Selection ¢) {
    selection = ¢;
    return this;
  }

  /** Initialize the selection of this applicator using AbstractSelection.
   * @param ¢ JD
   * @author Matteo Orru'
   * @return <code><b>this</b></code> applicator */
  protected Applicator selection(final AbstractSelection<?> ¢) {
    selection = ¢;
    return this;
  }

  /** @return applicator's name */
  public String name() {
    return name;
  }

  /** Name this applicator.
   * @param ¢ JD
   * @return <code><b>this</b></code> applicator */
  public Applicator name(final String ¢) {
    name = ¢;
    return this;
  }

  /** @return applicator's name */
  Linguistic.Activity operationName() {
    return operationName;
  }

  /** Name this applicator.
   * @param ¢ JD
   * @return <code><b>this</b></code> applicator */
  public Applicator operationName(final Linguistic.Activity ¢) {
    operationName = ¢;
    return this;
  }

  /** Main operation of this applicator. */
  public abstract void go();

  public Applicator defaultListenerNoisy() {
    return this;
  }

  public Applicator defaultSelection(@SuppressWarnings("unused") final AbstractSelection<?> of) {
    return this;
  }

  @SuppressWarnings("unused") public Applicator defaultRunAction(final CommandLine$Applicator __) {
    return this;
  }
}
