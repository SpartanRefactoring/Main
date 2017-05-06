package il.org.spartan.bench;

import org.eclipse.jdt.annotation.*;

public class RunRecordAccumulator extends AbstractRunRecord {
  public void add( final RunRecord ¢) {
    runs += ¢.runs;
    netTime += ¢.netTime;
    grossTime += ¢.grossTime;
  }
}