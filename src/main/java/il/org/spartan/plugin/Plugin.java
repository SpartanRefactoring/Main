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
public class Plugin extends AbstractUIPlugin implements IStartup {
  private static Plugin plugin;

  /** logs an error in the plugin
   * @param t an error */
  public static void log(final Throwable ¢) {
    plugin.getLog().log(new Status(IStatus.ERROR, "il.org.spartan.spartanizer", 0, ¢.getMessage(), ¢));
  }

  public static AbstractUIPlugin plugin() {
    return plugin;
  }

  public static void refreshAllProjects() {
    for (final IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects())
      try {
        if (p.isOpen())
          p.build(IncrementalProjectBuilder.FULL_BUILD, null);
      } catch (final CoreException e) {
        Plugin.log(e);
        ;
      }
  }

  /** Add nature to one project */
  private static void addNature(final IProject p) throws CoreException {
    final IProjectDescription d = p.getDescription();
    final String[] natures = d.getNatureIds();
    if (as.list(natures).contains(Nature.NATURE_ID))
      return; // Already got the nature
    d.setNatureIds(append(natures, Nature.NATURE_ID));
    p.setDescription(d, null);
  }

  /** Add nature to all opened projects */
  private static void applyPluginToAllProjects() {
    for (final IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects())
      if (p.isOpen())
        try {
          addNature(p);
        } catch (final CoreException e) {
          log(e);
        }
  }

  private static void startSpartan() {
    applyPluginToAllProjects();
    refreshAllProjects();
  }

  /** an empty c'tor. creates an instance of the plugin. */
  public Plugin() {
    plugin = this;
  }

  /** Called whenever the plugin is first loaded into the workbench */
  @Override public void earlyStartup() {
    System.out.println("Loaded Spartan Refactoring plugin");
    startSpartan();
  }

  @Override public void start(final BundleContext ¢) throws Exception {
    super.start(¢);
    startSpartan();
  }

  @Override public void stop(final BundleContext ¢) throws Exception {
    plugin = null;
    super.stop(¢);
  }
}