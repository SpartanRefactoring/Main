package il.org.spartan.plugin;

import static il.org.spartan.Utils.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import il.org.spartan.spartanizer.utils.*;

/** A command handler which toggles the spartanization nature
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2013/07/01 */
public final class TipsOnOffToggle extends AbstractHandler {
  private static void disableNature(final IProject p) throws CoreException {
    final IProjectDescription description = p.getDescription();
    final String[] natures = description.getNatureIds();
    for (int i = 0; i < natures.length; ++i)
      if (Nature.NATURE_ID.equals(natures[i])) {
        description.setNatureIds(delete(natures, i));
        p.setDescription(description, null);
        p.accept(r -> {
          if (r instanceof IFile && r.getName().endsWith(".java"))
            Builder.deleteMarkers((IFile) r);
          return true;
        });
      }
  }

  public static void enableNature(final IProject p) throws CoreException {
    final IProjectDescription description = p.getDescription();
    final String[] natures = description.getNatureIds();
    description.setNatureIds(append(natures, Nature.NATURE_ID));
    p.setDescription(description, null);
  }

  private static void toggleNature(final IProject p, final boolean state) throws CoreException {
    // NOTE: In order to ensure that we're not adding the nature when
    // it's
    // already associated with the project, when asked to add the nature
    // first try to remove it and then proceed by adding it
    disableNature(p);
    if (state)
      enableNature(p);
  }

  /** the main method of the command handler, runs when the command is
   * called. */
  @Override public Void execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    final IProject p = getProject();
    if (p == null)
      return null;
    try {
      toggleNature(p, !p.hasNature(Nature.NATURE_ID));
    } catch (final CoreException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return null;
  }

  private static IProject getProject() {
    final IProject p = Selection.Util.project();
    return p != null ? p : null;
  }
}
