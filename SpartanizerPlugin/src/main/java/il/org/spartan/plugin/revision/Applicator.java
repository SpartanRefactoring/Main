package il.org.spartan.plugin.revision;

/** @author Ori Roth
 * @since 2016 */
public abstract class Applicator {
  private Listener listener;
  private Selection selection;
  private boolean shouldRun = true;

  /** Tell this applicator it should not run. */
  public void stop() {
    shouldRun = false;
  }

  /** @return true iff this applicator should run */
  public boolean shouldRun() {
    return shouldRun;
  }

  /** @return selection of the applicator, ready to be configured. */
  public Listener listener() {
    return listener;
  }

  /** @return listener of the applicator, ready to be configured. */
  @SuppressWarnings({ "unchecked", "unused" }) public <L extends Listener> L listener(Class<L> __) {
    return (L) listener;
  }

  /** Initialize the listener of this applicator.
   * @param ¢ JD
   * @return this applicator */
  public Applicator listener(final Listener ¢) {
    this.listener = ¢;
    return this;
  }

  /** @return selection of the applicator, ready to be configured. */
  public Selection selection() {
    return selection;
  }

  /** @return selection of the applicator, ready to be configured. */
  @SuppressWarnings({ "unchecked", "unused" }) public <S extends Selection> S selection(Class<S> __) {
    return (S) selection;
  }

  /** Initialize the selection of this applicator.
   * @param ¢ JD
   * @return this applicator */
  public Applicator selection(final Selection ¢) {
    this.selection = ¢;
    return this;
  }

  /** Main operation of this applicator. */
  public abstract void go();
}
