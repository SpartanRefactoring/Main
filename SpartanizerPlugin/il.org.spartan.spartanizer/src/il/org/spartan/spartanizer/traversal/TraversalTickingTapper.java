package il.org.spartan.spartanizer.traversal;

/** A {@link TraversalTapper} to update {@link #progressMonitor}
 * @author Yossi Gil
 * @since 2017-04-09 */
public interface TraversalTickingTapper extends TraversalTapper {
  /** @formatter:off */
  @Override default void noTipper() { tick(1); }
  @Override default void setNode() { tick(1); }
  @Override default void tipperAccepts() { tick(1); }
  @Override default void tipperRejects() { tick(1); }
  @Override default void tipperTip() { tick(3); }
  @Override default void tipPrune() { tick(2); }
  @Override default void tipRewrite() { tick(5); }
  void tick(int work);
  //@formatter:on
}