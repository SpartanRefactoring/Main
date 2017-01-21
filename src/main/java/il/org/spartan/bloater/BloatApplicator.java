package il.org.spartan.bloater;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** Applicator for the Athens project.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class BloatApplicator extends GUIBatchLaconizer {
  public static BloatApplicator defaultApplicator() {
    return (BloatApplicator) new BloatApplicator().defaultListenerSilent().defaultRunContext().passes(1);
  }
  private boolean accurateContains(final String wrap, final String inner) {
    final String off = off(wrap), $ = trivia.accurateEssence(inner), essence2 = trivia.accurateEssence(off);
    assert essence2 != null;
    return essence2.contains($);
  }
  private String off(String wrap) {
    // TODO Auto-generated method stub
    return null;
  }}
