package il.org.spartan.refactoring.plugin;

import java.util.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ltk.ui.refactoring.*;
import org.eclipse.ui.*;
import org.eclipse.ui.handlers.*;

import il.org.spartan.refactoring.spartanizations.*;

/** @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>:
 *         original version
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>: major refactoring
 *         2013/07/11
 * @since 2013/07/01 */
public abstract class BaseHandler extends AbstractHandler {
  /** @return List of all compilation units in the current project */
  public static List<ICompilationUnit> compilationUnits() {
    try {
      return Spartanization.getAllProjectCompilationUnits(currentCompilationUnit(), new NullProgressMonitor());
    } catch (final JavaModelException e) {
      e.printStackTrace();
    }
    return null;
  }

  /** Retrieves the current {@link ICompilationUnit}
   * @return current {@link ICompilationUnit} */
  public static ICompilationUnit currentCompilationUnit() {
    return getCompilationUnit(getCurrentWorkbenchWindow().getActivePage().getActiveEditor());
  }

  /** Retrieves the current {@link IWorkbenchWindow}
   * @return current {@link IWorkbenchWindow} */
  public static IWorkbenchWindow getCurrentWorkbenchWindow() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
  }

  static Object getResource(final IEditorPart ep) {
    return ep.getEditorInput().getAdapter(IResource.class);
  }

  private static ICompilationUnit getCompilationUnit(final IEditorPart ep) {
    return ep == null ? null : getCompilationUnit((IResource) getResource(ep));
  }

  private static ICompilationUnit getCompilationUnit(final IResource r) {
    return r == null ? null : JavaCore.createCompilationUnitFrom((IFile) r);
  }

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
    return execute(new RefactoringWizardOpenOperation(getWizard(s, currentCompilationUnit())));
  }

  private Void execute(final RefactoringWizardOpenOperation wop) throws InterruptedException {
    wop.run(getCurrentWorkbenchWindow().getShell(), getDialogTitle());
    return null;
  }

  private RefactoringWizard getWizard(final ITextSelection s, final ICompilationUnit cu) {
    final Spartanization $ = getRefactoring();
    $.setSelection(s);
    $.setCompilationUnit(cu);
    return new Wizard($);
  }
}
