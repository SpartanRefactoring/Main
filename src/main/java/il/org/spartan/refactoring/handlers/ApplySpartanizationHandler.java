package il.org.spartan.refactoring.handlers;

import org.eclipse.core.commands.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;

import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.wring.*;
import il.org.spartan.utils.*;

/**
 * A handler for {@link Spartanizations} This handler executes all safe
 * spartanizations on all Java files in the current project, while exposing
 * static methods to spartanize only specific compulation units.
 *
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @since 2015/08/01
 */
public class ApplySpartanizationHandler extends BaseHandler {
  /**
   * Sets the maximum number of repetitions made when applying a spartanization
   */
  public static final int max_spartanization_repetitions = 16;

  /** Instantiates this class */
  public ApplySpartanizationHandler() {
    super(null);
  }

  static final Spartanization[] inner = { new Trimmer() };

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    execute(currentCompilationUnit(), getSelectedText());
    return null;
  }
  @SuppressWarnings("javadoc") public static void execute(final ICompilationUnit cu) {
    execute(cu, new Range(0, 0));
  }
  @SuppressWarnings("javadoc") public static void execute(final ICompilationUnit cu, final Range r) {
    execute(cu, r == null || r.size() <= 0 ? new TextSelection(0, 0) : new TextSelection(r.from, r.size()));
  }
  @SuppressWarnings("javadoc") public static void execute(final ICompilationUnit cu, final ITextSelection t) {
    for (final Spartanization s : inner)
      try {
        s.setCompilationUnit(cu);
        s.setSelection(t.getLength() > 0 && !t.isEmpty() ? t : null);
        for (int i = 0; i < max_spartanization_repetitions; ++i)
          if (!s.performRule(cu, new NullProgressMonitor()))
            break;
      } catch (final CoreException x) {
        x.printStackTrace();
      }
  }
  private static ITextSelection getSelectedText() {
    final IEditorPart ep = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    final ISelection s = ep.getEditorSite().getSelectionProvider().getSelection();
    return (ITextSelection) s;
  }
}
