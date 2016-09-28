package il.org.spartan.plugin;

import org.eclipse.ui.*;
import org.eclipse.ui.plugin.*;
import org.osgi.framework.*;

/** @author Artium Nihamkin
 * @since 2013/01/01
 * @author Ofir Elmakias
 * @since 2015/09/06 (Updated - auto initialization of the plugin) */
public final class Plugin extends AbstractUIPlugin implements IStartup {
  private static Plugin plugin;

  public static AbstractUIPlugin plugin() {
    return plugin;
  }

  private static void startSpartan() {
    SpartanizeableAll.go();
    RefreshAll.go();
  }

  /** an empty c'tor. creates an instance of the plugin. */
  public Plugin() {
    plugin = this;
  }

  /** Called whenever the plugin is first loaded into the workbench */
  @Override public void earlyStartup() {
    LoggingManner.info("EARLY STATRTUP: spartanizer");
    startSpartan();
  }

  @Override protected void loadDialogSettings() {
    LoggingManner.info("LDS: spartanizer");
    super.loadDialogSettings();
  }

  @Override protected void refreshPluginActions() {
    LoggingManner.info("RPA: spartanizer");
    super.refreshPluginActions();
  }

  @Override protected void saveDialogSettings() {
    LoggingManner.info("SDS: spartanizer");
    super.saveDialogSettings();
  }

  @Override public void start(final BundleContext ¢) throws Exception {
    super.start(¢);
    LoggingManner.info("START: spartnizer");
    startSpartan();
  }

  @Override public void stop(final BundleContext ¢) throws Exception {
    LoggingManner.info("STOP: spartnizer");
    plugin = null;
    super.stop(¢);
  }
}