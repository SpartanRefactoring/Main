package il.org.spartan.plugin;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

/** ??
 * @author Yossi Gil
 * @year 2016 */
public final class SpartanizeableAll extends BaseHandler {
  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    go();
    return null;
  }

  public void go() {
    for (final IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects())
      if (p.isOpen())
        try {
          Plugin.addNature(p);
        } catch (final CoreException e) {
          Plugin.logEvaluationError(this, e);
        }
  }
}
