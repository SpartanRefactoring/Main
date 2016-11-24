package il.org.spartan.plugin.old;

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
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Fluent API services for the plugin
 * @author Yossi Gil
 * @since 2016 */
public enum eclipse {
  facade;
  static ImageIcon icon;
  static org.eclipse.swt.graphics.Image iconNonBusy;
  static final String NAME = "Laconic";
  static final Shell parent = null;
  static final boolean persistLocation = false;
  static final boolean persistSize = false;
  static final int shellStyle = SWT.TOOL;
  static final boolean showDialogMenu = true;
  static final boolean takeFocusOnOpen = false;
  private static final String iconAddress = "platform:/plugin/org.eclipse.compare/icons/full/wizban/applypatch_wizban.png";
  private static boolean iconInitialized;
  private static boolean iconNonBusyInitialized;

  /** Add nature to one project */
  public static void addNature(final IProject p) throws CoreException {
    final IProjectDescription d = p.getDescription();
    final String[] natures = d.getNatureIds();
    if (as.list(natures).contains(Nature.NATURE_ID))
      return; // Already got the nature
    d.setNatureIds(append(natures, Nature.NATURE_ID));
    p.setDescription(d, null);
  }

  /** @param u A compilation unit for reference - you give me an arbitrary
   *        compilation unit from the project and I'll find the root of the
   *        project and do my magic.
   * @param m A standard {@link IProgressMonitor} - if you don't care about
   *        operation times use {@link wizard@nullProgressMonitor}
   * @return List of all compilation units in the current project
   * @throws JavaModelException don't forget to catch */
  public static List<ICompilationUnit> compilationUnits(final ICompilationUnit u, final IProgressMonitor m) throws JavaModelException {
    m.beginTask("Collection compilation units ", IProgressMonitor.UNKNOWN);
    final List<ICompilationUnit> $ = new ArrayList<>();
    if (u == null)
      return done(m, $, "Cannot find current compilation unit " + u);
    final IJavaProject javaProject = u.getJavaProject();
    if (javaProject == null)
      return done(m, $, "Cannot find project of " + u);
    if (!javaProject.isOpen())
      return done(m, $, javaProject.getElementName() + " is not open");
    final IPackageFragmentRoot[] rs = javaProject.getPackageFragmentRoots();
    if (rs == null)
      return done(m, $, "Cannot find roots of " + javaProject);
    final int n = 0;
    for (final IPackageFragmentRoot ¢ : rs)
      compilationUnits(m, $, ¢);
    return done(m, $, "Found " + n + " package roots, and " + $.size() + " packages");
  }

  public static int compilationUnits(final IProgressMonitor m, final List<ICompilationUnit> us, final IPackageFragmentRoot r)
      throws JavaModelException {
    int $ = 0;
    m.worked(1);
    if (r.getKind() == IPackageFragmentRoot.K_SOURCE)
      m.worked(1);
    for (final IJavaElement ¢ : r.getChildren()) {
      m.worked(1);
      if (¢.getElementType() == IJavaElement.PACKAGE_FRAGMENT && az.true¢(++$)) {
        ++$;
        us.addAll(as.list(((IPackageFragment) ¢).getCompilationUnits()));
      }
    }
    return $;
  }

  /** Retrieves the current {@link ICompilationUnit}
   * @return current {@link ICompilationUnit} */
  public static ICompilationUnit currentCompilationUnit() {
    return compilationUnit(currentWorkbenchWindow().getActivePage().getActiveEditor());
  }

  public static List<ICompilationUnit> done(final IProgressMonitor pm, final List<ICompilationUnit> $, final String message) {
    pm.done();
    announce(message);
    return $;
  }

  @SuppressWarnings("deprecation") public static IProgressMonitor newSubMonitor(final IProgressMonitor ¢) {
    return new SubProgressMonitor(¢, 1, SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK);
  }

  static Void announce(final Object message) {
    announceNonBusy(message + "").open();
    return null;
  }

  static MessageDialog announceNonBusy(final String message) {
    return new MessageDialog(null, NAME, iconNonBusy(), message, MessageDialog.INFORMATION, new String[] { "OK" }, 0) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.ON_TOP | SWT.MODELESS);
      }
    };
  }

  static ICompilationUnit compilationUnit(final IEditorPart ep) {
    return ep == null ? null : compilationUnit((IResource) resources(ep));
  }

  static ICompilationUnit compilationUnit(final IResource ¢) {
    return ¢ == null ? null : JavaCore.createCompilationUnitFrom((IFile) ¢);
  }

  /** Retrieves the current {@link IWorkbenchWindow}
   * @return current {@link IWorkbenchWindow} */
  static IWorkbenchWindow currentWorkbenchWindow() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
  }

  // XXX Roth: do not create a compilation unit
  /** @param u JD
   * @param m JD
   * @return node marked by the marker in the compilation unit */
  static ASTNode getNodeByMarker(final ICompilationUnit u, final IMarker m) {
    try {
      return find(u, int¢(m, IMarker.CHAR_START), int¢(m, IMarker.CHAR_END));
    } catch (final CoreException ¢) {
      monitor.logEvaluationError(¢);
    }
    return null;
  }

  public static ASTNode find(final ICompilationUnit u, final int start, final int end) {
    return new NodeFinder(createAST(u), start, end - start).getCoveredNode();
  }

  public static ASTNode createAST(final ICompilationUnit ¢) {
    return Make.COMPILATION_UNIT.parser(¢).createAST(nullProgressMonitor);
  }

  public static int int¢(final IMarker m, final String name) throws CoreException {
    return az.int¢(m.getAttribute(name));
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
      } catch (final MalformedURLException ¢) {
        ¢.printStackTrace();
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
      } catch (final MalformedURLException ¢) {
        monitor.log(¢);
      }
    }
    return iconNonBusy;
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

  static Object resources(final IEditorPart ep) {
    return ep.getEditorInput().getAdapter(IResource.class);
  }

  static ITextSelection selectedText() {
    final IEditorPart ep = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    final ISelection s = ep.getEditorSite().getSelectionProvider().getSelection();
    return !(s instanceof ITextSelection) ? null : (ITextSelection) s;
  }

  public boolean isNodeOutsideMarker(final ASTNode n, final IMarker m) {
    try {
      return n.getStartPosition() < ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue()
          || n.getLength() + n.getStartPosition() > ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue();
    } catch (final CoreException ¢) {
      monitor.logEvaluationError(this, ¢);
      return true;
    }
  }

  /** @return List of all compilation units in the current project */
  List<ICompilationUnit> compilationUnits() {
    try {
      return compilationUnits(currentCompilationUnit(), nullProgressMonitor);
    } catch (final JavaModelException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return null;
  }

  List<ICompilationUnit> compilationUnits(final ICompilationUnit u) {
    try {
      return compilationUnits(u, nullProgressMonitor);
    } catch (final JavaModelException ¢) {
      monitor.logEvaluationError(this, ¢);
      return null;
    }
  }
}
