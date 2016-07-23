package il.org.spartan.refactoring.utils;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;

/**
 * @author Yossi Gil
 *
 * @since 2016`
 */
public interface retrieve {
  static IPackageFragmentRoot[] roots(final IJavaProject javaProject) {
    try {
      return javaProject.getPackageFragmentRoots();
    } catch (final JavaModelException x) {
      x.printStackTrace();
      return null;
    }
  }
  /**
   * retrieves the {@link ICompilationUnit} of an {@link IResource}
   *
   * @param p
   *          JP
   * @return the compilation unit of the parameter
   */
  static ICompilationUnit compilationUnit(final IEditorPart p) {
    return retrieve.compilationUnit((IResource) p.getEditorInput().getAdapter(IResource.class));
  }
  /**
   * retrieves the {@link ICompilationUnit} of an {@link IEditorPart}
   *
   * @param r
   *          JD
   * @return ICompilationUnit of the parameter or returned value of method
   *         <code><b>null</b.</code>
   */
  static ICompilationUnit compilationUnit(final IResource r) {
    return r == null ? null : JavaCore.createCompilationUnitFrom((IFile) r);
  }
  /**
   * Retrieves the current {@link ICompilationUnit}
   *
   * @return the current {@link ICompilationUnit}
   */
  static ICompilationUnit currentCompilationUnit() {
    return compilationUnit(currentWorkbenchWindow().getActivePage().getActiveEditor());
  }
  /**
   * Retrieves the current {@link IWorkbenchWindow}
   *
   * @return the current {@link IWorkbenchWindow}
   */
  static IWorkbenchWindow currentWorkbenchWindow() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
  }
}
