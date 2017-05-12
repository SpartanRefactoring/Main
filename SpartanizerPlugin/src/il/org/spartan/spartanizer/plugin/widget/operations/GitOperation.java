package il.org.spartan.spartanizer.plugin.widget.operations;

import java.io.*;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.storage.file.*;

import il.org.spartan.spartanizer.plugin.widget.*;

/** Git pull command.
 * @author Ori Roth
 * @since 2017-04-24 */
public abstract class GitOperation extends WidgetOperation {
  private static final long serialVersionUID = 0xB18576ECF23E9C3L;

  protected abstract void gitOperation(Git g) throws Throwable;

  @Override public void onMouseUp(final WidgetContext c) throws Throwable {
    if (c.project == null || !c.project.exists())
      return;
    final File f = c.project.getRawLocation().toFile();
    if (f == null || !f.exists())
      return;
    final FileRepositoryBuilder builder = new FileRepositoryBuilder().findGitDir(f);
    if (builder != null)
      try (Repository repo = builder.build()) {
        if (repo != null)
          try (Git git = new Git(repo)) {
            gitOperation(git);
          }
      }
  }
}
