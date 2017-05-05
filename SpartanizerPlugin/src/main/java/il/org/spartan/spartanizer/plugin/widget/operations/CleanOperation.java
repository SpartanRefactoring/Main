package il.org.spartan.spartanizer.plugin.widget.operations;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.plugin.widget.*;

/** TODO Niv Shalmon: document class
 * @author Niv Shalmon
 * @since 2017-05-04 */
public class CleanOperation extends WidgetOperation {
  public static final String MODE = "mode";
  public static final String PROJECTS = "projects";
  public static final String current = "current project";
  public static final String all = "all projects";
  // public static final String selected = "selected projects";
  private String mode;

  @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.mylyn.commons.ui/icons/elcl16/checkboxcleared.gif";
  }

  @Override public String description() {
    return "clean project";
  }

  @Override public String[][] configurationComponents() {
    return new String[][] { //
        { MODE, "List", current, all, /* selected, */ "Required" },//
        // {PROJECTS, ""},//
    };
  }

  @Override public boolean register(final Map<?, ?> configuration) {
    return is.in(mode = (String) configuration.get(MODE), current, all);
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
          } catch (final CoreException x) {
            note.bug(x);
          }
        });
    }
  }
}
