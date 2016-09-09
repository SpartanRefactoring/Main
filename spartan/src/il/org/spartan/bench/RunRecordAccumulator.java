/**
 *
 */
package il.org.spartan.bench;

public class RunRecordAccumulator extends AbstractRunRecord {
  public void add(final RunRecord r) {
    runs += r.runs;
    netTime += r.netTime;
    grossTime += r.grossTime;
  }
}