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
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Even better than 300! A handler that runs the spartanization process step by
 * step until completion.
 * @author Ori Roth
 * @since 2016 */
public class SpartanMovie extends AbstractHandler {
  private static final String NAME = "Spartan movie";
  private static final double SLEEP_BETWEEN = 0.5;
  private static final double SLEEP_END = 2;

  @Nullable
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
        int changes = 0, filesModified = 0;
        // TODO Ori Roth: this function is much much too large. Try to break it
        // --yg
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
              marker.delete(); // TODO Ori Roth: does not seem to make a
                               // difference
              refresh(page);
              sleep(SLEEP_BETWEEN);
            }
          } catch (@NotNull final CoreException ¢) {
            monitor.log(¢);
          }
        }
        pm.subTask("Done: Commited " + changes + " changes in " + filesModified + " " + Linguistic.plurals("file", filesModified));
        sleep(SLEEP_END);
        pm.done();
      });
    } catch (@NotNull InvocationTargetException | InterruptedException ¢) {
      monitor.log(¢);
      ¢.printStackTrace();
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
  public static void mightNotBeSlick(@NotNull final IWorkbenchPage ¢) {
    close(¢);
  }

  private static IMarker[] getMarkers(@NotNull final IFile $) {
    try {
      return $.findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE);
    } catch (@NotNull final CoreException m) {
      monitor.log(m);
      return new IMarker[0];
    }
  }

  private static List<ICompilationUnit> getCompilationUnits() {
    try {
      return eclipse.compilationUnits(eclipse.currentCompilationUnit(), wizard.nullProgressMonitor);
    } catch (@NotNull final JavaModelException ¢) {
      monitor.log(¢);
      return new ArrayList<>();
    }
  }

  static boolean focus(@NotNull final IWorkbenchPage p, final IFile f) {
    try {
      IDE.openEditor(p, f, true);
    } catch (@NotNull final PartInitException ¢) {
      monitor.log(¢);
      return false;
    }
    return true;
  }

  static void close(@NotNull final IWorkbenchPage ¢) {
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
    } catch (@NotNull @SuppressWarnings("unused") final InterruptedException __) {
      return false;
    }
  }

  static void refresh(@NotNull final IWorkbenchPage ¢) {
    ¢.getWorkbenchWindow().getShell().update();
    ¢.getWorkbenchWindow().getShell().layout(true);
  }

  static void moveProgressDialog() {
    final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell(), parentShell = shell == null ? null : shell.getParent().getShell();
    if (shell != null && parentShell != null)
      shell.setLocation(parentShell.getBounds().x + parentShell.getBounds().width - shell.getBounds().width, parentShell.getBounds().y);
  }

  /** Finds the first marker in array in terms of textual location. The
   * "CHAR_START" attribute is not something I have added, but an existing and
   * well maintained marker attribute.
   * @author Ori Roth */
  @SuppressWarnings("boxing") static IMarker getFirstMarker(@NotNull final IMarker[] ms) {
    int $ = 0;
    for (final Integer i : range.from(0).to(ms.length))
      try {
        if (((Integer) ms[i].getAttribute(IMarker.CHAR_START)).intValue() < ((Integer) ms[$].getAttribute(IMarker.CHAR_START)).intValue())
          $ = i;
      } catch (@NotNull final CoreException ¢) {
        monitor.log(¢);
        break;
      }
    return ms[$];
  }
}
