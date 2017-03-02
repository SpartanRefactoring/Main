package il.org.spartan.plugin.old;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.utils.*;

/** *
 * @author Yossi Gil
 * @since Oct 16, 2016 */
public final class RefreshAll extends BaseHandler {
  public static void go() {
    for (final IProject ¢ : ResourcesPlugin.getWorkspace().getRoot().getProjects())
      go(¢);
  }

  public static void go(final IProject p) {
    final IProgressMonitor npm = new NullProgressMonitor();
    new Thread(() -> {
      try {
        if (p.isOpen() && p.getNature(Nature.NATURE_ID) != null)
          p.build(IncrementalProjectBuilder.FULL_BUILD, npm);
      } catch (final CoreException ¢) {
        monitor.logEvaluationError(new RefreshAll(), ¢);
      }
    }).run();
  }

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    go();
    return null;
  }
}
