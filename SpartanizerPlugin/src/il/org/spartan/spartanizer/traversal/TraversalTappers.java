package il.org.spartan.spartanizer.traversal;

import java.util.*;

public class TraversalTappers implements TraversalTapper {
  public TraversalTappers pop() {
    inner.remove(inner.size() - 1);
    return this;
  }
  public TraversalTappers push(final TraversalTapper ¢) {
    inner.add(¢);
    return this;
  }
  /** @formatter:off */
  @Override public void noTipper() { inner.forEach(TraversalTapper::noTipper); }
  @Override public void begin() { inner.forEach(TraversalTapper::begin); }
  @Override public void end() { inner.forEach(TraversalTapper::end); }
  @Override public void tipperAccepts() { inner.forEach(TraversalTapper::tipperAccepts); }
  @Override public void tipperRejects() { inner.forEach(TraversalTapper::tipperRejects); }
  @Override public void tipperTip() { inner.forEach(TraversalTapper::tipperTip); }
  @Override public void tipPrune() { inner.forEach(TraversalTapper::tipPrune); }
  @Override public void tipRewrite() { inner.forEach(TraversalTapper::tipRewrite); }
  //@formatter:on
  private final List<TraversalTapper> inner = new LinkedList<>();
}