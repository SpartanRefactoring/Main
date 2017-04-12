package il.org.spartan.plugin;

import org.eclipse.core.commands.*;

import il.org.spartan.bloater.*;
import il.org.spartan.bloater.collateral.*;

/** Handler for the Renamig thesis of Dor. Uses {@link BloatApplicator} as an
 * {@link Applicator} and {@link Augmenter} as an {@link Application}.
 * @author Dor Ma'ayan
 * @since Apr 4, 2017 */
public class RenamingHandler extends AbstractHandler {
  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    applicator().go();
    return null;
  }

  public static BatchApplicator applicator() {
    System.out.println("hi im here !!!!");
    return null;
  }
}
