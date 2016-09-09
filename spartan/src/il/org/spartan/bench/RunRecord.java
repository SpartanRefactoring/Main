/**
 *
 */
package il.org.spartan.bench;

import il.org.spartan.*;
import il.org.spartan.utils.*;

public class RunRecord extends AbstractRunRecord {
  public RunRecord(final int runs, final StopWatch grossTime, final StopWatch netTime) {
    this.runs = runs;
    this.grossTime = grossTime.time();
    this.netTime = netTime.time();
    ____.positive(this.netTime);
    ____.positive(this.grossTime);
    ____.nonnegative(runs);
    ____.positive(runs);
  }

  public double estimate() {
    ____.positive(netTime);
    ____.positive(runs);
    return 1.0 * netTime / runs;
  }

  public boolean ok() {
    Log.f("Checking %s entry: runs=%d, netTime=%s (%s), grossTime=%s (%s)", //
        getClass().getSimpleName(), //
        Box.it(runs), //
        Unit.formatNanoseconds(netTime), //
        Unit.formatRelative(netTime, BenchingPolicy.getBenchingTime()), //
        Unit.formatNanoseconds(grossTime), //
        Unit.formatRelative(grossTime, BenchingPolicy.MAX_TIME) //
    );
    if (runs > BenchingPolicy.MAX_RUNS)
      return true;
    if (runs < BenchingPolicy.MIN_RUNS)
      return false;
    return netTime > BenchingPolicy.getBenchingTime() || grossTime > BenchingPolicy.MAX_TIME;
  }
}