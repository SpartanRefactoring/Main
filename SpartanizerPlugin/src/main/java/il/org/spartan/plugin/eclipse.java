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

import il.org.spartan.*;
import il.org.spartan.spartanizer.wring.*;
import il.org.spartan.utils.*;

/** Fluent API services for plugin
 * @author Yossi Gil
 * @since 2016 */
public interface eclipse {
  static final Spartanization[] safeSpartanizations = { //
      new Trimmer() };
  static final String NAME = "Spartanization";
  static final String ICON_PATH = "/src/main/icons/spartan-warrior64.gif";
  ImageIcon icon = new ImageIcon(new eclipse() {
  }.getClass().getResource(ICON_PATH));

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

  /** @param u A compilation unit for reference - you give me an arbitrary
   *        compilation unit from the project and I'll find the root of the
   *        project and do my magic.
   * @param pm A standard {@link IProgressMonitor} - if you don't care about
   *        operation times put a "new NullProgressMonitor()"
   * @return List of all compilation units in the current project
   * @throws JavaModelException don't forget to catch */
  public static List<ICompilationUnit> compilationUnits(final ICompilationUnit u, final IProgressMonitor pm) throws JavaModelException {
    pm.beginTask("Gathering project information...", 1);
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
    for (final IPackageFragmentRoot r : packageFragmentRoots)
      if (r.getKind() == IPackageFragmentRoot.K_SOURCE)
        for (final IJavaElement e : r.getChildren())
          if (e.getElementType() == IJavaElement.PACKAGE_FRAGMENT)
            $.addAll(as.list(((IPackageFragment) e).getCompilationUnits()));
    pm.done();
    return $;
  }

  /** @param message What to announce
   * @return <code><b>null</b></code> */
  static Void announce(final Object message) {
    JOptionPane.showMessageDialog(null, message, NAME, JOptionPane.INFORMATION_MESSAGE, icon);
    return null;
  }

  static ICompilationUnit compilationUnit(final IEditorPart ep) {
    return ep == null ? null : compilationUnit((IResource) resources(ep));
  }

  static ICompilationUnit compilationUnit(final IResource r) {
    return r == null ? null : JavaCore.createCompilationUnitFrom((IFile) r);
  }

  /** @return List of all compilation units in the current project */
  static List<ICompilationUnit> compilationUnits() {
    try {
      return compilationUnits(currentCompilationUnit(), new NullProgressMonitor());
    } catch (final JavaModelException e) {
      e.printStackTrace();
    }
    return null;
  }

  static List<ICompilationUnit> compilationUnits(final ICompilationUnit u) {
    try {
      return compilationUnits(u, new NullProgressMonitor());
    } catch (final JavaModelException x) {
      x.printStackTrace();
      return null;
    }
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
    } catch (final CoreException e) {
      return true;
    }
  }

  static IProgressMonitor newSubMonitor(final IProgressMonitor m) {
    return new SubProgressMonitor(m, 1, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL);
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
