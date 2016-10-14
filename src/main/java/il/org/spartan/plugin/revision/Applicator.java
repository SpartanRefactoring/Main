package il.org.spartan.plugin.revision;

import java.util.function.*;

import org.eclipse.jdt.core.*;

import il.org.spartan.plugin.*;

/** Configurable applicator.
 * @author Ori Roth
 * @since 2.6 */
public abstract class Applicator<L extends Listener> {
  /** Generic listener. */
  private L listener;
  /** The selection covered by this applicator. */
  private Selection selection;
  /** The context in which the application runs. The bulk of the application
   * will run in this context, thus supporting tracking and monitoring. */
  private Consumer<Runnable> runContext;
  /** The modification process for each {@link ICompilationUnit} in
   * {@link Selection}. May activate, for instance, a {@link GUI$Applicator}.
   * The return value determines whether the compilation unit should continue to
   * the next pass or not. */
  private Function<ICompilationUnit, Boolean> runAction;
  /** How many passes this applicator conducts. May vary according to
   * {@link Applicator#selection}. */
  private int passes;
  /** Whether or not the applicator should run. May be checked/change multiple
   * times during main application run. */
  private boolean shouldRun = true;

  /** Tell this applicator it should not run. */
  public void stop() {
    shouldRun = false;
  }

  /** @return <code><b>true</b></code> <em>iff</em> this applicator should
   *         run. */
  public boolean shouldRun() {
    return shouldRun;
  }

  /** @return run context for this applicator. */
  public Consumer<Runnable> runContext() {
    return runContext;
  }

  /** Determines run context for this applicator.
   * @param ¢ JD
   * @return this applicator */
  public Applicator<L> runContext(final Consumer<Runnable> ¢) {
    runContext = ¢;
    return this;
  }

  /** @return run action for this applicator */
  public Function<ICompilationUnit, Boolean> runAction() {
    return runAction;
  }

  /** Determines run action for this applicator.
   * @param ¢ JD
   * @return this applicator */
  public Applicator<L> runAction(final Function<ICompilationUnit, Boolean> ¢) {
    runAction = ¢;
    return this;
  }

  /** @return number of iterations for this applicator */
  public int passes() {
    return passes;
  }

  /** Determines number of iterations for this applicator.
   * @param ¢ JD
   * @return this applicator */
  public Applicator<L> passes(final int ¢) {
    passes = ¢;
    return this;
  }

  /** @return selection of the applicator, ready to be configured. */
  public L listener() {
    return listener;
  }

  /** Initialize the listener of this applicator.
   * @param ¢ JD
   * @return this applicator */
  public Applicator<L> listener(final L ¢) {
    listener = ¢;
    return this;
  }

  /** @return selection of the applicator, ready to be configured. */
  public Selection selection() {
    return selection;
  }

  /** Initialize the selection of this applicator.
   * @param ¢ JD
   * @return this applicator */
  public Applicator<L> selection(final Selection ¢) {
    selection = ¢;
    return this;
  }

  /** Main operation of this applicator. */
  public abstract void go();
}
