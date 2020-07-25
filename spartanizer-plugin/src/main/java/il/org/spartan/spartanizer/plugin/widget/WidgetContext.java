package il.org.spartan.spartanizer.plugin.widget;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;

import il.org.spartan.spartanizer.plugin.Selection;

/** Widget button activation context. Useful context information to be used by
 * {@link WidgetOperation}. Also used to pass data between operations' phases.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @author Niv Shalmon
 * @since 2017-04-06 */
public class WidgetContext {
  public IProject project;
  public IJavaProject javaProject;
  public List<IProject> allProjects;
  public Selection currentSelecetion;
  public Selection currentCompilationUnit;
  public Selection allCompilationUnits;

  /** @return a WidgetContext representing the current context of the
   *         workspace */
  public static WidgetContext generateContext() {
    final WidgetContext $ = new WidgetContext();
    $.project = Selection.Util.project();
    $.javaProject = Selection.Util.getJavaProject();
    $.allProjects = Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().getProjects());
    $.currentSelecetion = Selection.Util.current();
    $.currentCompilationUnit = Selection.Util.getCurrentCompilationUnit();
    $.allCompilationUnits = Selection.Util.getAllCompilationUnits();
    return $;
  }
}
