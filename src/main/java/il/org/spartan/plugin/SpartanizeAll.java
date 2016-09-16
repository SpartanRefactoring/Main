package il.org.spartan.plugin;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.core.commands.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;
import org.eclipse.ui.progress.*;

/** A handler for {@link Spartanizations}. This handler executes all safe
 * Spartanizations on all Java files in the current project.
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @since 2015/08/01 */
public class SpartanizeAll extends BaseHandler {
  static final int MAX_PASSES = 20;

  /** Returns the number of spartanization suggestions for a compilation unit
   * @param u JD
   * @return number of suggestions available for the compilation unit */
  public static int countSuggestions(final ICompilationUnit u) {
    int $ = 0;
    for (final Applicator ¢ : eclipse.safeSpartanizations) {
      ¢.setMarker(null);
      ¢.setCompilationUnit(u);
      $ += ¢.countSuggestions();
    }
    return $;
  }

  /** Instantiates this class */
  public SpartanizeAll() {
    super(null);
  }

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) throws ExecutionException {
    final StringBuilder message = new StringBuilder();
    final ICompilationUnit currentCompilationUnit = eclipse.currentCompilationUnit();
    final IJavaProject javaProject = currentCompilationUnit.getJavaProject();
    message.append("starting at " + currentCompilationUnit.getElementName() + "\n");
    final List<ICompilationUnit> us = eclipse.compilationUnits(currentCompilationUnit);
    message.append("found " + us.size() + " compilation units \n");
    final IWorkbench wb = PlatformUI.getWorkbench();
    final int initialCount = countSuggestions(currentCompilationUnit);
    message.append("with " + initialCount + " suggestions");
    if (initialCount == 0)
      return eclipse.announce("No suggestions for '" + javaProject.getElementName() + "' project\n" + message);
    for (int i = 0; i < MAX_PASSES; ++i) {
      final IProgressService ps = wb.getProgressService();
      final AtomicInteger passNum = new AtomicInteger(i + 1);
      try {
        // TODO: Ori, please please no busy cursor. Use ProgressManager
        ps.busyCursorWhile(pm -> {
          pm.beginTask("Spartanizing project '" + javaProject.getElementName() + "' - " + //
          "Pass " + passNum.get() + " out of maximum of " + MAX_PASSES, us.size());
          int n = 0;
          final List<ICompilationUnit> es = new LinkedList<>();
          for (final ICompilationUnit ¢ : us) {
            if (!eclipse.apply(¢))
              es.add(¢);
            pm.worked(1);
            pm.subTask(¢.getElementName() + " " + ++n + "/" + us.size());
          }
          us.removeAll(es);
          pm.done();
        });
      } catch (final InvocationTargetException x) {
        x.printStackTrace();
      } catch (final InterruptedException x) {
        x.printStackTrace();
      }
      final int finalCount = countSuggestions(currentCompilationUnit);
      if (finalCount <= 0)
        return eclipse.announce("Spartanizing '" + javaProject.getElementName() + "' project \n" + //
            "Completed in " + (1 + i) + " passes. \n" + //
            "Total changes: " + (initialCount - finalCount) + "\n" + //
            "Suggestions before: " + initialCount + "\n" + //
            "Suggestions after: " + finalCount + "\n" + //
            message);
    }
    throw new ExecutionException("Too many iterations");
  }
}
