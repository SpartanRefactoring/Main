package il.org.spartan.plugin.revision;

import org.eclipse.core.commands.*;

/** Some simple handlers to be used by the GUI.
 * @author Ori Roth
 * @since 2.6 */
public class Handlers extends AbstractHandler {
  private static final String LACONIZE_CURRENT = "il.org.spartan.LaconizeCurrent";
  private static final String LACONIZE_PROJECT = "il.org.spartan.LaconizeAll";

  @Override public Object execute(ExecutionEvent ¢) {
    if (¢ == null || ¢.getCommand() == null)
      return null;
    switch (¢.getCommand().getId()) {
      case LACONIZE_CURRENT:
        SpartanizationHandler.applicator().defaultPassesMany().selection(Selection.Util.getCurrentCompilationUnit().buildAll()).go();
        break;
      case LACONIZE_PROJECT:
        SpartanizationHandler.applicator().defaultPassesMany().selection(Selection.Util.getAllCompilationUnits().buildAll()).go();
        break;
      default:
        break;
    }
    return null;
  }
}
