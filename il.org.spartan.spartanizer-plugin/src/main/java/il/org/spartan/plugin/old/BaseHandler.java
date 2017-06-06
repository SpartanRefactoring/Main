package il.org.spartan.plugin.old;

import org.eclipse.core.commands.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ltk.ui.refactoring.*;
import org.eclipse.ui.handlers.*;

import il.org.spartan.spartanizer.plugin.*;

/** @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>:
 *         original version
 * @author Yossi Gil: major refactoring 2013/07/11
 * @since 2013/07/01 */
abstract class BaseHandler extends AbstractHandler {
  /** [[SuppressWarningsSpartan]] */
  private final GUITraversal inner = new GUITraversal();

  @Override public Void execute(final ExecutionEvent $) throws ExecutionException {
    try {
      return execute(HandlerUtil.getCurrentSelection($));
    } catch (final InterruptedException ¢) {
      throw new ExecutionException(¢.getMessage());
    }
  }
  private String getDialogTitle() {
    return inner.getName();
  }
  private Void execute(final ISelection ¢) throws InterruptedException {
    return !(¢ instanceof ITextSelection) ? null : execute((ITextSelection) ¢);
  }
  private Void execute(final ITextSelection ¢) throws InterruptedException {
    return execute(new RefactoringWizardOpenOperation(new Wizard(inner.setSelection(¢).iCompilationUnit(eclipse.currentCompilationUnit()))));
  }
  private Void execute(final RefactoringWizardOpenOperation wop) throws InterruptedException {
    wop.run(eclipse.currentWorkbenchWindow().getShell(), getDialogTitle());
    return null;
  }
}
