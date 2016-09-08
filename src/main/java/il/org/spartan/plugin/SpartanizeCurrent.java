package il.org.spartan.plugin;

import org.eclipse.core.commands.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;

import il.org.spartan.spartanizer.wring.*;
import il.org.spartan.utils.*;

/** A handler for {@link Spartanizations} This handler executes all safe
 * spartanizations on all Java files in the current project, while exposing
 * static methods to spartanize only specific compilation units.
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @since 2015/08/01 */
public class SpartanizeCurrent extends BaseHandler {
  static final Spartanization[] safeSpartanizations = { //
      new Trimmer() };

  public static void apply(final ICompilationUnit cu) {
    apply(cu, new Range(0, 0));
  }

  public static void apply(final ICompilationUnit cu, final ITextSelection t) {
    for (final Spartanization s : safeSpartanizations)
      try {
        s.setCompilationUnit(cu);
        s.setSelection(t.getLength() > 0 && !t.isEmpty() ? t : null);
        s.performRule(cu, new NullProgressMonitor());
      } catch (final CoreException x) {
        x.printStackTrace();
      }
  }

  public static void apply(final ICompilationUnit cu, final Range r) {
    apply(cu, r == null || r.isEmpty() ? new TextSelection(0, 0) : new TextSelection(r.from, r.size()));
  }

  private static ITextSelection getSelectedText() {
    final IEditorPart ep = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    final ISelection s = ep.getEditorSite().getSelectionProvider().getSelection();
    return !(s instanceof ITextSelection) ? null : (ITextSelection) s;
  }

  /** Instantiates this class */
  public SpartanizeCurrent() {
    super(null);
  }

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    apply(currentCompilationUnit(), getSelectedText());
    return null;
  }
}
