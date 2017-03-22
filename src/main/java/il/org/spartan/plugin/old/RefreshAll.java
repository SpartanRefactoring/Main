package il.org.spartan.plugin.old;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.plugin.*;
import il.org.spartan.utils.*;

/** *
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Oct 16, 2016 */
public final class RefreshAll extends BaseHandler {
  public static void go() {
    as.list(ResourcesPlugin.getWorkspace().getRoot().getProjects()).forEach(λ -> go(λ));
  }

  public static void go(@NotNull final IProject p) {
    final IProgressMonitor npm = new NullProgressMonitor();
    new Thread(() -> {
      try {
        if (p.isOpen() && p.getNature(Nature.NATURE_ID) != null)
          p.build(IncrementalProjectBuilder.FULL_BUILD, npm);
      } catch (@NotNull final CoreException ¢) {
        monitor.logEvaluationError(new RefreshAll(), ¢);
      }
    }).run();
  }

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    go();
    return null;
  }
}
