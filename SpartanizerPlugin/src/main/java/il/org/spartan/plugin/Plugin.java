package il.org.spartan.plugin;

import static il.org.spartan.Utils.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.*;
import org.eclipse.ui.plugin.*;
import org.osgi.framework.*;

import il.org.spartan.*;

/** @author Artium Nihamkin
 * @since 2013/01/01
 * @author Ofir Elmakias
 * @since 2015/09/06 (Updated - auto initialization of the plugin) */
public final class Plugin extends AbstractUIPlugin implements IStartup {
  private static Plugin plugin;

  public static AbstractUIPlugin plugin() {
    return plugin;
  }

  /** an empty c'tor. creates an instance of the plugin. */
  public Plugin() {
    plugin = this;
  }

  /** Called whenever the plugin is first loaded into the workbench */
  @Override public void earlyStartup() {
    LoggingManner.now.info("EARLY STATRTUP: spartanizer");
  }

  @Override public void start(final BundleContext ¢) throws Exception {
    super.start(¢);
    LoggingManner.now.info("START: spartnizer");
  }

  @Override public void stop(final BundleContext ¢) throws Exception {
    LoggingManner.now.info("STOP: spartnizer");
    plugin = null;
    super.stop(¢);
  }

  @Override protected void loadDialogSettings() {
    LoggingManner.now.info("LDS: spartanizer");
    super.loadDialogSettings();
  }

  @Override protected void refreshPluginActions() {
    LoggingManner.now.info("RPA: spartanizer");
    super.refreshPluginActions();
  }

  @Override protected void saveDialogSettings() {
    LoggingManner.now.info("SDS: spartanizer");
    super.saveDialogSettings();
  }
}