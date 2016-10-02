package il.org.spartan.plugin;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;
import org.eclipse.ui.internal.handlers.WizardHandler.*;
import org.eclipse.ui.progress.*;

import il.org.spartan.spartanizer.dispatch.*;

/** A handler for {@link Tips}. This handler executes all safe Tips on all Java
 * files in the current project.
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @since 2015/08/01 */
public final class LaconizeProject extends BaseHandler {
  static final int MAX_PASSES = 20;
  private final StringBuilder status = new StringBuilder();
  private ICompilationUnit currentCompilationUnit;
  private IJavaProject javaProject;
  private List<ICompilationUnit> todo;
  private int initialCount;

  /** Returns the number of spartanization tips for a compilation unit
   * @param u JD
   * @return number of tips available for the compilation unit */
  public int countTips(final ICompilationUnit u) {
    final AtomicInteger $ = new AtomicInteger(0);
    try {
      PlatformUI.getWorkbench().getProgressService().run(true, true, pm -> {
        pm.beginTask("Looking for tips in " + u.getResource().getProject().getName(), IProgressMonitor.UNKNOWN);
        final GUI$Applicator ¢ = new Trimmer();
        ¢.setMarker(null);
        ¢.setICompilationUnit(u);
        $.addAndGet(¢.countTips());
        pm.done();
      });
    } catch (InvocationTargetException x) {
      LoggingManner.logEvaluationError(this, x);
    } catch (InterruptedException x) {
      LoggingManner.logCancellationRequest(this, x);
    }
    return $.get();
  }

  public LaconizeProject() {
    this(new Trimmer());
  }

  public LaconizeProject(final GUI$Applicator inner) {
    super(inner);
  }

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) throws ExecutionException {
    status.setLength(0);
    return go();
  }
  
  public void start() {
    currentCompilationUnit = eclipse.currentCompilationUnit();
    status.append("Starting at compilation unit: " + currentCompilationUnit.getElementName() + "\n");
    javaProject = currentCompilationUnit.getJavaProject();
    status.append("Java project is: " + javaProject + "\n");
    todo = eclipse.facade.compilationUnits(currentCompilationUnit);
    status.append("Found " + todo.size() + " compilation units, ");
    initialCount = countTips(currentCompilationUnit);
    status.append("with " + initialCount + " tips.\n");
  }

  public Void go() throws ExecutionException {
    start();
    if (initialCount == 0)
      return eclipse.announce(status + "No tips for '" + javaProject.getElementName() + "' project\n" + status);
    eclipse.announce(status);

    final GUI$Applicator a = new Trimmer();
    int i;
    final IWorkbench wb = PlatformUI.getWorkbench();
    for (i = 0; i < MAX_PASSES; ++i) {
      final IProgressService ps = wb.getProgressService();
      final AtomicInteger passNum = new AtomicInteger(i + 1);
      final AtomicBoolean cancelled = new AtomicBoolean(false);
      try {
        ps.run(true, true, pm -> {
          // a.setProgressMonitor(pm);
          pm.beginTask(
              "Spartanizing project '" + javaProject.getElementName() + "' - " + "Pass " + passNum.get() + " out of maximum of " + MAX_PASSES,
              todo.size());
          int n = 0;
          final List<ICompilationUnit> dead = new ArrayList<>();
          for (final ICompilationUnit ¢ : todo) {
            if (pm.isCanceled()) {
              cancelled.set(true);
              break;
            }
            pm.worked(1);
            pm.subTask("Compilation unit #" + ++n + "/" + todo.size() + " (" + ¢.getElementName() + ")");
            if (!a.apply(¢))
              dead.add(¢);
          }
          if (dead.size() == 0)
            status.append(dead.size() + " compilation  units remain unchanged; will not be processed again\n");
          todo.removeAll(dead);
          pm.done();
        });
      } catch (final InvocationTargetException x) {
        LoggingManner.logEvaluationError(this, x);
      } catch (final InterruptedException x) {
        LoggingManner.logEvaluationError(this, x);
      }
      if (cancelled.get() || todo.isEmpty())
        break;
    }
    if (i == MAX_PASSES)
      throw new ExecutionException(status + "Too many iterations");
    final int finalCount = countTips(currentCompilationUnit);
    return eclipse
        .announce(status + "Spartanizing '" + javaProject.getElementName() + "' project \n" + "Completed in " + (1 + i) + " passes. \n" + "Total changes: "
            + (initialCount - finalCount) + "\n" + "Tips before: " + initialCount + "\n" + "Tips after: " + finalCount + "\n" + status);
  }
}
