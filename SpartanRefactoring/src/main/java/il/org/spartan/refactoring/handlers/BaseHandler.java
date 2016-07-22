package il.org.spartan.refactoring.handlers;

import il.org.spartan.refactoring.utils.*;

import org.eclipse.core.commands.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ltk.ui.refactoring.*;
import org.eclipse.ui.handlers.*;

/**
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>:
 *         original version
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>: major refactoring
 *         2013/07/11
 * @since 2013/07/01
 */
public abstract class BaseHandler extends AbstractHandler {
  @Override public Void execute(final ExecutionEvent e) throws ExecutionException {
    try {
      return execute(HandlerUtil.getCurrentSelection(e));
    } catch (final InterruptedException x) {
      throw new ExecutionException(x.getMessage());
    }
  }
  private Void execute(final ISelection s) throws InterruptedException {
    return !(s instanceof ITextSelection) ? null : execute((ITextSelection) s);
  }
  private Void execute(final ITextSelection s) throws InterruptedException {
    return execute(new RefactoringWizardOpenOperation(getWizard(s, retrieve.currentCompilationUnit())));
  }
  private static Void execute(final RefactoringWizardOpenOperation wop) throws InterruptedException {
    wop.run(retrieve.currentWorkbenchWindow().getShell(), getDialogTitle());
    return null;
  }
  protected final static String getDialogTitle() {
    return "Spartanize XXX";
  }
}
