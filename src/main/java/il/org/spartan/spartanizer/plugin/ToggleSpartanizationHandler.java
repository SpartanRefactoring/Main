package il.org.spartan.spartanizer.plugin;

import static il.org.spartan.Utils.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.handlers.*;

/** A command handler which toggles the spartanization nature
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code>
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2013/07/01 */
public class ToggleSpartanizationHandler extends AbstractHandler {
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

  private static void enableNature(final IProject p) throws CoreException {
    final IProjectDescription description = p.getDescription();
    final String[] natures = description.getNatureIds();
    description.setNatureIds(append(natures, Nature.NATURE_ID));
    p.setDescription(description, null);
  }

  private static Void execute(final ISelection s, final boolean state) throws CoreException {
    if (s instanceof IStructuredSelection)
      for (final Object o : ((IStructuredSelection) s).toList()) {
        final IProject p = extractProject(o);
        if (p != null)
          toggleNature(p, state);
      }
    return null;
  }

  private static IProject extractProject(final Object o) {
    return o instanceof IProject ? (IProject) o : o instanceof IAdaptable ? (IProject) ((IAdaptable) o).getAdapter(IProject.class) : null;
  }

  private static void toggleNature(final IProject p, final boolean state) throws CoreException {
    // NOTE: In order to ensure that we're not adding the nature when it's
    // already associated with the project, when asked to add the nature
    // first
    // try to remove it and then proceed by adding it
    disableNature(p);
    if (state)
      enableNature(p);
  }

  /** the main method of the command handler, runs when the command is
   * called. */
  @Override public Void execute(final ExecutionEvent e) throws ExecutionException {
    // Invert the old value to get the new
    final boolean newValue = !HandlerUtil.toggleCommandState(e.getCommand());
    try {
      return execute(HandlerUtil.getCurrentSelectionChecked(e), newValue);
    } catch (final CoreException x) {
      throw new ExecutionException(x.getMessage());
    }
  }
}
