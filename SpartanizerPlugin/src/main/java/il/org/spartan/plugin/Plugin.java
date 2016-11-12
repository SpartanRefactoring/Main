package il.org.spartan.plugin;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.*;
import org.eclipse.ui.*;
import org.eclipse.ui.plugin.*;
import org.osgi.framework.*;

import il.org.spartan.plugin.old.*;
import il.org.spartan.utils.*;

/** @author Artium Nihamkin
 * @since 2013/01/01
 * @author Ofir Elmakias
 * @since 2015/09/06 (Updated - auto initialization of the plugin)
 * @author Ori Roth
 * @since 2.6 (Updated - apply nature to newly opened projects)
 *        [[SuppressWarningsSpartan]] */
public final class Plugin extends AbstractUIPlugin implements IStartup {
  private static Plugin plugin;
  private static boolean listening;
  private static final int SAFETY_DELAY = 100;

  public static AbstractUIPlugin plugin() {
    return plugin;
  }
  private static void startSpartan() {
    addPartListener();
    SpartanizeableAll.go();
    RefreshAll.go();
  }
  public Plugin() {
    plugin = this;
  }
  /** Called whenever the plugin is first loaded into the workbench */
  @Override public void earlyStartup() {
    monitor.debug("EARLY STATRTUP: gUIBatchLaconizer");
    startSpartan();
  }
  @Override public void start(final BundleContext ¢) throws Exception {
    super.start(¢);
    monitor.debug("START: gUIBatchLaconizer");
    startSpartan();
  }
  @Override public void stop(final BundleContext ¢) throws Exception {
    monitor.debug("STOP: spartnizer");
    plugin = null;
    super.stop(¢);
  }
  @Override protected void loadDialogSettings() {
    monitor.debug("LDS: gUIBatchLaconizer");
    super.loadDialogSettings();
  }
  @Override protected void refreshPluginActions() {
    monitor.debug("RPA: gUIBatchLaconizer");
    super.refreshPluginActions();
  }
  @Override protected void saveDialogSettings() {
    monitor.debug("SDS: gUIBatchLaconizer");
    super.saveDialogSettings();
  }
  private static void addPartListener() {
    if (listening)
      return;
    final IWorkspace w = ResourcesPlugin.getWorkspace();
    if (w == null)
      return;
    w.addResourceChangeListener(e -> {
      if (e == null || e.getDelta() == null || !PreferencesResources.NEW_PROJECTS_ENABLE_BY_DEFAULT_VALUE.get())
        return;
      try {
        final MProject mp = new MProject();
        e.getDelta().accept(d -> {
          if (d == null || d.getResource() == null || !(d.getResource() instanceof IProject))
            return true;
          final IProject p = (IProject) d.getResource();
          if (d.getKind() == IResourceDelta.ADDED) {
            mp.p = p;
            mp.type = Type.new_project;
          }
          // else if (d.getKind() == IResourceDelta.CHANGED && p.isOpen()) {
          // mp.p = p;
          // mp.type = Type.opened_project;
          // }
          return true;
        });
        if (mp.p != null)
          Job.createSystem(pm -> {
            try {
              switch (mp.type) {
                case new_project:
                  eclipse.addNature(mp.p);
                  mp.p.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
                  break;
                // case opened_project:
                // if
                // (as.list(mp.p.getDescription().getNatureIds()).contains(Nature.NATURE_ID))
                // TipsOnOffToggle.enableNature(mp.p);
                // break;
                default:
                  break;
              }
            } catch (final Exception x) {
              monitor.log(x);
            }
          }).schedule(SAFETY_DELAY);
      } catch (final CoreException x) {
        monitor.log(x);
      }
    });
    listening = true;
  }

  /* TODO Roth: don't use enums, prefer strings, which require less overhead,
   * and are easier to debug; --yg */
  static enum Type {
    new_project, opened_project
  }

  /** TODO Roth: not convinced it is required. --yg */
  static class MProject {
    public IProject p;
    public Type type;
  }
}