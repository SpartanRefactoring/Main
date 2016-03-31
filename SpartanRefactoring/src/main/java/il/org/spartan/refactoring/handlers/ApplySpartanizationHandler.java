package il.org.spartan.refactoring.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import il.org.spartan.refactoring.spartanizations.Spartanization;
import il.org.spartan.refactoring.spartanizations.Spartanizations;
import il.org.spartan.refactoring.wring.Trimmer;
import il.org.spartan.utils.Range;

/**
 * A handler for {@link Spartanizations} This handler executes all safe
 * spartanizations on all Java files in the current project,
 * while exposing static methods to spartanize only specific compulation
 * units.
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
      new Trimmer() };
  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent e) {
    applySafeSpartanizationsTo(currentCompilationUnit(), getSelectedText());
    return null;
  }
  public static void applySafeSpartanizationsTo(final ICompilationUnit cu) {
	applySafeSpartanizationsTo(cu, new Range(0, 0));
  }
  public static void applySafeSpartanizationsTo(final ICompilationUnit cu, final Range r) {
	applySafeSpartanizationsTo(cu,
			((r == null || r.size() <= 0) ? new TextSelection(0, 0) : new TextSelection(r.from, r.size())));
  }
  public static void applySafeSpartanizationsTo(final ICompilationUnit cu, final ITextSelection t) {
	  for (final Spartanization s : safeSpartanizations)
	      try {
	        s.setCompilationUnit(cu);
	        s.setSelection(t.getLength() > 0 && !t.isEmpty() ? t : null);
	        s.performRule(cu, new NullProgressMonitor());
	      } catch (final CoreException x) {
	        x.printStackTrace();
	      }
	  }
  private static ITextSelection getSelectedText() {
	IEditorPart ep = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	ISelection s = ep.getEditorSite().getSelectionProvider().getSelection();
	return !(s instanceof ITextSelection) ? null : (ITextSelection) s;
  }
}
