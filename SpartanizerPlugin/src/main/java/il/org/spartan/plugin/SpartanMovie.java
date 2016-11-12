package il.org.spartan.plugin;

import java.lang.reflect.*;
import java.util.*;
import java.util.List;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.*;
import org.eclipse.ui.progress.*;

import il.org.spartan.plugin.old.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.utils.*;

/** Even better than 300! A handler that runs the spartanization process step by
 * step until completion.
 * @author Ori Roth
 * @since 2016 */
public class SpartanMovie extends AbstractHandler {
  private static final String NAME = "Spartan movie";
  private static final double SLEEP_BETWEEN = 0.5;
  private static final double SLEEP_END = 2;

  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    final IWorkbench workbench = PlatformUI.getWorkbench();
    final List<ICompilationUnit> compilationUnits = getCompilationUnits();
    final IWorkbenchWindow window = workbench == null ? null : workbench.getActiveWorkbenchWindow();
    final IWorkbenchPage page = window == null ? null : window.getActivePage();
    final IProgressService progressService = workbench == null ? null : workbench.getProgressService();
    final Trimmer trimmer = new Trimmer();
    if (compilationUnits == null || page == null || progressService == null)
      return null;
    try {
      progressService.run(false, true, pm -> {
        moveProgressDialog();
        pm.beginTask(NAME, IProgressMonitor.UNKNOWN);
        int changes = 0;
        int filesModified = 0;
        // TODO Roth: this function is much much too large. Try to break it --yg
        for (final ICompilationUnit currentCompilationUnit : compilationUnits) {
          mightNotBeSlick(page);
          final IFile file = (IFile) currentCompilationUnit.getResource();
          try {
            IMarker[] markers = getMarkers(file);
            if (markers.length > 0)
              ++filesModified;
            for (; markers.length > 0; markers = getMarkers(file)) {
              final IMarker marker = getFirstMarker(markers);
              pm.subTask("Working on " + file.getName() + "\nCurrent tip: "
                  + ((Class<?>) marker.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY)).getSimpleName());
              IDE.openEditor(page, marker, true);
              refresh(page);
              sleep(SLEEP_BETWEEN);
              trimmer.runAsMarkerFix(marker);
              ++changes;
              marker.delete(); // TODO Roth: does not seem to make a difference
              refresh(page);
              sleep(SLEEP_BETWEEN);
            }
          } catch (final CoreException x) {
            monitor.log(x);
          }
        }
        pm.subTask("Done: Commited " + changes + " changes in " + filesModified + " " + RefactorerUtil.plurals("file", filesModified));
        sleep(SLEEP_END);
        pm.done();
      });
    } catch (InvocationTargetException | InterruptedException x) {
      monitor.log(x);
      x.printStackTrace();
    }
    sleep(1);
    return null;
  }
  /** Just in case, so that editors don't pile up. Not sure this is the right
   * behavior
   * <p>
   * Ori Roth says: it just looks better this way. Editors do not pile up and
   * create a mess.
   * @author Yossi Gil
   * @param ¢ JD */
  // sure this is the right behavior
  public static void mightNotBeSlick(final IWorkbenchPage ¢) {
    close(¢);
  }
  /** @param ¢
   * @return
   * @throws CoreException */
  private static IMarker[] getMarkers(final IFile ¢) {
    try {
      return ¢.findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE);
    } catch (final CoreException x) {
      monitor.log(x);
      return new IMarker[0];
    }
  }
  private static List<ICompilationUnit> getCompilationUnits() {
    try {
      return eclipse.compilationUnits(eclipse.currentCompilationUnit(), wizard.nullProgressMonitor);
    } catch (final JavaModelException x) {
      monitor.log(x);
      return new LinkedList<>();
    }
  }
  static boolean focus(final IWorkbenchPage p, final IFile f) {
    try {
      IDE.openEditor(p, f, true);
    } catch (final PartInitException x) {
      monitor.log(x);
      return false;
    }
    return true;
  }
  static void close(final IWorkbenchPage ¢) {
    ¢.closeAllEditors(true);
  }
  /** The current SpartanMovie is not releaseable. Some big changes should be
   * made.
   * @author Ori Roth
   * @param howMuch
   * @return */
  static boolean sleep(final double howMuch) {
    try {
      Thread.sleep((int) (1000 * howMuch));
      return true;
    } catch (@SuppressWarnings("unused") final InterruptedException __) {
      return false;
    }
  }
  static void refresh(final IWorkbenchPage ¢) {
    ¢.getWorkbenchWindow().getShell().update();
    ¢.getWorkbenchWindow().getShell().layout(true);
  }
  static void moveProgressDialog() {
    final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
    final Shell parentShell = shell == null ? null : shell.getParent().getShell();
    if (shell != null && parentShell != null)
      shell.setLocation(parentShell.getBounds().x + parentShell.getBounds().width - shell.getBounds().width, parentShell.getBounds().y);
  }
  /** Finds the first marker in array in terms of textual location. The
   * "CHAR_START" attribute is not something I have added, but an existing and
   * well maintained marker attribute.
   * @author Ori Roth */
  static IMarker getFirstMarker(final IMarker[] ¢) {
    int $ = 0;
    for (int i = 0; i < ¢.length; ++i)
      try {
        if (((Integer) ¢[i].getAttribute(IMarker.CHAR_START)).intValue() < ((Integer) ¢[$].getAttribute(IMarker.CHAR_START)).intValue())
          $ = i;
      } catch (final CoreException x) {
        monitor.log(x);
        break;
      }
    return ¢[$];
  }
}
