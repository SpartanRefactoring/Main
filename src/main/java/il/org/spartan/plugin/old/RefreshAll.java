package il.org.spartan.plugin.old;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.fluent.*;

/** *
 * @author Yossi Gil
 * @since Oct 16, 2016 */
public final class RefreshAll extends BaseHandler {
  public static void go() {
    as.list(ResourcesPlugin.getWorkspace().getRoot().getProjects()).forEach(λ -> go(λ));
  }

  public static void go(final IProject p) {
    final IProgressMonitor npm = new NullProgressMonitor();
    new Thread(() -> {
      try {
        if (p.isOpen() && p.getNature(Nature.NATURE_ID) != null)
          p.touch(npm);
        // see issue #767
        // p.build(IncrementalProjectBuilder.FULL_BUILD, npm);
      } catch (final CoreException ¢) {
        note.bug(new RefreshAll(), ¢);
      }
    }).run();
  }

  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    go();
    return null;
  }
}
