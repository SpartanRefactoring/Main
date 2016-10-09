package il.org.spartan.plugin;

import java.lang.reflect.*;
import java.util.List;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.*;
import org.eclipse.ui.progress.*;

import il.org.spartan.spartanizer.dispatch.*;

/** Even better than 300! A handler that runs the spartanization process step by
 * step until completion.
 * @author Ori Roth
 * @since 2016 */
public class SpartanMovie extends AbstractHandler {
  private static final String NAME = "Spartan Movie";
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
        for (final ICompilationUnit currentCompilationUnit : compilationUnits) {
          close(page);
          final IFile file = (IFile) currentCompilationUnit.getResource();
          boolean counterInitialized = false;
          try {
            for (IMarker[] markers = file.findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE); markers != null
                && markers.length > 0; markers = file.findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE)) {
              if (!counterInitialized) {
                ++filesModified;
                counterInitialized = true;
              }
              final IMarker marker = getFirstMarker(markers);
              pm.subTask("Working on " + file.getName() + "\nCurrent tip: "
                  + ((Class<?>) marker.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY)).getSimpleName());
              IDE.openEditor(page, marker, true);
              refresh(page);
              sleep(SLEEP_BETWEEN);
              trimmer.runAsMarkerFix(marker);
              ++changes;
              marker.delete(); // TODO Roth: does not seam to make a difference
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

  private static List<ICompilationUnit> getCompilationUnits() {
    try {
      return eclipse.compilationUnits(eclipse.currentCompilationUnit(), new NullProgressMonitor());
    } catch (final JavaModelException x) {
      monitor.log(x);
    }
    return null;
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

  static boolean sleep(final double i) {
    try {
      Thread.sleep((int) (1000 * i));
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
    final Shell s = PlatformUI.getWorkbench().getDisplay().getActiveShell();
    final Shell p = s == null ? null : s.getParent().getShell();
    if (s != null && p != null)
      s.setLocation(p.getBounds().x + p.getBounds().width - s.getBounds().width, p.getBounds().y);
  }

  @SuppressWarnings("boxing") static IMarker getFirstMarker(final IMarker[] ¢) {
    int $ = 0;
    for (int i = 0; i < ¢.length; ++i)
      try {
        if ((int) ¢[i].getAttribute(IMarker.CHAR_START) < (int) ¢[$].getAttribute(IMarker.CHAR_START))
          $ = i;
      } catch (final CoreException x) {
        monitor.log(x);
        break;
      }
    return ¢[$];
  }
}
