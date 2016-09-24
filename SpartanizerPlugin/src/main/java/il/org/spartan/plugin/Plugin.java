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

  /** To be invoked whenever you do not know what to do with an exception
   * @param o JD
   * @param x JD */
  public static void logCancellationRequest(final Object o, final Exception x) {
    LoggingManner.now.log(//
        "An instane of " + o.getClass().getSimpleName() + //
            "\n was hit by a " + x.getClass().getSimpleName() + //
            " (probably cancellation) exception." + //
            "\n x = '" + x + "'" + //
            "\n o = " + o + "'");
  }

  public static void logEvaluationError(final Object o, final Exception x) {
    System.err.println(//
        "An instane of " + o.getClass().getSimpleName() + //
            "\n was hit by a " + x.getClass().getSimpleName() + //
            "\n      exeption, probably due to unusual " + //
            "\n      Java constructs in the input " + //
            "\n   x = '" + x + "'" + //
            "\n   o = " + o + "'");
  }

  public static void logProbableBug(final Object o, final Throwable t) {
    LoggingManner.now.log(//
        "An instane of " + o.getClass().getSimpleName() + //
            "\n was hit by a " + t.getClass().getSimpleName() + //
            " exception, which may indicate a bug somwhwere." + //
            "\n x = '" + t + "'" + //
            "\n o = " + o + "'");
  }

  public static AbstractUIPlugin plugin() {
    return plugin;
  }

  /** logs an error in the plugin
   * @param t an error */
  public static void xlog(final Throwable ¢) {
    LoggingManner.now.log(¢ + "");
  }

  /** Add nature to one project */
  static void addNature(final IProject p) throws CoreException {
    final IProjectDescription d = p.getDescription();
    final String[] natures = d.getNatureIds();
    if (as.list(natures).contains(Nature.NATURE_ID))
      return; // Already got the nature
    d.setNatureIds(append(natures, Nature.NATURE_ID));
    p.setDescription(d, null);
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

  /** Add nature to all opened projects */
  @Deprecated void applyPluginToAllProjects() {
    for (final IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects())
      if (p.isOpen())
        try {
          addNature(p);
        } catch (final CoreException e) {
          Plugin.logEvaluationError(this, e);
        }
  }
}