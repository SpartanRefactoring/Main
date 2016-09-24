package il.org.spartan.plugin;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

/** ??
 * @author Yossi Gil
 * @year 2016 */
public final class RefreshAll extends BaseHandler {
  public static void go() {
    for (final IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects())
      try {
        if (p.isOpen())
          p.build(IncrementalProjectBuilder.FULL_BUILD, null);
      } catch (final CoreException e) {
        LoggingManner.logEvaluationError(new RefreshAll(), e);
      }
  }

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    go();
    return null;
  }
}
