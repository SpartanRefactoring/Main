package il.org.spartan.spartanizer.plugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;

import il.org.spartan.athenizer.Application;
import il.org.spartan.athenizer.BloaterGUIApplicator;
import il.org.spartan.athenizer.collateral.Augmenter;

/** Handler for the Renamig thesis of Dor. Uses {@link BloaterGUIApplicator} as
 * an {@link Applicator} and {@link Augmenter} as an {@link Application}.
 * @author Dor Ma'ayan
 * @since Apr 4, 2017 */
public class RenamingHandler extends AbstractHandler {
  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    applicator().go();
    return null;
  }
  public static GUIApplicator applicator() {
    System.out.println("hi im here !!!!");
    return null;
  }
}
