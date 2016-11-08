package il.org.spartan.plugin.old;

import java.lang.reflect.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ui.*;
import org.eclipse.ui.progress.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** A handler for {@link Tips} This handler executes all safe spartanizations in
 * selected range, while exposing static methods to spartanize only specific
 * compilation units.
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code>
 * @since 2015/08/01 */
public abstract class LaconizeSelection extends BaseHandler {
  private final int MAX_PASSES = 20;

  public Void execute() throws ExecutionException {
    final ICompilationUnit currentCompilationUnit = eclipse.currentCompilationUnit();
    final StringBuilder status = new StringBuilder("Spartanizing " + currentCompilationUnit.getElementName());
    final IWorkbench wb = PlatformUI.getWorkbench();
    final AbstractGUIApplicator applicator = new Trimmer();
    applicator.setICompilationUnit(currentCompilationUnit);
    int i, total = 0;
    for (i = 0; i < (!isRepeating() ? 1 : MAX_PASSES); ++i) {
      final Int n = new Int();
      final IProgressService ps = wb.getProgressService();
      try {
        ps.busyCursorWhile(pm -> {
          // applicator.setProgressMonitor(pm);
          pm.setTaskName(status + "");
          applicator.parse();
          applicator.scan();
          n.inner = applicator.TipsCount();
          final Range r = getSelection(currentCompilationUnit);
          if (r != null)
            applicator.apply(currentCompilationUnit, r);
        });
      } catch (final InvocationTargetException x) {
        monitor.logEvaluationError(this, x);
      } catch (final InterruptedException x) {
        monitor.logCancellationRequest(this, x);
        return null;
      }
      if (n.inner <= 0) {
        status.append("\n Applied a total of " + total + " tips in " + i + " rounds");
        return eclipse.announce(status);
      }
      status.append("\n Round " + (i + 1) + ": " + n.inner + " tips (previous rounds: " + total + " tips)");
      total += n.inner;
    }
    if (i != MAX_PASSES) {
      status.append("\n Applied a total of " + total + " tips in " + i + " rounds");
      return eclipse.announce(status);
    }
    status.append("\n too many passes; aborting");
    throw new ExecutionException(status + "");
  }
  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) throws ExecutionException {
    return execute();
  }
  /** @param u JD
   * @return selection chosen for spartanization */
  public abstract Range getSelection(ICompilationUnit u);
  /** @return <code><b>true</b></code> <em>iff</em> the handler runs in a loop,
   *         for {@link LaconizeSelection#MAX_PASSES} times */
  public abstract boolean isRepeating();

  /** A handler for {@link Tips} executing all safe spartanizations on marker
   * enclosure, while exposing static methods to spartanize only specific
   * compilation units.
   * @author Ori Roth
   * @since 2016 */
  public static final class Enclosure extends LaconizeSelection implements IMarkerResolution {
    IMarker marker;
    Class<? extends ASTNode> clazz;
    String label;

    public Enclosure(final Class<? extends ASTNode> clazz, final String label) {
      this.clazz = clazz;
      this.label = label;
    }
    @Override public String getLabel() {
      return label;
    }
    @Override public Range getSelection(final ICompilationUnit u) {
      final ASTNode n = eclipse.getNodeByMarker(u, marker);
      if (n == null)
        return null;
      final ASTNode a = searchAncestors.forClass(clazz).from(n);
      return a == null ? new Range(n.getStartPosition(), n.getStartPosition() + n.getLength())
          : new Range(a.getStartPosition(), a.getStartPosition() + a.getLength());
    }
    @Override public boolean isRepeating() {
      return false;
    }
    @Override public void run(final IMarker m) {
      marker = m;
      try {
        execute();
      } catch (final ExecutionException x) {
        monitor.logEvaluationError(this, x);
      }
    }
  }
}
