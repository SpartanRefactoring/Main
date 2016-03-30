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
	ITextSelection sel = (r == null || r.size() <= 0) ? new TextSelection(0, 0) : new TextSelection(r.from, r.size());
	applySafeSpartanizationsTo(cu, sel);
  }
  public static void applySafeSpartanizationsTo(final ICompilationUnit cu, final ITextSelection t) {
	    for (final Spartanization s : safeSpartanizations)
	      try {
	        s.setCompilationUnit(cu);
	        // TODO We might want a real ProgressMonitor for large projects - I
	        // think that since there is a progress monitor for the whole project we
	        // don't really need it for each file.
	        if(t.getLength() > 0 && !t.isEmpty())
	        	s.setSelection(t);
	        s.performRule(cu, new NullProgressMonitor());
	      } catch (final CoreException x) {
	        x.printStackTrace();
	      }
	  }
  private static ITextSelection getSelectedText() {
	IEditorPart ep = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	ISelection s = ep.getEditorSite().getSelectionProvider().getSelection();
	
	if(s instanceof ITextSelection) {
		ITextSelection ts = (ITextSelection)s;
		return ts;
	}

	return null;  
  }
}
