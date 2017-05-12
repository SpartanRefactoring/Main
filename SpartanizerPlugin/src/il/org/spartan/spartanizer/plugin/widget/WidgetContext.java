package il.org.spartan.spartanizer.plugin.widget;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;

import il.org.spartan.spartanizer.plugin.*;

/** Widget button activation context. Useful context information to be used by
 * {@link WidgetOperation}. Also used to pass data between operations' phases.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-04-06 */
public class WidgetContext {
  public IProject project;
  public IJavaProject javaProject;
  public Selection currentSelecetion;
  public Selection currentCompilationUnit;
  public Selection allCompilationUnits;
}
