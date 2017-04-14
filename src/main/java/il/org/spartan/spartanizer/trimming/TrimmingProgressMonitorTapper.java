package il.org.spartan.spartanizer.trimming;

/** A {@link TrimmingTapper} to update {@link #progressMonitor}
 * @author Yossi Gil
 * @since 2017-04-09 */
public class TrimmingProgressMonitorTapper implements TrimmingTapper {
  private final Trimmer trimmer;
  TrimmingProgressMonitorTapper(Trimmer trimmer) {
    this.trimmer = trimmer;
  }
  /** @formatter:off */
  @Override public void noTipper() { w(1); }
  @Override public void setNode() { w(1); }
  @Override public void tipperAccepts() { w(1); }
  @Override public void tipperRejects() { w(1); }
  @Override public void tipperTip() { w(3); }
  @Override public void tipPrune() { w(2); }
  @Override public void tipRewrite() { w(5); }
  void w(final int w) { this.trimmer.progressMonitor().worked(w); }
  //@formatter:on
}