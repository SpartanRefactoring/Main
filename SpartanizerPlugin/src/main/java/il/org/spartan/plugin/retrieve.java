package il.org.spartan.plugin;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;

/** Fluent API services for plugin
 * @author Yossi Gil
 * @since 2016 */
public enum retrieve {
  ;
  /** @return List of all compilation units in the current project */
  static List<ICompilationUnit> compilationUnits() {
    try {
      return Spartanization.getAllProjectCompilationUnits(currentCompilationUnit(), new NullProgressMonitor());
    } catch (final JavaModelException e) {
      e.printStackTrace();
    }
    return null;
  }

  /** Retrieves the current {@link ICompilationUnit}
   * @return current {@link ICompilationUnit} */
  static ICompilationUnit currentCompilationUnit() {
    return getCompilationUnit(getCurrentWorkbenchWindow().getActivePage().getActiveEditor());
  }

  /** Retrieves the current {@link IWorkbenchWindow}
   * @return current {@link IWorkbenchWindow} */
  static IWorkbenchWindow getCurrentWorkbenchWindow() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
  }

  static Object getResource(final IEditorPart ep) {
    return ep.getEditorInput().getAdapter(IResource.class);
  }

  static ICompilationUnit getCompilationUnit(final IEditorPart ep) {
    return ep == null ? null : getCompilationUnit((IResource) getResource(ep));
  }

  static ICompilationUnit getCompilationUnit(final IResource r) {
    return r == null ? null : JavaCore.createCompilationUnitFrom((IFile) r);
  }
}
