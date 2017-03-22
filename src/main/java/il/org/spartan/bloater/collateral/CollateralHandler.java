package il.org.spartan.bloater.collateral;

import org.eclipse.core.commands.*;

import il.org.spartan.bloater.*;
import il.org.spartan.plugin.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Handler for the Bloater project's feature (global Bloater). Uses
 * {@link BloatApplicator} as an {@link Applicator} and {@link Augmenter} as an
 * {@link Application}.
 * @author Ori Roth
 * @since Nov 25, 2016 */
class CollateralHandler extends AbstractHandler {
  @Nullable @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    applicator().defaultSelection().go();
    return null;
  }

  @NotNull private static BloatApplicator applicator() {
    @NotNull final BloatApplicator $ = BloatApplicator.defaultApplicator();
    $.setRunAction(λ -> new Augmenter().commitChanges(λ, $.selection()));
    $.defaultRunContext();
    return $;
  }
}
