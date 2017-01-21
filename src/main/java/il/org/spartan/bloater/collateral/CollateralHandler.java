package il.org.spartan.bloater.collateral;

import org.eclipse.core.commands.*;

import il.org.spartan.bloater.*;
import il.org.spartan.plugin.*;

/** Handler for the Bloater project's feature (global Bloater). Uses
 * {@link AthensApplicator} as an {@link Applicator} and {@link Augmenter} as an
 * {@link Application}.
 * @author Ori Roth
 * @since Nov 25, 2016 */
class CollateralHandler extends AbstractHandler {
  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    applicator().defaultSelection().go();
    return null;
  }

  private static AthensApplicator applicator() {
    final AthensApplicator $ = AthensApplicator.defaultApplicator();
    $.setRunAction(¢ -> new Augmenter().commitChanges(¢, $.selection()));
    $.defaultRunContext();
    return $;
  }
}
