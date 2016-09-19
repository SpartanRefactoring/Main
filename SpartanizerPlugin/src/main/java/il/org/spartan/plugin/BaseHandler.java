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
  private final GUI$Applicator inner;

  /** Instantiates this class
   * @param inner JD */
  protected BaseHandler(final GUI$Applicator inner) {
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

  protected GUI$Applicator getRefactoring() {
    return inner;
  }

  private Void execute(final ISelection ¢) throws InterruptedException {
    return !(¢ instanceof ITextSelection) ? null : execute((ITextSelection) ¢);
  }

  private Void execute(final ITextSelection ¢) throws InterruptedException {
    return execute(new RefactoringWizardOpenOperation(getWizard(¢, eclipse.currentCompilationUnit())));
  }

  private Void execute(final RefactoringWizardOpenOperation wop) throws InterruptedException {
    wop.run(eclipse.currentWorkbenchWindow().getShell(), getDialogTitle());
    return null;
  }

  private RefactoringWizard getWizard(final ITextSelection s, final ICompilationUnit cu) {
    final GUI$Applicator $ = getRefactoring();
    $.setSelection(s);
    $.setCompilationUnit(cu);
    return new Wizard($);
  }
}
