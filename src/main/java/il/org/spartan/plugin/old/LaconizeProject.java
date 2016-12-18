package il.org.spartan.plugin.old;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;
import org.eclipse.ui.progress.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.utils.*;
import static il.org.spartan.lisp.*;

/** A handler for {@link Tips}. This handler executes all safe Tips on all Java
 * files in the current project.
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @author Ori Roth
 * @author Yossi Gil
 * @since 2015/08/01 */
public final class LaconizeProject extends BaseHandler {
  static final int MAX_PASSES = 20;
  private final StringBuilder status = new StringBuilder();
  private ICompilationUnit currentCompilationUnit;
  IJavaProject javaProject;
  final List<ICompilationUnit> todo = new ArrayList<>();
  private int initialCount;
  private final List<ICompilationUnit> done = new ArrayList<>();
  private int passNumber;

  /** Returns the number of spartanization tips for a compilation unit
   * @param u JD
   * @return number of tips available for the compilation unit */
  public int countTips() {
    if (todo.isEmpty())
      return 0;
    final Int $ = new Int();
    final AbstractGUIApplicator a = new Trimmer();
    try {
      eclipse.progressMonitorDialog(true).run(true, true, pm -> {
        pm.beginTask("Looking for tips in " + javaProject.getElementName(), IProgressMonitor.UNKNOWN);
        a.setMarker(null);
        a.setICompilationUnit(first(todo));
        $.addAndGet(a.countTips());
        if (pm.isCanceled())
          $.set(0);
        pm.done();
      });
    } catch (InvocationTargetException | InterruptedException ¢) {
      ¢.printStackTrace();
    }
    return $.get();
  }

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    status.setLength(0);
    todo.clear();
    done.clear();
    initialCount = 0;
    return go();
  }

  public Void go() {
    start();
    if (initialCount == 0)
      return eclipse.announce(status + "No tips found.");
    manyPasses();
    todo.clear();
    todo.addAll(eclipse.facade.compilationUnits(currentCompilationUnit));
    final int $ = countTips();
    return eclipse.announce(//
        status + "Laconizing '" + javaProject.getElementName() + "' project \n" + "Completed in " + passNumber + " passes. \n"
            + (passNumber < MAX_PASSES ? "" : "   === too many passes\n") + "Tips followed: " + (initialCount - $) + "\n" + "Tips before: "
            + initialCount + "\n" + "Tips after: " + $ + "\n");
  }

  final IWorkbench workench = PlatformUI.getWorkbench();

  void manyPasses() {
    for (passNumber = 1;; ++passNumber)
      if (passNumber > MAX_PASSES || singlePass())
        return;
  }

  boolean singlePass() {
    final Trimmer t = new Trimmer();
    final IProgressService ps = workench.getProgressService();
    final Int passNum = new Int(passNumber + 1);
    final AtomicBoolean $ = new AtomicBoolean(false);
    try {
      ps.run(true, true, pm -> {
        pm.beginTask("Spartanizing project '" + javaProject.getElementName() + "' - " + "Pass " + passNum.get() + " out of maximum of " + MAX_PASSES,
            todo.size());
        int n = 0;
        for (final ICompilationUnit ¢ : todo) {
          if (pm.isCanceled()) {
            $.set(true);
            break;
          }
          pm.worked(1);
          pm.subTask("Compilation unit #" + ++n + "/" + todo.size() + " (" + ¢.getElementName() + ")");
          if (!t.apply(¢))
            done.add(¢);
        }
        if (!done.isEmpty())
          status.append(done.size() + " CUs did not change; will not be processed further\n");
        todo.removeAll(done);
        done.clear();
        pm.done();
      });
    } catch (final InterruptedException | InvocationTargetException ¢) {
      monitor.logEvaluationError(this, ¢);
      return true;
    }
    return $.get() || todo.isEmpty();
  }

  public void start() {
    currentCompilationUnit = eclipse.currentCompilationUnit();
    status.append("Starting at compilation unit: " + currentCompilationUnit.getElementName() + "\n");
    javaProject = currentCompilationUnit.getJavaProject();
    status.append("Java project is: " + javaProject.getElementName() + "\n");
    todo.clear();
    todo.addAll(eclipse.facade.compilationUnits(currentCompilationUnit));
    status.append("Found " + todo.size() + " compilation units, ");
    initialCount = countTips();
    status.append("with " + initialCount + " tips.\n");
  }
}