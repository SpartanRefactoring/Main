package il.org.spartan.plugin;

import java.util.*;

import javax.swing.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;

import static il.org.spartan.spartanizer.ast.wizard.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;

/** Fluent API services for the plugin
 * @author Yossi Gil
 * @since 2016 */
public interface eclipse {
  final GUI$Applicator[] safeSpartanizations = { new Trimmer() };
  final String NAME = "Spartanization";
  final String ICON_PATH = "/src/main/icons/spartan-warrior64.gif";
  final ImageIcon icon = new ImageIcon(new eclipse() {
    // This anonymous class is intentionally empty
  }.getClass().getResource(ICON_PATH));

  /** @param message What to announce
   * @return <code><b>null</b></code> */
  static Void announce(final Object message) {
    JOptionPane.showMessageDialog(null, message, NAME, JOptionPane.INFORMATION_MESSAGE, icon);
    return null;
  }

  static ICompilationUnit compilationUnit(final IEditorPart ep) {
    return ep == null ? null : compilationUnit((IResource) resources(ep));
  }

  static ICompilationUnit compilationUnit(final IResource ¢) {
    return ¢ == null ? null : JavaCore.createCompilationUnitFrom((IFile) ¢);
  }

  /** @return List of all compilation units in the current project */
  static List<ICompilationUnit> compilationUnits() {
    try {
      return compilationUnits(currentCompilationUnit(), nullProgressMonitor);
    } catch (final JavaModelException e) {
      Plugin.log(e);
    }
    return null;
  }

  static List<ICompilationUnit> compilationUnits(final ICompilationUnit u) {
    try {
      return compilationUnits(u, nullProgressMonitor);
    } catch (final JavaModelException x) {
      Plugin.log(x);
      return null;
    }
  }

  /** @param u A compilation unit for reference - you give me an arbitrary
   *        compilation unit from the project and I'll find the root of the
   *        project and do my magic.
   * @param pm A standard {@link IProgressMonitor} - if you don't care about
   *        operation times use {@link wizard@nullProgressMonitor{
   * @return List of all compilation units in the current project
   * @throws JavaModelException don't forget to catch */
  static List<ICompilationUnit> compilationUnits(final ICompilationUnit u, final IProgressMonitor pm) throws JavaModelException {
    pm.beginTask("Collection compilation units ", IProgressMonitor.UNKNOWN);
    final List<ICompilationUnit> $ = new ArrayList<>();
    if (u == null) {
      announce("Cannot find current compilation unit " + u);
      return $;
    }
    final IJavaProject javaProject = u.getJavaProject();
    if (javaProject == null) {
      announce("Cannot find project of " + u);
      return $;
    }
    final IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
    if (packageFragmentRoots == null) {
      announce("Cannot find roots of " + javaProject);
      return $;
    }
    for (final IPackageFragmentRoot r : packageFragmentRoots) {
      pm.worked(1);
      if (r.getKind() == IPackageFragmentRoot.K_SOURCE)
        pm.worked(1);
      for (final IJavaElement ¢ : r.getChildren()) {
        pm.worked(1);
        if (¢.getElementType() == IJavaElement.PACKAGE_FRAGMENT)
          $.addAll(as.list(((IPackageFragment) ¢).getCompilationUnits()));
      }
    }
    pm.done();
    return $;
  }

  /** Retrieves the current {@link ICompilationUnit}
   * @return current {@link ICompilationUnit} */
  static ICompilationUnit currentCompilationUnit() {
    return compilationUnit(currentWorkbenchWindow().getActivePage().getActiveEditor());
  }

  /** Retrieves the current {@link IWorkbenchWindow}
   * @return current {@link IWorkbenchWindow} */
  static IWorkbenchWindow currentWorkbenchWindow() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
  }

  static boolean isNodeOutsideMarker(final ASTNode n, final IMarker m) {
    try {
      return n.getStartPosition() < ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue()
          || n.getLength() + n.getStartPosition() > ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue();
    } catch (final CoreException x) {
      Plugin.log(x);
      return true;
    }
  }

  static IProgressMonitor newSubMonitor(final IProgressMonitor ¢) {
    return new SubProgressMonitor(¢, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL);
  }

  static Object resources(final IEditorPart ep) {
    return ep.getEditorInput().getAdapter(IResource.class);
  }

  static ITextSelection selectedText() {
    final IEditorPart ep = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    final ISelection s = ep.getEditorSite().getSelectionProvider().getSelection();
    return !(s instanceof ITextSelection) ? null : (ITextSelection) s;
  }
}
