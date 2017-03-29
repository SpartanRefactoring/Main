package il.org.spartan.plugin;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.plugin.preferences.revision.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Disable tipper, removing it from XML file.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-03-29 */
public class DisableTipper {
  /** Disable tipper of this marker.
   * @param m spartanization marker */
  @SuppressWarnings("unchecked") public static void disable(final IMarker m) {
    if (m != null)
      try {
        disable((Class<? extends Tipper<?>>) m.getAttribute(Builder.SPARTANIZATION_TIPPER_KEY), m.getResource().getProject());
      } catch (CoreException x) {
        monitor.log(x);
      }
  }

  /** Disable tipper.
   * @param tipperClass JD
   * @param p current project */
  public static void disable(final Class<? extends Tipper<?>> tipperClass, final IProject p) {
    if (tipperClass == null || p == null || !p.exists() || !p.isOpen())
      return;
    final Set<Class<Tipper<? extends ASTNode>>> enabledTippers = XMLSpartan.enabledTippers(p);
    if (!enabledTippers.contains(tipperClass))
      return;
    enabledTippers.remove(tipperClass);
    XMLSpartan.updateEnabledTippers(p, enabledTippers.stream().map(λ -> λ.getSimpleName()).collect(Collectors.toList()));
    try {
      Eclipse.refreshProject(p);
    } catch (InvocationTargetException | CoreException | InterruptedException ¢) {
      monitor.log(¢);
    }
  }
}
