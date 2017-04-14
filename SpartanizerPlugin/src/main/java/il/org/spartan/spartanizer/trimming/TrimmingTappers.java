package il.org.spartan.spartanizer.trimming;

import java.util.*;

public class TrimmingTappers implements TrimmingTapper {
  @Override public void noTipper() {
    inner.forEach(TrimmingTapper::noTipper);
  }

  /** @formatter:off */
  public TrimmingTappers pop() { inner.remove(inner.size()-1); return this; }
  public TrimmingTappers push(final TrimmingTapper ¢) { inner.add(¢); return this; }
  @Override public void setNode() { inner.forEach(TrimmingTapper::setNode); }
  @Override public void tipperAccepts() { inner.forEach(TrimmingTapper::tipperAccepts); }
  @Override public void tipperRejects() { inner.forEach(TrimmingTapper::tipperRejects); }
  @Override public void tipperTip() { inner.forEach(TrimmingTapper::tipperTip); }
  @Override public void tipPrune() { inner.forEach(TrimmingTapper::tipPrune); }
  @Override public void tipRewrite() { inner.forEach(TrimmingTapper::tipRewrite); }
  private final List<TrimmingTapper> inner = new LinkedList<>();
  //@formatter:on
}