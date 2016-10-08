package il.org.spartan.plugin;

import static il.org.spartan.Utils.*;

import java.awt.*;
import java.net.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.*;
import org.eclipse.jface.text.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** Fluent API services for the plugin
 * @author Yossi Gil
 * @since 2016 */
public enum eclipse {
  facade;
  static final String NAME = "Laconic";
  private static final String iconAddress = "platform:/plugin/org.eclipse.compare/icons/full/wizban/applypatch_wizban.png";
  private static boolean iconInitialized;
  static ImageIcon icon;
  private static boolean iconNonBusyInitialized;
  static org.eclipse.swt.graphics.Image iconNonBusy;
  static final Shell parent = null;
  static final int shellStyle = SWT.TOOL;
  static final boolean takeFocusOnOpen = false;
  static final boolean persistSize = false;
  static final boolean persistLocation = false;
  static final boolean showDialogMenu = true;

  /** Add nature to one project */
  static void addNature(final IProject p) throws CoreException {
    final IProjectDescription d = p.getDescription();
    final String[] natures = d.getNatureIds();
    if (as.list(natures).contains(Nature.NATURE_ID))
      return; // Already got the nature
    d.setNatureIds(append(natures, Nature.NATURE_ID));
    p.setDescription(d, null);
  }

  static Void announce(final Object message) {
    // JOptionPane.showMessageDialog(null, message, NAME,
    // JOptionPane.INFORMATION_MESSAGE, icon());
    announceNonBusy(message + "");
    return null;
  }

  static MessageDialog announceNonBusy(final String message) {
    return new MessageDialog(null, NAME, iconNonBusy(), message, MessageDialog.INFORMATION, 0) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.ON_TOP | SWT.MODELESS);
      }
    };
  }

  static ProgressMonitorDialog progressMonitorDialog(final boolean openOnRun) {
    final ProgressMonitorDialog $ = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.MODELESS);
      }
    };
    $.setBlockOnOpen(false);
    $.setCancelable(true);
    $.setOpenOnRun(openOnRun);
    return $;
  }

  static ICompilationUnit compilationUnit(final IEditorPart ep) {
    return ep == null ? null : compilationUnit((IResource) resources(ep));
  }

  static ICompilationUnit compilationUnit(final IResource ¢) {
    return ¢ == null ? null : JavaCore.createCompilationUnitFrom((IFile) ¢);
  }

  /** @param u A compilation unit for reference - you give me an arbitrary
   *        compilation unit from the project and I'll find the root of the
   *        project and do my magic.
   * @param pm A standard {@link IProgressMonitor} - if you don't care about
   *        operation times use {@link wizard@nullProgressMonitor}
   * @return List of all compilation units in the current project
   * @throws JavaModelException don't forget to catch */
  static List<ICompilationUnit> compilationUnits(final ICompilationUnit u, final IProgressMonitor pm) throws JavaModelException {
    pm.beginTask("Collection compilation units ", IProgressMonitor.UNKNOWN);
    final List<ICompilationUnit> $ = new ArrayList<>();
    if (u == null) {
      pm.done();
      announce("Cannot find current compilation unit " + u);
      return $;
    }
    final IJavaProject javaProject = u.getJavaProject();
    if (javaProject == null) {
      pm.done();
      announce("Cannot find project of " + u);
      return $;
    }
    if (!javaProject.isOpen()) {
      pm.done();
      announce(javaProject.getElementName() + " is not open");
      return $;
    }
    final IPackageFragmentRoot[] packageFragmentRoots = javaProject.getPackageFragmentRoots();
    if (packageFragmentRoots == null) {
      pm.done();
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

  // TODO Roth: do not create a compilation unit
  /** @param u JD
   * @param m JD
   * @return node marked by the marker in the compilation unit */
  @SuppressWarnings("boxing") static ASTNode getNodeByMarker(final ICompilationUnit u, final IMarker m) {
    try {
      final int s = (int) m.getAttribute(IMarker.CHAR_START);
      return new NodeFinder(Make.COMPILATION_UNIT.parser(u).createAST(new NullProgressMonitor()), s, (int) m.getAttribute(IMarker.CHAR_END) - s)
          .getCoveredNode();
    } catch (final CoreException x) {
      monitor.logEvaluationError(x);
    }
    return null;
  }

  static ImageIcon icon() {
    if (!iconInitialized) {
      iconInitialized = true;
      URL u;
      try {
        u = new URL(iconAddress);
        final Image i = Toolkit.getDefaultToolkit().getImage(u);
        if (i != null)
          icon = new ImageIcon(
              i/* .getScaledInstance(128, 128, Image.SCALE_SMOOTH) */);
      } catch (final MalformedURLException x) {
        x.printStackTrace();
      }
    }
    return icon;
  }

  static org.eclipse.swt.graphics.Image iconNonBusy() {
    if (!iconNonBusyInitialized) {
      iconNonBusyInitialized = true;
      try {
        iconNonBusy = new org.eclipse.swt.graphics.Image(null,
            ImageDescriptor.createFromURL(new URL("platform:/plugin/org.eclipse.team.ui/icons/full/obj/changeset_obj.gif")).getImageData());
      } catch (final MalformedURLException x) {
        monitor.log(x);
      }
    }
    return iconNonBusy;
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

  /** @return List of all compilation units in the current project */
  List<ICompilationUnit> compilationUnits() {
    try {
      return compilationUnits(currentCompilationUnit(), nullProgressMonitor);
    } catch (final JavaModelException x) {
      monitor.logEvaluationError(this, x);
    }
    return null;
  }

  List<ICompilationUnit> compilationUnits(final ICompilationUnit u) {
    try {
      return compilationUnits(u, nullProgressMonitor);
    } catch (final JavaModelException x) {
      monitor.logEvaluationError(this, x);
      return null;
    }
  }

  boolean isNodeOutsideMarker(final ASTNode n, final IMarker m) {
    try {
      return n.getStartPosition() < ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue()
          || n.getLength() + n.getStartPosition() > ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue();
    } catch (final CoreException x) {
      monitor.logEvaluationError(this, x);
      return true;
    }
  }
}
