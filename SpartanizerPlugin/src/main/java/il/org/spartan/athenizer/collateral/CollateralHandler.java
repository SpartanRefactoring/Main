package il.org.spartan.athenizer.collateral;

import org.eclipse.core.commands.*;

import il.org.spartan.athenizer.*;
import il.org.spartan.plugin.*;

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
    final Augmenter a = new Augmenter();
    $.setRunAction(¢ -> a.commitChanges(¢, $.selection()));
    $.defaultRunContext();
    return $;
  }
}
