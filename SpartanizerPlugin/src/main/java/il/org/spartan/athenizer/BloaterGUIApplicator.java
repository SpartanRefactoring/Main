package il.org.spartan.athenizer;

import il.org.spartan.spartanizer.plugin.*;

/** Applicator for the Athens project.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class BloaterGUIApplicator extends NewGUIApplicator {
  public static BloaterGUIApplicator defaultApplicator() {
    return (BloaterGUIApplicator) new BloaterGUIApplicator().defaultListenerSilent().setPasses(1);
  }
}
