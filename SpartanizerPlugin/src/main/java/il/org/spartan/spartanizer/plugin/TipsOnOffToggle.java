package il.org.spartan.spartanizer.plugin;

import static il.org.spartan.Utils.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import fluent.ly.*;

/** A command handler which toggles the spartanization nature
 * @see IHandler
 * @see AbstractHandler
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2013/07/01 */
public final class TipsOnOffToggle extends AbstractHandler {
  @SuppressWarnings("boxing") private static void disableNature(final IProject p) throws CoreException {
    final IProjectDescription description = p.getDescription();
    final String[] natures = description.getNatureIds();
    for (final Integer i : range.from(0).to(natures.length))
      if (Nature.NATURE_ID.equals(natures[i])) {
        description.setNatureIds(delete(natures, i));
        p.setDescription(description, null);
        p.accept(λ -> {
          if (λ instanceof IFile && λ.getName().endsWith(".java"))
            Builder.deleteMarkers(λ);
          return true;
        });
      }
  }
  public static void enableNature(final IProject p) throws CoreException {
    final IProjectDescription description = p.getDescription();
    description.setNatureIds(append(description.getNatureIds(), Nature.NATURE_ID));
    p.setDescription(description, null);
  }
  public static void toggleNature(final IProject p, final boolean state) throws CoreException {
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
      note.bug(this, ¢);
    }
    return null;
  }
  private static IProject getProject() {
    final IProject $ = Selection.Util.project();
    return $ != null ? $ : null;
  }
}
