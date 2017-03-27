package il.org.spartan.bloater;

import il.org.spartan.plugin.*;

/** Applicator for the Athens project.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class BloatApplicator extends GUIBatchLaconizer {
  public static BloatApplicator defaultApplicator() {
    return (BloatApplicator) new BloatApplicator().defaultListenerSilent().defaultRunContext().passes(1);
  }
}
