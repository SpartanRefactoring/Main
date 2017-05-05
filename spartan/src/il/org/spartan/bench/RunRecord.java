package il.org.spartan.bench;

import org.jetbrains.annotations.*;

import il.org.spartan.*;
import fluent.ly.*;

public class RunRecord extends AbstractRunRecord {
  public RunRecord(final int runs, @NotNull final Stopwatch grossTime, @NotNull final Stopwatch netTime) {
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
    Log.f("Checking %s entry: runs=%d, netTime=%s (%s), grossTime=%s (%s)", getClass().getSimpleName(), box.it(runs), Unit.formatNanoseconds(netTime),
        Unit.formatRelative(netTime, BenchingPolicy.getBenchingTime()), Unit.formatNanoseconds(grossTime),
        Unit.formatRelative(grossTime, BenchingPolicy.MAX_TIME));
    return runs > BenchingPolicy.MAX_RUNS
        || runs >= BenchingPolicy.MIN_RUNS && (netTime > BenchingPolicy.getBenchingTime() || grossTime > BenchingPolicy.MAX_TIME);
  }
}