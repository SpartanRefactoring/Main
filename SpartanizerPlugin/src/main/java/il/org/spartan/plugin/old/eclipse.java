package il.org.spartan.plugin.old;

import static il.org.spartan.Utils.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

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

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.traversal.Traversal.*;
import nano.ly.*;

/** Fluent API services for the plugin
 * @author Yossi Gil
 * @since 2016 */
public enum eclipse {
  facade;
  static ImageIcon icon;
  static org.eclipse.swt.graphics.Image iconNonBusy;
  static final String NAME = "The Spartanizer";
  static final Shell parent = null;
  static final boolean persistLocation = false;
  static final boolean persistSize = false;
  static final int shellStyle = SWT.TOOL;
  static final boolean showDialogMenu = true;
  static final boolean takeFocusOnOpen = false;
  private static final String iconAddress = "platform:/plugin/org.eclipse.compare/icons/full/wizban/applypatch_wizban.png";
  private static boolean iconInvalid = true;
  private static boolean iconNotBusyInvalid = true;

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
   *        operation times use {@link __#nullProgressMonitor}
   * @return List of all compilation units in the current project
   * @throws JavaModelException don't forget to catch */
  public static List<ICompilationUnit> compilationUnits(final IJavaElement u, final IProgressMonitor m) throws JavaModelException {
    m.beginTask("Collection compilation units ", IProgressMonitor.UNKNOWN);
    final List<ICompilationUnit> $ = an.empty.list();
    if (u == null)
      return done(m, $, "Cannot find current compilation unit " + null);
    final IJavaProject javaProject = u.getJavaProject();
    if (javaProject == null)
      return done(m, $, "Cannot find project of " + u);
    if (!javaProject.isOpen())
      return done(m, $, javaProject.getElementName() + " is not open");
    final IPackageFragmentRoot[] rs = javaProject.getPackageFragmentRoots();
    if (rs == null)
      return done(m, $, "Cannot find roots of " + javaProject);
    for (final IPackageFragmentRoot ¢ : rs) // NANO - can't, throws
      compilationUnits(m, $, ¢);
    return done(m, $, "Found " + rs.length + " package roots, and " + $.size() + " packages");
  }

  private static int compilationUnits(final IProgressMonitor m, final Collection<ICompilationUnit> us, final IPackageFragmentRoot r)
      throws JavaModelException {
    m.worked(1);
    if (r.getKind() == IPackageFragmentRoot.K_SOURCE)
      m.worked(1);
    int $ = 0;
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

  private static List<ICompilationUnit> done(final IProgressMonitor pm, final List<ICompilationUnit> $, final String message) {
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
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.ON_TOP);
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
  static ASTNode getNodeByMarker(final ICompilationUnit $, final IMarker m) {
    try {
      return !m.exists() ? null : find($, int¢(m, IMarker.CHAR_START), int¢(m, IMarker.CHAR_END));
    } catch (final CoreException ¢) {
      return note.bug(¢);
    }
  }

  private static ASTNode find(final ICompilationUnit u, final int start, final int end) {
    return new NodeFinder(createAST(u), start, end - start).getCoveredNode();
  }

  private static ASTNode createAST(final ICompilationUnit ¢) {
    return make.COMPILATION_UNIT.parser(¢).createAST(nullProgressMonitor);
  }

  private static int int¢(final IMarker m, final String name) throws CoreException {
    return az.int¢(m.getAttribute(name));
  }

  static ImageIcon icon() {
    if (!iconInvalid)
      return icon;
    iconInvalid = false;
    try {
      final Image i = Toolkit.getDefaultToolkit().getImage(new URL(iconAddress));
      if (i != null)
        icon = new ImageIcon(i);
    } catch (final MalformedURLException $) {
      return note.io($);
    }
    return icon;
  }

  static org.eclipse.swt.graphics.Image iconNonBusy() {
    if (!iconNotBusyInvalid)
      return iconNonBusy;
    iconNotBusyInvalid = false;
    try {
      iconNonBusy = new org.eclipse.swt.graphics.Image(null,
          ImageDescriptor.createFromURL(new URL("platform:/plugin/org.eclipse.team.ui/icons/full/obj/changeset_obj.gif")).getImageData());
    } catch (final MalformedURLException ¢) {
      note.bug(¢);
    }
    return iconNonBusy;
  }

  static ProgressMonitorDialog progressMonitorDialog(final boolean openOnRun) {
    final ProgressMonitorDialog $ = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER);
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
    final ISelection $ = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getSelectionProvider()
        .getSelection();
    return !($ instanceof ITextSelection) ? null : (ITextSelection) $;
  }

  /** @return List of all compilation units in the current project */
  List<ICompilationUnit> compilationUnits() {
    try {
      return compilationUnits(currentCompilationUnit(), nullProgressMonitor);
    } catch (final JavaModelException $) {
      return note.bug(this, $);
    }
  }

  Collection<ICompilationUnit> compilationUnits(final IJavaElement $) {
    try {
      return compilationUnits($, nullProgressMonitor);
    } catch (final JavaModelException ¢) {
      return note.bug(this, ¢);
    }
  }

  public static IProject[] getAllSpartanizerProjects() {
    return ResourcesPlugin.getWorkspace().getRoot().getProjects();
  }
}
