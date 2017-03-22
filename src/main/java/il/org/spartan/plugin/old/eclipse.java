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
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Fluent API services for the plugin
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
public enum eclipse {
  facade;
  static ImageIcon icon;
  @Nullable
  static org.eclipse.swt.graphics.Image iconNonBusy;
  static final String NAME = "Laconic";
  @Nullable
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
  public static void addNature(@NotNull final IProject p) throws CoreException {
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
   *        operation times use {@link wizard#nullProgressMonitor}
   * @return List of all compilation units in the current project
   * @throws JavaModelException don't forget to catch */
  public static List<ICompilationUnit> compilationUnits(@Nullable final IJavaElement u, @NotNull final IProgressMonitor m) throws JavaModelException {
    m.beginTask("Collection compilation units ", IProgressMonitor.UNKNOWN);
    @NotNull final List<ICompilationUnit> $ = new ArrayList<>();
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
    for (@NotNull final IPackageFragmentRoot ¢ : rs) // NANO - can't, throws
      compilationUnits(m, $, ¢);
    return done(m, $, "Found " + rs.length + " package roots, and " + $.size() + " packages");
  }

  private static int compilationUnits(@NotNull final IProgressMonitor m, @NotNull final Collection<ICompilationUnit> us, @NotNull final IPackageFragmentRoot r)
      throws JavaModelException {
    m.worked(1);
    if (r.getKind() == IPackageFragmentRoot.K_SOURCE)
      m.worked(1);
    int $ = 0;
    for (@NotNull final IJavaElement ¢ : r.getChildren()) {
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

  private static List<ICompilationUnit> done(@NotNull final IProgressMonitor pm, final List<ICompilationUnit> $, final String message) {
    pm.done();
    announce(message);
    return $;
  }

  @NotNull
  @SuppressWarnings("deprecation") public static IProgressMonitor newSubMonitor(final IProgressMonitor ¢) {
    return new SubProgressMonitor(¢, 1, SubProgressMonitor.PREPEND_MAIN_LABEL_TO_SUBTASK);
  }

  static Void announce(final Object message) {
    announceNonBusy(message + "").open();
    return null;
  }

  @NotNull
  static MessageDialog announceNonBusy(final String message) {
    return new MessageDialog(null, NAME, iconNonBusy(), message, MessageDialog.INFORMATION, new String[] { "OK" }, 0) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.ON_TOP);
      }
    };
  }

  @Nullable
  static ICompilationUnit compilationUnit(@Nullable final IEditorPart ep) {
    return ep == null ? null : compilationUnit((IResource) resources(ep));
  }

  static ICompilationUnit compilationUnit(@Nullable final IResource ¢) {
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
  static ASTNode getNodeByMarker(final ICompilationUnit $, @NotNull final IMarker m) {
    try {
      return find($, int¢(m, IMarker.CHAR_START), int¢(m, IMarker.CHAR_END));
    } catch (@NotNull final CoreException ¢) {
      monitor.logEvaluationError(¢);
    }
    return null;
  }

  private static ASTNode find(final ICompilationUnit u, final int start, final int end) {
    return new NodeFinder(createAST(u), start, end - start).getCoveredNode();
  }

  private static ASTNode createAST(final ICompilationUnit ¢) {
    return make.COMPILATION_UNIT.parser(¢).createAST(nullProgressMonitor);
  }

  private static int int¢(@NotNull final IMarker m, final String name) throws CoreException {
    return az.int¢(m.getAttribute(name));
  }

  static ImageIcon icon() {
    if (iconInvalid) {
      iconInvalid = false;
      try {
        final Image i = Toolkit.getDefaultToolkit().getImage(new URL(iconAddress));
        if (i != null)
          icon = new ImageIcon(
              i/* .getScaledInstance(128, 128, Image.SCALE_SMOOTH) */);
      } catch (@NotNull final MalformedURLException ¢) {
        monitor.logProbableBug(¢);
      }
    }
    return icon;
  }

  @Nullable
  static org.eclipse.swt.graphics.Image iconNonBusy() {
    if (iconNotBusyInvalid) {
      iconNotBusyInvalid = false;
      try {
        iconNonBusy = new org.eclipse.swt.graphics.Image(null,
            ImageDescriptor.createFromURL(new URL("platform:/plugin/org.eclipse.team.ui/icons/full/obj/changeset_obj.gif")).getImageData());
      } catch (@NotNull final MalformedURLException ¢) {
        monitor.log(¢);
      }
    }
    return iconNonBusy;
  }

  @NotNull
  static ProgressMonitorDialog progressMonitorDialog(final boolean openOnRun) {
    @NotNull final ProgressMonitorDialog $ = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER);
      }
    };
    $.setBlockOnOpen(false);
    $.setCancelable(true);
    $.setOpenOnRun(openOnRun);
    return $;
  }

  static Object resources(@NotNull final IEditorPart ep) {
    return ep.getEditorInput().getAdapter(IResource.class);
  }

  static ITextSelection selectedText() {
    final ISelection $ = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getSelectionProvider()
        .getSelection();
    return !($ instanceof ITextSelection) ? null : (ITextSelection) $;
  }

  public boolean isNodeOutsideMarker(@NotNull final ASTNode $, @NotNull final IMarker m) {
    try {
      return $.getStartPosition() < ((Integer) m.getAttribute(IMarker.CHAR_START)).intValue()
          || $.getLength() + $.getStartPosition() > ((Integer) m.getAttribute(IMarker.CHAR_END)).intValue();
    } catch (@NotNull final CoreException ¢) {
      monitor.logEvaluationError(this, ¢);
      return true;
    }
  }

  /** @return List of all compilation units in the current project */
  List<ICompilationUnit> compilationUnits() {
    try {
      return compilationUnits(currentCompilationUnit(), nullProgressMonitor);
    } catch (@NotNull final JavaModelException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return null;
  }

  Collection<ICompilationUnit> compilationUnits(final IJavaElement $) {
    try {
      return compilationUnits($, nullProgressMonitor);
    } catch (@NotNull final JavaModelException ¢) {
      monitor.logEvaluationError(this, ¢);
      return null;
    }
  }
}
