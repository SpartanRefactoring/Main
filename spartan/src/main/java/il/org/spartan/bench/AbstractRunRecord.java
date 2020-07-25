package il.org.spartan.bench;

import fluent.ly.box;

public class AbstractRunRecord {
  protected int runs;
  protected long grossTime;
  protected long netTime;

  public long grossTime() {
    return grossTime;
  }

  public long netTime() {
    return netTime;
  }

  public int runs() {
    return runs;
  }

  @Override public String toString() {
    return String.format("runs=%d, netTime=%s, grossTime=%s, efficacy=%s", //
        box.it(runs), //
        Unit.formatNanoseconds(netTime), //
        Unit.formatNanoseconds(grossTime), //
        Unit.formatRelative(netTime, grossTime));
  }
}
