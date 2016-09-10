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
    ___.positive(this.netTime);
    ___.positive(this.grossTime);
    ___.nonnegative(runs);
    ___.positive(runs);
  }

  public double estimate() {
    ___.positive(netTime);
    ___.positive(runs);
    return 1.0 * netTime / runs;
  }

  public boolean ok() {
    Log.f("Checking %s entry: runs=%d, netTime=%s (%s), grossTime=%s (%s)", //
        getClass().getSimpleName(), //
        Box.it(runs), //
        Unit.formatNanoseconds(netTime), //
        Unit.formatRelative(netTime, BenchingPolicy.getBenchingTime()), //
        Unit.formatNanoseconds(grossTime), //
        Unit.formatRelative(grossTime, BenchingPolicy.MAX__TIME) //
    );
    if (runs > BenchingPolicy.MAX__RUNS)
      return true;
    if (runs < BenchingPolicy.MIN__RUNS)
      return false;
    return netTime > BenchingPolicy.getBenchingTime() || grossTime > BenchingPolicy.MAX__TIME;
  }
}