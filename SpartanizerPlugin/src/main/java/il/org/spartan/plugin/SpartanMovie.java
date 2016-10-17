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
          // XXX Roth: seems strange; not saying it is not right, but try to
          // make it evident why this is necessary. --yg
          // TODO Yossi: it just looks better this way. Editors do not pile up
          // and create a mess. --or
          close(page);
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

  /** @param ¢
   * @return
   * @throws CoreException */
  private static IMarker[] getMarkers(final IFile ¢) {
    try {
      return ¢.findMarkers(Builder.MARKER_TYPE, true, IResource.DEPTH_INFINITE);
    } catch (CoreException x) {
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

  static boolean sleep(final double i) {
    try {
      Thread.sleep((int) (1000 * i));
      return true;
    } catch (@SuppressWarnings("unused") final InterruptedException __) {
      // XXX Roth: this seems like an awful bug to me. You cannot interrupt
      // during sleep? Huh? --yg
      // TODO Yossi: you are defiantly right. The current SpartanMovie is not
      // releasable. Some big changes should be made. --or
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
    // TODO Roth: I think you should check for 'p' only; give it a meaningful
    // name --yg
    if (shell != null && parentShell != null)
      shell.setLocation(parentShell.getBounds().x + parentShell.getBounds().width - shell.getBounds().width, parentShell.getBounds().y);
  }

  static IMarker getFirstMarker(final IMarker[] ¢) {
    int $ = 0;
    for (int i = 0; i < ¢.length; ++i)
      try {
        // XXX Roth: how could this ever be true? --yg
        // XXX Roth: are you sure you can store 'int'? --yg
        // TODO Yossi: this function finds the first marker in array in terms of
        // textual location. The "CHAR_START" attribute is not something I have
        // added, but an existing and well maintained marker attribute. I agree
        // this can be done with more caution, although it works fine by now.
        // --or
        if (((Integer) ¢[i].getAttribute(IMarker.CHAR_START)).intValue() < ((Integer) ¢[$].getAttribute(IMarker.CHAR_START)).intValue())
          $ = i;
      } catch (final CoreException x) {
        monitor.log(x);
        break;
      }
    return ¢[$];
  }
}
