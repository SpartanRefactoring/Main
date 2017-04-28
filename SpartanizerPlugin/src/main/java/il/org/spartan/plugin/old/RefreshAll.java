package il.org.spartan.plugin.old;

import java.util.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.plugin.*;

/** *
 * @author Yossi Gil
 * @since Oct 16, 2016 */
public final class RefreshAll extends BaseHandler {
  public static Set<IProject> waitingForRefresh = Collections.synchronizedSet(new HashSet<>());

  public static void go() {
    as.list(ResourcesPlugin.getWorkspace().getRoot().getProjects()).forEach(λ -> go(λ));
  }

  public static void go(final IProject p) {
    final IProgressMonitor npm = new NullProgressMonitor();
    new Thread(() -> {
      try {
        if (p.isOpen() && p.getNature(Nature.NATURE_ID) != null) {
          waitingForRefresh.add(p);
          p.touch(npm);
        }
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
