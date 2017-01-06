package il.org.spartan.zoomer;

import il.org.spartan.plugin.*;

/** Applicator for the Athens project.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class AthensApplicator extends GUIBatchLaconizer {
  public static AthensApplicator defaultApplicator() {
    return (AthensApplicator) new AthensApplicator().defaultListenerSilent().defaultRunContext().passes(1);
  }
}
