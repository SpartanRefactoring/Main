package il.org.spartan.refactoring.builder;

import static il.org.spartan.Utils.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.*;
import org.eclipse.ui.plugin.*;
import org.osgi.framework.*;

/**
 * @author Artium Nihamkin
 * @since 2013/01/01
 * @author Ofir Elmakias
 * @since 2015/09/06 (Updated - auto initialization of the plugin)
 */
public class Plugin extends AbstractUIPlugin implements IStartup {
  private static Plugin plugin;

  /**
   * an empty c'tor. creates an instance of the plugin.
   */
  public Plugin() {
    plugin = this;
  }
  /**
   * Called whenever the plugin is first loaded into the workbench
   */
  @Override public void earlyStartup() {
    System.out.println("Loaded Spartan Refactoring plugin");
    startSpartan();
  }
  @Override public void start(final BundleContext c) throws Exception {
    super.start(c);
    startSpartan();
  }
  @Override public void stop(final BundleContext c) throws Exception {
    plugin = null;
    super.stop(c);
  }
  private static void startSpartan() {
    applyPluginToAllProjects();
    refreshAllProjects();
  }
  /**
   * @return the (single) instance of the plugin
   */
  public static Plugin getDefault() {
    return plugin;
  }
  /**
   * logs an error in the plugin
   *
   * @param t an error
   */
  public static void log(final Throwable t) {
    getDefault().getLog().log(new Status(IStatus.ERROR, "org.spartan.refactoring", 0, t.getMessage(), t));
  }
  /**
   * Add nature to all opened projects
   */
  private static void applyPluginToAllProjects() {
    for (final IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects())
      try {
        if (p.isOpen())
          addNature(p);
      } catch (final CoreException e) {
        e.printStackTrace();
      }
  }
  /**
   * Add nature to one project
   */
  private static void addNature(final IProject p) throws CoreException {
    final IProjectDescription description = p.getDescription();
    final String[] natures = description.getNatureIds();
    if (Arrays.asList(natures).contains(Nature.NATURE_ID))
      return; // Already got the nature
    description.setNatureIds(append(natures, Nature.NATURE_ID));
    p.setDescription(description, null);
  }
  /** */
  public static void refreshAllProjects() {
    for (final IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects())
      try {
        if (p.isOpen())
          p.build(IncrementalProjectBuilder.FULL_BUILD, null);
      } catch (final CoreException e) {
        e.printStackTrace();
      }
  }
}