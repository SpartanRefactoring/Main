package il.org.spartan.plugin;

import org.eclipse.core.commands.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ltk.ui.refactoring.*;
import org.eclipse.ui.handlers.*;

/** @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>:
 *         original version
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>: major refactoring
 *         2013/07/11
 * @since 2013/07/01 */
public abstract class BaseHandler extends AbstractHandler {
  private final Spartanization inner;

  /** Instantiates this class
   * @param inner JD */
  protected BaseHandler(final Spartanization inner) {
    this.inner = inner;
  }

  @Override public Void execute(final ExecutionEvent e) throws ExecutionException {
    try {
      return execute(HandlerUtil.getCurrentSelection(e));
    } catch (final InterruptedException x) {
      throw new ExecutionException(x.getMessage());
    }
  }

  protected final String getDialogTitle() {
    return inner.getName();
  }

  protected Spartanization getRefactoring() {
    return inner;
  }

  private Void execute(final ISelection s) throws InterruptedException {
    return !(s instanceof ITextSelection) ? null : execute((ITextSelection) s);
  }

  private Void execute(final ITextSelection s) throws InterruptedException {
    return execute(new RefactoringWizardOpenOperation(getWizard(s, eclipse.currentCompilationUnit())));
  }

  private Void execute(final RefactoringWizardOpenOperation wop) throws InterruptedException {
    wop.run(eclipse.currentWorkbenchWindow().getShell(), getDialogTitle());
    return null;
  }

  private RefactoringWizard getWizard(final ITextSelection s, final ICompilationUnit cu) {
    final Spartanization $ = getRefactoring();
    $.setSelection(s);
    $.setCompilationUnit(cu);
    return new Wizard($);
  }
}
