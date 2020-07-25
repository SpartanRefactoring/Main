package il.org.spartan.spartanizer.plugin.widget.operations;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.ui.ISharedImages;

import fluent.ly.note;
import il.org.spartan.spartanizer.plugin.widget.ConfigurationsMap;
import il.org.spartan.spartanizer.plugin.widget.WidgetContext;
import il.org.spartan.spartanizer.plugin.widget.WidgetOperation;

/** Runs predefined run/debug configuration.
 * @author Ori Roth
 * @since 2017-04-24 */
public class RunOperation extends WidgetOperation {
  private static final long serialVersionUID = -0xBBDFF0B54B0BEDBL;
  public static final String NAME = "name";
  public static final String DEBUG = "debug";
  private String configurationName;
  private Boolean debug;
  private ILaunchConfiguration configuration;

  @Override public String description() {
    return "Activate run/debug configuration";
  }
  @Override public String[][] configurationComponents() {
    return new String[][] { //
        { NAME, "String", "Run configuration name", "REQUIRED" }, //
        { DEBUG, "Boolean", "Debug", "REQUIRED" }, //
    };
  }
  @Override public ConfigurationsMap defaultConfiguration() {
    return null;
  }
  @Override public boolean register(final ConfigurationsMap ¢) {
    return (configurationName = ¢.getString(NAME)) != null //
        && (debug = ¢.getBoolean(DEBUG)) != null //
        && load();
  }
  @Override public void onMouseUp(@SuppressWarnings("unused") final WidgetContext __) throws CoreException {
    configuration.launch(!debug.booleanValue() ? ILaunchManager.RUN_MODE : ILaunchManager.DEBUG_MODE, null);
  }
  @Deprecated @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.jdt.debug.ui/icons/full/etool16/run_exc.gif";
  }
  @Override public String imageKey() {
    return ISharedImages.IMG_TOOL_FORWARD;
  }
  private boolean load() {
    final DebugPlugin plugin = DebugPlugin.getDefault();
    if (plugin == null)
      return false;
    final ILaunchManager manager = plugin.getLaunchManager();
    if (manager == null)
      return false;
    final List<ILaunchConfiguration> configurations = an.empty.list();
    for (final ILaunchConfigurationType ¢ : manager.getLaunchConfigurationTypes())
      try {
        final ILaunchConfiguration[] cs = manager.getLaunchConfigurations(¢);
        if (cs != null)
          Collections.addAll(configurations, cs);
      } catch (final CoreException ignore) {
        note.cancel(ignore);
      }
    return (configuration = configurations.stream() //
        .filter(λ -> configurationName.equals(λ.getName())) //
        .findFirst().orElseGet(() -> null)) != null;
  }
}
