package il.org.spartan.plugin;

import java.lang.reflect.*;

import javax.swing.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ui.*;
import org.eclipse.ui.progress.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** A handler for {@link Tips} This handler executes all safe spartanizations in
 * selected range, while exposing static methods to
 * spartanize only specific compilation units.
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @since 2015/08/01 */
public abstract class LaconizeSelection extends BaseHandler {
  private final int MAX_PASSES = 20;
  
  public abstract Range getSelection(ICompilationUnit u);
  
  public abstract boolean isRepeating();

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) throws ExecutionException {
    final ICompilationUnit currentCompilationUnit = eclipse.currentCompilationUnit();
    final StringBuilder status = new StringBuilder("Spartanizing " + currentCompilationUnit.getElementName());
    new JOptionPane(status, JOptionPane.INFORMATION_MESSAGE, JOptionPane.NO_OPTION, eclipse.icon, null, Integer.valueOf(0));
    final IWorkbench wb = PlatformUI.getWorkbench();
    final GUI$Applicator applicator = new Trimmer();
    applicator.setICompilationUnit(currentCompilationUnit);
    int i, total = 0;
    for (i = 0; i < (isRepeating() ? MAX_PASSES : 1); ++i) {
      final Int n = new Int();
      final IProgressService ps = wb.getProgressService();
      try {
        ps.busyCursorWhile(pm -> {
          applicator.setProgressMonitor(pm);
          pm.setTaskName(status + "");
          applicator.parse();
          applicator.scan();
          n.inner = applicator.TipsCount();
          applicator.apply(currentCompilationUnit, getSelection(currentCompilationUnit));
        });
      } catch (final InvocationTargetException x) {
        LoggingManner.logEvaluationError(this, x);
      } catch (final InterruptedException x) {
        LoggingManner.logCancellationRequest(this, x);
        return null;
      }
      if (n.inner <= 0) {
        status.append("\n Applied a total of " + total + " tips in " + i + " rounds");
        return eclipse.announce(status);
      }
      status.append("\n Round " + (i + 1) + ": " + n.inner + " tips (previous rounds: " + total + " tips");
      total += n.inner;
    }
    if (i != MAX_PASSES) {
      status.append("\n Applied a total of " + total + " tips in " + i + " rounds");
      return eclipse.announce(status);
    }
    status.append("\n too many passes; aborting");
    throw new ExecutionException(status + "");
  }
  
  /** A handler for {@link Tips} This handler executes all safe spartanizations on
   * marker enclosure, while exposing static methods to
   * spartanize only specific compilation units.
   * @author Ori Roth
   * @since 2016 */
  public static final class Enclosure extends LaconizeSelection implements IMarkerResolution {
    IMarker marker;
    Class<? extends ASTNode> clazz;
    String label;
    
    public Enclosure(Class<? extends ASTNode> clazz, String label) {
      this.clazz = clazz;
      this.label = label;
    }
    
    @Override public Range getSelection(ICompilationUnit u) {
      ASTNode n = eclipse.getNodeByMarker(u, marker);
      if (n == null)
        return new Range(0, 0); // TODO Ori: replace with empty range
      ASTNode a = searchAncestors.forClass(clazz).from(n);
      return a == null ? new Range(n.getStartPosition(), n.getStartPosition() + n.getLength())
          : new Range(a.getStartPosition(), a.getStartPosition() + a.getLength());
    }

    @Override public String getLabel() {
      return label;
    }

    @Override public void run(IMarker m) {
      marker = m;
      try {
        execute(null);
      } catch (ExecutionException x) {
        // TODO Ori: log it
        x.printStackTrace();
      }
    }

    @Override public boolean isRepeating() {
      return false;
    }
  }
}
