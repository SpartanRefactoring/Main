package il.org.spartan.plugin.old;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import il.org.spartan.utils.*;

/** ??
 * @author Yossi Gil
 * @year 2016 */
public final class SpartanizeableAll extends BaseHandler {
  public static void go() {
    for (final IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects())
      if (p.isOpen())
        try {
          eclipse.addNature(p);
        } catch (final CoreException e) {
          monitor.logEvaluationError(new SpartanizeableAll(), e);
        }
  }
  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    go();
    return null;
  }
}
