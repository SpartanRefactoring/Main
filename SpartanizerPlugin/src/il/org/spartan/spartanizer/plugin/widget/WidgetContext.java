package il.org.spartan.spartanizer.plugin.widget;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;

import il.org.spartan.spartanizer.plugin.*;

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
    final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    $.allProjects = Arrays.asList(workspaceRoot.getProjects());
    $.currentSelecetion = Selection.Util.current();
    $.currentCompilationUnit = Selection.Util.getCurrentCompilationUnit();
    $.allCompilationUnits = Selection.Util.getAllCompilationUnits();
    return $;
  }
}
