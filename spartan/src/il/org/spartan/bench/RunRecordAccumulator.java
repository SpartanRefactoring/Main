package il.org.spartan.bench;

public class RunRecordAccumulator extends AbstractRunRecord {
  public void add(final RunRecord ¢) {
    runs += ¢.runs;
    netTime += ¢.netTime;
    grossTime += ¢.grossTime;
  }
}