package il.org.spartan.spartanizer.athenizer;

import il.org.spartan.plugin.*;

public class AthensApplicator extends GUIBatchLaconizer {
  public static AthensApplicator defaultApplicator() {
    return (AthensApplicator) new AthensApplicator().defaultListenerSilent().defaultRunContext().passes(1);
  }
}
