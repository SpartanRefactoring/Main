package il.org.spartan.plugin;

import java.lang.reflect.*;

import javax.swing.*;

import org.eclipse.core.commands.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;
import org.eclipse.ui.progress.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.utils.*;

/** A handler for {@link Spartanizations} This handler executes all safe
 * spartanizations on all Java files in the current project, while exposing
 * static methods to spartanize only specific compilation units.
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @since 2015/08/01 */
public final class SpartanizeCurrent extends BaseHandler {
  private int MAX_PASSES = 20;

  @Override public Void execute(final ExecutionEvent e) throws ExecutionException {
    final ICompilationUnit currentCompilationUnit = eclipse.currentCompilationUnit();
    final StringBuilder status = new StringBuilder("Spartanizing " +  currentCompilationUnit.getElementName());
    JOptionPane pane = new JOptionPane(status, JOptionPane.INFORMATION_MESSAGE, JOptionPane.NO_OPTION, eclipse.icon, null, Integer.valueOf(0));
    final IWorkbench wb = PlatformUI.getWorkbench();
    final GUI$Applicator applicator = new Trimmer();
    applicator.setICompilationUnit(currentCompilationUnit);
    int total = 0;
    for (int i = 0; i < MAX_PASSES; ++i) {
      final Int n = new Int(); 
      final IProgressService ps = wb.getProgressService();
      try {
        ps.busyCursorWhile(pm -> {
          applicator.setProgressMonitor(pm);
          applicator.parse();
          applicator.scan();
          n.inner = applicator.suggestionsCount();
          applicator.apply();
        });
      } catch (final InvocationTargetException x) {
        Plugin.log(x);
      } catch (final InterruptedException x) {
        Plugin.info(x);
        return null;
      }
      if (n.inner <= 0) {
        status.append("\n Applied a total of " + total + " suggestions in " + (i + 1) + " rounds");
        return eclipse.announce(status);
      }
      total += n.inner;
      status.append("\n Round " + i + ": " + n.inner + " suggestions (total count " + (i+1) + " passes");
    }
    status.append("\n too many passes; aborting"); 
    throw new ExecutionException(status + "");
  }
}
