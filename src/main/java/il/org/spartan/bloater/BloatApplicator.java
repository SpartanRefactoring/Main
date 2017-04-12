package il.org.spartan.bloater;

import il.org.spartan.spartanizer.plugin.*;

/** Applicator for the Athens project.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class BloatApplicator extends BatchApplicator {
  public static BloatApplicator defaultApplicator() {
    return (BloatApplicator) new BloatApplicator().defaultListenerSilent().defaultRunContext().passes(1);
  }
}
