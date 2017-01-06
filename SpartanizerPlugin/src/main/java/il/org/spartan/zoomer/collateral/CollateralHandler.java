package il.org.spartan.zoomer.collateral;

import org.eclipse.core.commands.*;

import il.org.spartan.plugin.*;
import il.org.spartan.zoomer.*;

/** Handler for the Athenizer project's feature (global athenizer). Uses
 * {@link AthensApplicator} as an {@link Applicator} and {@link Augmenter} as an
 * {@link Application}.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class CollateralHandler extends AbstractHandler {
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
