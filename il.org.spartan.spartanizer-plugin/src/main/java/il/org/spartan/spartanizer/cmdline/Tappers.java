package il.org.spartan.spartanizer.cmdline;

import java.util.*;

/** @formatter:on */
class Tappers implements Tapper {
  /** @formatter:on */
  private final List<Tapper> inner = new LinkedList<>();

/** @formatter:off */
@Override public void beginBatch() { inner.forEach(Tapper::beginBatch); }
@Override public void beginFile() { inner.forEach(Tapper::beginFile); }
@Override public void beginLocation() { inner.forEach(Tapper::beginLocation); }
@Override public void endBatch() { inner.forEach(Tapper::endBatch); }
@Override public void endFile() { inner.forEach(Tapper::endFile); }
@Override public void endLocation() { inner.forEach(Tapper::endLocation); }

  public Tappers pop() {
    inner.remove(inner.size() - 1);
    return this;
  }

  public Tappers push(final Tapper ¢) {
    inner.add(¢);
    return this;
  }
}