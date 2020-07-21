package il.org.spartan.spartanizer.plugin.widget.operations;

import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.ui.*;

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

  @Deprecated @Override public String imageURL() {
    return "file:/plugin/pictures/cleanIconSmall.png";
  }
  @Override public String imageKey() {
    return ISharedImages.IMG_ETOOL_CLEAR;
  }
  @Override public String description() {
    return "clean project";
  }
  @Override public String[][] configurationComponents() {
    return new String[][] { //
        { MODE, "List", current, all, "Required" },//
    };
  }
  @Override public Function<ImageData, ImageData> scale() {
    return λ -> λ.scaledTo(25, 25);
  }
  @Override public ConfigurationsMap defaultConfiguration() {
    return new ConfigurationsMap().put(MODE, current);
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
