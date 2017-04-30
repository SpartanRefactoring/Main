package il.org.spartan.athenizer.collateral;

import org.eclipse.core.commands.*;

import il.org.spartan.athenizer.*;
import il.org.spartan.spartanizer.plugin.*;

/** Handler for the Bloater project's feature (global Bloater). Uses
 * {@link BloaterGUIApplicator} as an {@link Applicator} and {@link Augmenter}
 * as an {@link Application}.
 * @author Ori Roth
 * @since Nov 25, 2016 */
class CollateralHandler extends AbstractHandler {
  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    applicator().defaultSelection().go();
    return null;
  }

  private static BloaterGUIApplicator applicator() {
    final BloaterGUIApplicator $ = BloaterGUIApplicator.defaultApplicator();
    $.setRunAction(λ -> new Augmenter().commitChanges(λ, $.selection()));
    return $;
  }
}
