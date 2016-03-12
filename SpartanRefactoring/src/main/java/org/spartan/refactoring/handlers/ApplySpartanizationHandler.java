package org.spartan.refactoring.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.spartan.refactoring.spartanizations.Spartanization;
import org.spartan.refactoring.spartanizations.Spartanizations;
import org.spartan.refactoring.wring.Trimmer;

/**
 * A handler for {@link Spartanizations} This handler executes all safe
 * Spartanizations on all java files in the current project.
 *
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @since 2015/08/01
 */
public class ApplySpartanizationHandler extends BaseHandler {
  /** Instantiates this class */
  public ApplySpartanizationHandler() {
    super(null);
  }
  static final Spartanization[] safeSpartanizations = { //
      new Trimmer(), };
  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent e) {
    applySafeSpartanizationsTo(currentCompilationUnit());
    return null;
  }
  public static void applySafeSpartanizationsTo(final ICompilationUnit cu) {
    for (final Spartanization s : safeSpartanizations)
      try {
        s.setCompilationUnit(cu);
        // TODO We might want a real ProgressMonitor for large projects - I
        // think that since there is a progress monitor for the whole project we
        // don't really need it for each file.
        s.performRule(cu, new NullProgressMonitor());
      } catch (final CoreException x) {
        x.printStackTrace();
      }
  }
}
