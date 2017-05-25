package il.org.spartan.spartanizer.plugin.widget.operations;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.plugin.widget.*;

/** A widget operation that cleans projects
 * @author Niv Shalmon
 * @since 2017-05-04 */
public class CleanOperation extends WidgetOperation {
  private static final long serialVersionUID = -0x4C283B73F1B10E73L;
  public static final String MODE = "mode";
  public static final String PROJECTS = "projects";
  public static final String current = "current project";
  public static final String all = "all projects";
  private String mode = current;

  @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.mylyn.commons.ui/icons/elcl16/checkboxcleared.gif";
  }
  @Override public String description() {
    return "clean project";
  }
  @Override public String[][] configurationComponents() {
    return new String[][] { //
        { MODE, "List", current, all, "Required" },//
    };
  }
  @Override protected boolean defaultConfiguration() {
    mode = current;
    return true;
  }
  @Override public boolean register(final ConfigurationsMap ¢) {
    return is.in(mode = ¢.getString(MODE), current, all);
  }
  @Override public void onMouseUp(final WidgetContext c) throws Throwable {
    switch (mode) {
      case current:
        c.project.build(IncrementalProjectBuilder.CLEAN_BUILD, new NullProgressMonitor());
        break;
      case all:
        c.allProjects.forEach(p -> {
          try {
            p.build(IncrementalProjectBuilder.CLEAN_BUILD, new NullProgressMonitor());
          } catch (final CoreException ¢) {
            note.bug(¢);
          }
        });
    }
  }
}
