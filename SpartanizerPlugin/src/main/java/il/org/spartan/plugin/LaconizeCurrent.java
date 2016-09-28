package il.org.spartan.plugin;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.*;

import il.org.spartan.utils.*;

/** A handler for {@link Tips} This handler executes all safe spartanizations on
 * current window, while exposing static methods to spartanize only specific
 * compilation units.
 * @author Ori Roth
 * @since 2016 */
public final class LaconizeCurrent extends LaconizeSelection implements IMarkerResolution {
  @Override public Range getSelection(@SuppressWarnings("unused") final ICompilationUnit __) {
    return new Range(0, 0);
  }

  @Override public boolean isRepeating() {
    return true;
  }

  @Override public String getLabel() {
    return "Laconize file";
  }

  @Override public void run(@SuppressWarnings("unused") IMarker __) {
    try {
      execute();
    } catch (ExecutionException x) {
      // TODO Ori: log it
      x.printStackTrace();
    }
  }
}
