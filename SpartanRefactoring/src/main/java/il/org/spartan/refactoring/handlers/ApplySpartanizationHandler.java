package il.org.spartan.refactoring.handlers;

import static il.org.spartan.idiomatic.*;
import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;
import il.org.spartan.utils.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;

/**
 * A handler for {@link Spartanizations}. This handler executes all safe
 * spartanizations on all Java files in the current project, while exposing
 * static methods to spartanize only specific compilation units.
 *
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @since 2015/08/01
 */
public class ApplySpartanizationHandler extends BaseHandler {
  /**
   * Sets the maximum number of repetitions made when applying a spartanization
   */
  public static final int max_spartanization_repetitions = 16;

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    execute(retrieve.currentCompilationUnit(), getSelectedText());
    return null;
  }
  /**
   * @param cu JD
   */
  public static void execute(final ICompilationUnit cu) {
    execute(cu, new Range(0, 0));
  }
  /**
   * @param cu JD
   * @param r JD
   */
  public static void execute(final ICompilationUnit cu, final Range r) {
    execute(cu, r == null || r.size() <= 0 ? new TextSelection(0, 0) : new TextSelection(r.from, r.size()));
  }
  /**
   * @param cu JD
   * @param t JD
   */
  public static void execute(final ICompilationUnit cu, final ITextSelection t) {
    final starting s = new starting();
    try {
      starting.from(cu).with(
          take(t).when(t.getLength() > 0 && !t.isEmpty()
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
