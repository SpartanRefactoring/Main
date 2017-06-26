package il.org.spartan.spartanizer.plugin;

import java.util.function.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.cmdline.applicators.*;

/** Configurable applicator.
 * @author Ori Roth
 * @since 2.6 */
public abstract class Applicator {
  /** Generic listener. */
  private Listener listener;
  /** The selection covered by this applicator. */
  private AbstractSelection<?> selection;
  /** The context in which the application runs. The bulk of the application
   * will run in this context, thus supporting tracking and monitoring. */
  private Consumer<Runnable> runContext = Runnable::run;
  /** The modification process for each {@link ICU} in {@link Selection}. May
   * activate, for instance, a {@link GUITraversal}. The return value determines
   * whether the compilation unit should continue to the next pass or not. */
  private Function<WrappedCompilationUnit, Integer> runAction = λ -> Integer.valueOf(0);
  /** How many passes this applicator conducts. May vary according to
   * {@link Applicator#selection}. */
  private int passes;
  /** Whether or not the applicator should run. May be checked/change multiple
   * times during main application run. */
  private boolean shouldRun = true;
  /** Applicator's name. */
  private String name;
  /** Applicator's operation cCamelCase. */
  private English.Inflection operationName;

  /** Tell this applicator it should not run. */
  public void stop() {
    shouldRun = false;
  }
  /** @return whether this applicator should run. */
  protected boolean shouldRun() {
    return shouldRun;
  }
  /** @return run context for this applicator. */
  protected Consumer<Runnable> runContext() {
    return runContext;
  }
  /** Determines run context for this applicator.
   * @param ¢ JD
   * @return {@code this} applicator */
  public Applicator setContext(final Consumer<Runnable> ¢) {
    runContext = ¢;
    return this;
  }
  /** @return run misc for this applicator */
  protected Function<WrappedCompilationUnit, Integer> runAction() {
    return runAction;
  }
  /** Determines run misc for this applicator.
   * @param ¢ JD
   * @return {@code this} applicator */
  public Applicator setRunAction(final Function<WrappedCompilationUnit, Integer> x) {
    runAction = x;
    return this;
  }
  /** @return number of iterations for this applicator */
  protected int passes() {
    return passes;
  }
  /** Determines number of iterations for this applicator.
   * @param ¢ JD
   * @return {@code this} applicator */
  public Applicator setPasses(final int ¢) {
    passes = ¢;
    return this;
  }
  /** @return selection of the applicator, ready to be configured. */
  protected Listener listener() {
    return listener;
  }
  /** Initialize the listener of this applicator.
   * @param ¢ JD
   * @return {@code this} applicator */
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
   * @return {@code this} applicator */
  public Applicator selection(final Selection ¢) {
    selection = ¢;
    return this;
  }
  /** Initialize the selection of this applicator using AbstractSelection.
   * @param ¢ JD
   * @author Matteo Orru'
   * @return {@code this} applicator */
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
   * @return {@code this} applicator */
  public Applicator name(final String ¢) {
    name = ¢;
    return this;
  }
  /** @return applicator's name */
  English.Inflection operationName() {
    return operationName;
  }
  /** Name this applicator.
   * @param ¢ JD
   * @return {@code this} applicator */
  public Applicator operationName(final English.Inflection ¢) {
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
