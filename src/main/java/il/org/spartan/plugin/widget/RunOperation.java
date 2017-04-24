package il.org.spartan.plugin.widget;

import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.debug.core.*;

import nano.ly.*;

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

  @Override public boolean register(@SuppressWarnings("hiding") Map<?, ?> configuration) {
    return (configurationName = (String) configuration.get(NAME)) != null //
        && (debug = (Boolean) configuration.get(DEBUG)) != null //
        && load();
  }

  @Override public void onMouseUp(@SuppressWarnings("unused") final WidgetContext __) throws CoreException {
    configuration.launch(!debug.booleanValue() ? ILaunchManager.RUN_MODE : ILaunchManager.DEBUG_MODE, null);
  }

  @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.jdt.debug.ui/icons/full/etool16/run_exc.gif";
  }

  private boolean load() {
    DebugPlugin plugin = DebugPlugin.getDefault();
    if (plugin == null)
      return false;
    ILaunchManager manager = plugin.getLaunchManager();
    if (manager == null)
      return false;
    final List<ILaunchConfiguration> configurations = an.empty.list();
    for (ILaunchConfigurationType ¢ : manager.getLaunchConfigurationTypes())
      try {
        ILaunchConfiguration[] cs = manager.getLaunchConfigurations(¢);
        if (cs != null)
          Collections.addAll(configurations, cs);
      } catch (CoreException e) {
        note.cancel(e);
      }
    return (configuration = configurations.stream() //
        .filter(λ -> configurationName.equals(λ.getName())) //
        .findFirst().orElseGet(() -> null)) != null;
  }
}
