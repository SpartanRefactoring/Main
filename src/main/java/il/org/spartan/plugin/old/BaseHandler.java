package il.org.spartan.plugin.old;

import org.eclipse.core.commands.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ltk.ui.refactoring.*;
import org.eclipse.ui.handlers.*;

import il.org.spartan.plugin.*;

/** @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>:
 *         original version
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>: major refactoring
 *         2013/07/11
 * @since 2013/07/01 */
abstract class BaseHandler extends AbstractHandler {
  private final AbstractGUIApplicator inner;

  /** Instantiates this class */
  BaseHandler() {
    this(null);
  }

  /** Instantiates this class
   * @param inner JD */
  private BaseHandler(final AbstractGUIApplicator inner) {
    this.inner = inner;
  }

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

  private AbstractGUIApplicator getRefactoring() {
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

  private RefactoringWizard getWizard(final ITextSelection s, final ICompilationUnit u) {
    final AbstractGUIApplicator $ = getRefactoring();
    $.setSelection(s);
    $.setICompilationUnit(u);
    return new Wizard($);
  }
}
