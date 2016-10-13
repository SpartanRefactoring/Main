package il.org.spartan.plugin.revision;

/** @author Ori Roth
 * @since 2016 */
public abstract class Applicator<L extends Listener> {
  private L listener;
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
